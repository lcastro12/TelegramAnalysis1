package org.telegram.messenger.exoplayer2.audio;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.AudioFormat;
import android.media.AudioTimestamp;
import android.media.AudioTrack;
import android.os.ConditionVariable;
import android.os.SystemClock;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import org.telegram.messenger.exoplayer2.C0600C;
import org.telegram.messenger.exoplayer2.PlaybackParameters;
import org.telegram.messenger.exoplayer2.audio.AudioSink.ConfigurationException;
import org.telegram.messenger.exoplayer2.audio.AudioSink.InitializationException;
import org.telegram.messenger.exoplayer2.audio.AudioSink.Listener;
import org.telegram.messenger.exoplayer2.audio.AudioSink.WriteException;
import org.telegram.messenger.exoplayer2.upstream.cache.CacheDataSink;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.Util;

public final class DefaultAudioSink implements AudioSink {
    private static final int BUFFER_MULTIPLICATION_FACTOR = 4;
    private static final int ERROR_BAD_VALUE = -2;
    private static final long MAX_AUDIO_TIMESTAMP_OFFSET_US = 5000000;
    private static final long MAX_BUFFER_DURATION_US = 750000;
    private static final long MAX_LATENCY_US = 5000000;
    private static final int MAX_PLAYHEAD_OFFSET_COUNT = 10;
    private static final long MIN_BUFFER_DURATION_US = 250000;
    private static final int MIN_PLAYHEAD_OFFSET_SAMPLE_INTERVAL_US = 30000;
    private static final int MIN_TIMESTAMP_SAMPLE_INTERVAL_US = 500000;
    private static final int MODE_STATIC = 0;
    private static final int MODE_STREAM = 1;
    private static final long PASSTHROUGH_BUFFER_DURATION_US = 250000;
    private static final int PLAYSTATE_PAUSED = 2;
    private static final int PLAYSTATE_PLAYING = 3;
    private static final int PLAYSTATE_STOPPED = 1;
    private static final int START_IN_SYNC = 1;
    private static final int START_NEED_SYNC = 2;
    private static final int START_NOT_SET = 0;
    private static final int STATE_INITIALIZED = 1;
    private static final String TAG = "AudioTrack";
    @SuppressLint({"InlinedApi"})
    private static final int WRITE_NON_BLOCKING = 1;
    public static boolean enablePreV21AudioSessionWorkaround = false;
    public static boolean failOnSpuriousAudioTimestamp = false;
    private AudioAttributes audioAttributes;
    private final AudioCapabilities audioCapabilities;
    private AudioProcessor[] audioProcessors;
    private int audioSessionId;
    private boolean audioTimestampSet;
    private AudioTrack audioTrack;
    private final AudioTrackUtil audioTrackUtil;
    private ByteBuffer avSyncHeader;
    private int bufferSize;
    private long bufferSizeUs;
    private int bytesUntilNextAvSync;
    private boolean canApplyPlaybackParameters;
    private int channelConfig;
    private final ChannelMappingAudioProcessor channelMappingAudioProcessor;
    private int drainingAudioProcessorIndex;
    private PlaybackParameters drainingPlaybackParameters;
    private final boolean enableConvertHighResIntPcmToFloat;
    private int framesPerEncodedSample;
    private Method getLatencyMethod;
    private boolean handledEndOfStream;
    private boolean hasData;
    private ByteBuffer inputBuffer;
    private int inputSampleRate;
    private boolean isInputPcm;
    private AudioTrack keepSessionIdAudioTrack;
    private long lastFeedElapsedRealtimeMs;
    private long lastPlayheadSampleTimeUs;
    private long lastTimestampSampleTimeUs;
    private long latencyUs;
    private Listener listener;
    private int nextPlayheadOffsetIndex;
    private ByteBuffer outputBuffer;
    private ByteBuffer[] outputBuffers;
    private int outputEncoding;
    private int outputPcmFrameSize;
    private int pcmFrameSize;
    private PlaybackParameters playbackParameters;
    private final ArrayDeque<PlaybackParametersCheckpoint> playbackParametersCheckpoints;
    private long playbackParametersOffsetUs;
    private long playbackParametersPositionUs;
    private int playheadOffsetCount;
    private final long[] playheadOffsets;
    private boolean playing;
    private byte[] preV21OutputBuffer;
    private int preV21OutputBufferOffset;
    private boolean processingEnabled;
    private final ConditionVariable releasingConditionVariable;
    private long resumeSystemTimeUs;
    private int sampleRate;
    private boolean shouldConvertHighResIntPcmToFloat;
    private long smoothedPlayheadOffsetUs;
    private final SonicAudioProcessor sonicAudioProcessor;
    private int startMediaTimeState;
    private long startMediaTimeUs;
    private long submittedEncodedFrames;
    private long submittedPcmBytes;
    private final AudioProcessor[] toFloatPcmAvailableAudioProcessors;
    private final AudioProcessor[] toIntPcmAvailableAudioProcessors;
    private final TrimmingAudioProcessor trimmingAudioProcessor;
    private boolean tunneling;
    private float volume;
    private long writtenEncodedFrames;
    private long writtenPcmBytes;

    private static class AudioTrackUtil {
        private static final long FORCE_RESET_WORKAROUND_TIMEOUT_MS = 200;
        protected AudioTrack audioTrack;
        private long endPlaybackHeadPosition;
        private long forceResetWorkaroundTimeMs;
        private long lastRawPlaybackHeadPosition;
        private boolean needsPassthroughWorkaround;
        private long passthroughWorkaroundPauseOffset;
        private long rawPlaybackHeadWrapCount;
        private int sampleRate;
        private long stopPlaybackHeadPosition;
        private long stopTimestampUs;

        private AudioTrackUtil() {
        }

        public void reconfigure(AudioTrack audioTrack, boolean needsPassthroughWorkaround) {
            this.audioTrack = audioTrack;
            this.needsPassthroughWorkaround = needsPassthroughWorkaround;
            this.stopTimestampUs = C0600C.TIME_UNSET;
            this.forceResetWorkaroundTimeMs = C0600C.TIME_UNSET;
            this.lastRawPlaybackHeadPosition = 0;
            this.rawPlaybackHeadWrapCount = 0;
            this.passthroughWorkaroundPauseOffset = 0;
            if (audioTrack != null) {
                this.sampleRate = audioTrack.getSampleRate();
            }
        }

        public void handleEndOfStream(long writtenFrames) {
            this.stopPlaybackHeadPosition = getPlaybackHeadPosition();
            this.stopTimestampUs = SystemClock.elapsedRealtime() * 1000;
            this.endPlaybackHeadPosition = writtenFrames;
            this.audioTrack.stop();
        }

        public void pause() {
            if (this.stopTimestampUs == C0600C.TIME_UNSET) {
                this.audioTrack.pause();
            }
        }

        public boolean needsReset(long writtenFrames) {
            return this.forceResetWorkaroundTimeMs != C0600C.TIME_UNSET && writtenFrames > 0 && SystemClock.elapsedRealtime() - this.forceResetWorkaroundTimeMs >= FORCE_RESET_WORKAROUND_TIMEOUT_MS;
        }

        public long getPlaybackHeadPosition() {
            if (this.stopTimestampUs != C0600C.TIME_UNSET) {
                return Math.min(this.endPlaybackHeadPosition, this.stopPlaybackHeadPosition + ((((long) this.sampleRate) * ((SystemClock.elapsedRealtime() * 1000) - this.stopTimestampUs)) / C0600C.MICROS_PER_SECOND));
            }
            int state = this.audioTrack.getPlayState();
            if (state == 1) {
                return 0;
            }
            long rawPlaybackHeadPosition = 4294967295L & ((long) this.audioTrack.getPlaybackHeadPosition());
            if (this.needsPassthroughWorkaround) {
                if (state == 2 && rawPlaybackHeadPosition == 0) {
                    this.passthroughWorkaroundPauseOffset = this.lastRawPlaybackHeadPosition;
                }
                rawPlaybackHeadPosition += this.passthroughWorkaroundPauseOffset;
            }
            if (Util.SDK_INT <= 26) {
                if (rawPlaybackHeadPosition == 0 && this.lastRawPlaybackHeadPosition > 0 && state == 3) {
                    if (this.forceResetWorkaroundTimeMs == C0600C.TIME_UNSET) {
                        this.forceResetWorkaroundTimeMs = SystemClock.elapsedRealtime();
                    }
                    return this.lastRawPlaybackHeadPosition;
                }
                this.forceResetWorkaroundTimeMs = C0600C.TIME_UNSET;
            }
            if (this.lastRawPlaybackHeadPosition > rawPlaybackHeadPosition) {
                this.rawPlaybackHeadWrapCount++;
            }
            this.lastRawPlaybackHeadPosition = rawPlaybackHeadPosition;
            return (this.rawPlaybackHeadWrapCount << 32) + rawPlaybackHeadPosition;
        }

        public long getPositionUs() {
            return (getPlaybackHeadPosition() * C0600C.MICROS_PER_SECOND) / ((long) this.sampleRate);
        }

        public boolean updateTimestamp() {
            return false;
        }

        public long getTimestampNanoTime() {
            throw new UnsupportedOperationException();
        }

        public long getTimestampFramePosition() {
            throw new UnsupportedOperationException();
        }
    }

    @TargetApi(19)
    private static class AudioTrackUtilV19 extends AudioTrackUtil {
        private final AudioTimestamp audioTimestamp = new AudioTimestamp();
        private long lastRawTimestampFramePosition;
        private long lastTimestampFramePosition;
        private long rawTimestampFramePositionWrapCount;

        public AudioTrackUtilV19() {
            super();
        }

        public void reconfigure(AudioTrack audioTrack, boolean needsPassthroughWorkaround) {
            super.reconfigure(audioTrack, needsPassthroughWorkaround);
            this.rawTimestampFramePositionWrapCount = 0;
            this.lastRawTimestampFramePosition = 0;
            this.lastTimestampFramePosition = 0;
        }

        public boolean updateTimestamp() {
            boolean updated = this.audioTrack.getTimestamp(this.audioTimestamp);
            if (updated) {
                long rawFramePosition = this.audioTimestamp.framePosition;
                if (this.lastRawTimestampFramePosition > rawFramePosition) {
                    this.rawTimestampFramePositionWrapCount++;
                }
                this.lastRawTimestampFramePosition = rawFramePosition;
                this.lastTimestampFramePosition = (this.rawTimestampFramePositionWrapCount << 32) + rawFramePosition;
            }
            return updated;
        }

        public long getTimestampNanoTime() {
            return this.audioTimestamp.nanoTime;
        }

        public long getTimestampFramePosition() {
            return this.lastTimestampFramePosition;
        }
    }

    public static final class InvalidAudioTrackTimestampException extends RuntimeException {
        public InvalidAudioTrackTimestampException(String message) {
            super(message);
        }
    }

    private static final class PlaybackParametersCheckpoint {
        private final long mediaTimeUs;
        private final PlaybackParameters playbackParameters;
        private final long positionUs;

        private PlaybackParametersCheckpoint(PlaybackParameters playbackParameters, long mediaTimeUs, long positionUs) {
            this.playbackParameters = playbackParameters;
            this.mediaTimeUs = mediaTimeUs;
            this.positionUs = positionUs;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface StartMediaTimeState {
    }

    public DefaultAudioSink(AudioCapabilities audioCapabilities, AudioProcessor[] audioProcessors) {
        this(audioCapabilities, audioProcessors, false);
    }

    public DefaultAudioSink(AudioCapabilities audioCapabilities, AudioProcessor[] audioProcessors, boolean enableConvertHighResIntPcmToFloat) {
        this.audioCapabilities = audioCapabilities;
        this.enableConvertHighResIntPcmToFloat = enableConvertHighResIntPcmToFloat;
        this.releasingConditionVariable = new ConditionVariable(true);
        if (Util.SDK_INT >= 18) {
            try {
                this.getLatencyMethod = AudioTrack.class.getMethod("getLatency", (Class[]) null);
            } catch (NoSuchMethodException e) {
            }
        }
        if (Util.SDK_INT >= 19) {
            this.audioTrackUtil = new AudioTrackUtilV19();
        } else {
            this.audioTrackUtil = new AudioTrackUtil();
        }
        this.channelMappingAudioProcessor = new ChannelMappingAudioProcessor();
        this.trimmingAudioProcessor = new TrimmingAudioProcessor();
        this.sonicAudioProcessor = new SonicAudioProcessor();
        this.toIntPcmAvailableAudioProcessors = new AudioProcessor[(audioProcessors.length + 4)];
        this.toIntPcmAvailableAudioProcessors[0] = new ResamplingAudioProcessor();
        this.toIntPcmAvailableAudioProcessors[1] = this.channelMappingAudioProcessor;
        this.toIntPcmAvailableAudioProcessors[2] = this.trimmingAudioProcessor;
        System.arraycopy(audioProcessors, 0, this.toIntPcmAvailableAudioProcessors, 3, audioProcessors.length);
        this.toIntPcmAvailableAudioProcessors[audioProcessors.length + 3] = this.sonicAudioProcessor;
        this.toFloatPcmAvailableAudioProcessors = new AudioProcessor[]{new FloatResamplingAudioProcessor()};
        this.playheadOffsets = new long[10];
        this.volume = 1.0f;
        this.startMediaTimeState = 0;
        this.audioAttributes = AudioAttributes.DEFAULT;
        this.audioSessionId = 0;
        this.playbackParameters = PlaybackParameters.DEFAULT;
        this.drainingAudioProcessorIndex = -1;
        this.audioProcessors = new AudioProcessor[0];
        this.outputBuffers = new ByteBuffer[0];
        this.playbackParametersCheckpoints = new ArrayDeque();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public boolean isEncodingSupported(int encoding) {
        boolean z = true;
        if (!isEncodingPcm(encoding)) {
            if (this.audioCapabilities == null || !this.audioCapabilities.supportsEncoding(encoding)) {
                z = false;
            }
            return z;
        } else if (encoding != 4 || Util.SDK_INT >= 21) {
            return true;
        } else {
            return false;
        }
    }

    public long getCurrentPositionUs(boolean sourceEnded) {
        if (!hasCurrentPositionUs()) {
            return Long.MIN_VALUE;
        }
        long positionUs;
        if (this.audioTrack.getPlayState() == 3) {
            maybeSampleSyncParams();
        }
        long systemClockUs = System.nanoTime() / 1000;
        if (this.audioTimestampSet) {
            positionUs = framesToDurationUs(this.audioTrackUtil.getTimestampFramePosition() + durationUsToFrames(systemClockUs - (this.audioTrackUtil.getTimestampNanoTime() / 1000)));
        } else {
            if (this.playheadOffsetCount == 0) {
                positionUs = this.audioTrackUtil.getPositionUs();
            } else {
                positionUs = systemClockUs + this.smoothedPlayheadOffsetUs;
            }
            if (!sourceEnded) {
                positionUs -= this.latencyUs;
            }
        }
        return this.startMediaTimeUs + applySpeedup(Math.min(positionUs, framesToDurationUs(getWrittenFrames())));
    }

    public void configure(int inputEncoding, int inputChannelCount, int inputSampleRate, int specifiedBufferSize, int[] outputChannels, int trimStartSamples, int trimEndSamples) throws ConfigurationException {
        int channelConfig;
        boolean flush = false;
        this.inputSampleRate = inputSampleRate;
        int channelCount = inputChannelCount;
        int sampleRate = inputSampleRate;
        this.isInputPcm = isEncodingPcm(inputEncoding);
        boolean z = this.enableConvertHighResIntPcmToFloat && isEncodingSupported(1073741824) && Util.isEncodingHighResolutionIntegerPcm(inputEncoding);
        this.shouldConvertHighResIntPcmToFloat = z;
        if (this.isInputPcm) {
            this.pcmFrameSize = Util.getPcmFrameSize(inputEncoding, channelCount);
        }
        int encoding = inputEncoding;
        boolean processingEnabled = this.isInputPcm && inputEncoding != 4;
        z = processingEnabled && !this.shouldConvertHighResIntPcmToFloat;
        this.canApplyPlaybackParameters = z;
        if (processingEnabled) {
            this.trimmingAudioProcessor.setTrimSampleCount(trimStartSamples, trimEndSamples);
            this.channelMappingAudioProcessor.setChannelMap(outputChannels);
            AudioProcessor[] availableAudioProcessors = getAvailableAudioProcessors();
            int length = availableAudioProcessors.length;
            int i = 0;
            while (i < length) {
                AudioProcessor audioProcessor = availableAudioProcessors[i];
                try {
                    flush |= audioProcessor.configure(sampleRate, channelCount, encoding);
                    if (audioProcessor.isActive()) {
                        channelCount = audioProcessor.getOutputChannelCount();
                        sampleRate = audioProcessor.getOutputSampleRateHz();
                        encoding = audioProcessor.getOutputEncoding();
                    }
                    i++;
                } catch (Throwable e) {
                    throw new ConfigurationException(e);
                }
            }
        }
        switch (channelCount) {
            case 1:
                channelConfig = 4;
                break;
            case 2:
                channelConfig = 12;
                break;
            case 3:
                channelConfig = 28;
                break;
            case 4:
                channelConfig = 204;
                break;
            case 5:
                channelConfig = 220;
                break;
            case 6:
                channelConfig = 252;
                break;
            case 7:
                channelConfig = 1276;
                break;
            case 8:
                channelConfig = C0600C.CHANNEL_OUT_7POINT1_SURROUND;
                break;
            default:
                throw new ConfigurationException("Unsupported channel count: " + channelCount);
        }
        if (Util.SDK_INT <= 23 && "foster".equals(Util.DEVICE) && "NVIDIA".equals(Util.MANUFACTURER)) {
            switch (channelCount) {
                case 3:
                case 5:
                    channelConfig = 252;
                    break;
                case 7:
                    channelConfig = C0600C.CHANNEL_OUT_7POINT1_SURROUND;
                    break;
            }
        }
        if (Util.SDK_INT <= 25 && "fugu".equals(Util.DEVICE) && !this.isInputPcm && channelCount == 1) {
            channelConfig = 12;
        }
        if (flush || !isInitialized() || this.outputEncoding != encoding || this.sampleRate != sampleRate || this.channelConfig != channelConfig) {
            long framesToDurationUs;
            reset();
            this.processingEnabled = processingEnabled;
            this.sampleRate = sampleRate;
            this.channelConfig = channelConfig;
            this.outputEncoding = encoding;
            if (this.isInputPcm) {
                this.outputPcmFrameSize = Util.getPcmFrameSize(this.outputEncoding, channelCount);
            }
            if (specifiedBufferSize != 0) {
                this.bufferSize = specifiedBufferSize;
            } else if (this.isInputPcm) {
                int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, this.outputEncoding);
                Assertions.checkState(minBufferSize != -2);
                this.bufferSize = Util.constrainValue(minBufferSize * 4, ((int) durationUsToFrames(250000)) * this.outputPcmFrameSize, (int) Math.max((long) minBufferSize, durationUsToFrames(MAX_BUFFER_DURATION_US) * ((long) this.outputPcmFrameSize)));
            } else if (this.outputEncoding == 5 || this.outputEncoding == 6) {
                this.bufferSize = CacheDataSink.DEFAULT_BUFFER_SIZE;
            } else if (this.outputEncoding == 7) {
                this.bufferSize = 49152;
            } else {
                this.bufferSize = 294912;
            }
            if (this.isInputPcm) {
                framesToDurationUs = framesToDurationUs((long) (this.bufferSize / this.outputPcmFrameSize));
            } else {
                framesToDurationUs = C0600C.TIME_UNSET;
            }
            this.bufferSizeUs = framesToDurationUs;
        }
    }

    private void resetAudioProcessors() {
        ArrayList<AudioProcessor> newAudioProcessors = new ArrayList();
        for (AudioProcessor audioProcessor : getAvailableAudioProcessors()) {
            AudioProcessor audioProcessor2;
            if (audioProcessor2.isActive()) {
                newAudioProcessors.add(audioProcessor2);
            } else {
                audioProcessor2.flush();
            }
        }
        int count = newAudioProcessors.size();
        this.audioProcessors = (AudioProcessor[]) newAudioProcessors.toArray(new AudioProcessor[count]);
        this.outputBuffers = new ByteBuffer[count];
        for (int i = 0; i < count; i++) {
            audioProcessor2 = this.audioProcessors[i];
            audioProcessor2.flush();
            this.outputBuffers[i] = audioProcessor2.getOutput();
        }
    }

    private void initialize() throws InitializationException {
        this.releasingConditionVariable.block();
        this.audioTrack = initializeAudioTrack();
        setPlaybackParameters(this.playbackParameters);
        resetAudioProcessors();
        int audioSessionId = this.audioTrack.getAudioSessionId();
        if (enablePreV21AudioSessionWorkaround && Util.SDK_INT < 21) {
            if (!(this.keepSessionIdAudioTrack == null || audioSessionId == this.keepSessionIdAudioTrack.getAudioSessionId())) {
                releaseKeepSessionIdAudioTrack();
            }
            if (this.keepSessionIdAudioTrack == null) {
                this.keepSessionIdAudioTrack = initializeKeepSessionIdAudioTrack(audioSessionId);
            }
        }
        if (this.audioSessionId != audioSessionId) {
            this.audioSessionId = audioSessionId;
            if (this.listener != null) {
                this.listener.onAudioSessionId(audioSessionId);
            }
        }
        this.audioTrackUtil.reconfigure(this.audioTrack, needsPassthroughWorkarounds());
        setVolumeInternal();
        this.hasData = false;
    }

    public void play() {
        this.playing = true;
        if (isInitialized()) {
            this.resumeSystemTimeUs = System.nanoTime() / 1000;
            this.audioTrack.play();
        }
    }

    public void handleDiscontinuity() {
        if (this.startMediaTimeState == 1) {
            this.startMediaTimeState = 2;
        }
    }

    public boolean handleBuffer(ByteBuffer buffer, long presentationTimeUs) throws InitializationException, WriteException {
        boolean z = this.inputBuffer == null || buffer == this.inputBuffer;
        Assertions.checkArgument(z);
        if (!isInitialized()) {
            initialize();
            if (this.playing) {
                play();
            }
        }
        if (needsPassthroughWorkarounds()) {
            if (this.audioTrack.getPlayState() == 2) {
                this.hasData = false;
                return false;
            } else if (this.audioTrack.getPlayState() == 1 && this.audioTrackUtil.getPlaybackHeadPosition() != 0) {
                return false;
            }
        }
        boolean hadData = this.hasData;
        this.hasData = hasPendingData();
        if (!(!hadData || this.hasData || this.audioTrack.getPlayState() == 1 || this.listener == null)) {
            this.listener.onUnderrun(this.bufferSize, C0600C.usToMs(this.bufferSizeUs), SystemClock.elapsedRealtime() - this.lastFeedElapsedRealtimeMs);
        }
        if (this.inputBuffer == null) {
            if (!buffer.hasRemaining()) {
                return true;
            }
            if (!this.isInputPcm && this.framesPerEncodedSample == 0) {
                this.framesPerEncodedSample = getFramesPerEncodedSample(this.outputEncoding, buffer);
                if (this.framesPerEncodedSample == 0) {
                    return true;
                }
            }
            if (this.drainingPlaybackParameters != null) {
                if (!drainAudioProcessorsToEndOfStream()) {
                    return false;
                }
                this.playbackParametersCheckpoints.add(new PlaybackParametersCheckpoint(this.drainingPlaybackParameters, Math.max(0, presentationTimeUs), framesToDurationUs(getWrittenFrames())));
                this.drainingPlaybackParameters = null;
                resetAudioProcessors();
            }
            if (this.startMediaTimeState == 0) {
                this.startMediaTimeUs = Math.max(0, presentationTimeUs);
                this.startMediaTimeState = 1;
            } else {
                long expectedPresentationTimeUs = this.startMediaTimeUs + inputFramesToDurationUs(getSubmittedFrames());
                if (this.startMediaTimeState == 1 && Math.abs(expectedPresentationTimeUs - presentationTimeUs) > 200000) {
                    Log.e(TAG, "Discontinuity detected [expected " + expectedPresentationTimeUs + ", got " + presentationTimeUs + "]");
                    this.startMediaTimeState = 2;
                }
                if (this.startMediaTimeState == 2) {
                    this.startMediaTimeUs += presentationTimeUs - expectedPresentationTimeUs;
                    this.startMediaTimeState = 1;
                    if (this.listener != null) {
                        this.listener.onPositionDiscontinuity();
                    }
                }
            }
            if (this.isInputPcm) {
                this.submittedPcmBytes += (long) buffer.remaining();
            } else {
                this.submittedEncodedFrames += (long) this.framesPerEncodedSample;
            }
            this.inputBuffer = buffer;
        }
        if (this.processingEnabled) {
            processBuffers(presentationTimeUs);
        } else {
            writeBuffer(this.inputBuffer, presentationTimeUs);
        }
        if (!this.inputBuffer.hasRemaining()) {
            this.inputBuffer = null;
            return true;
        } else if (!this.audioTrackUtil.needsReset(getWrittenFrames())) {
            return false;
        } else {
            Log.w(TAG, "Resetting stalled audio track");
            reset();
            return true;
        }
    }

    private void processBuffers(long avSyncPresentationTimeUs) throws WriteException {
        int count = this.audioProcessors.length;
        int index = count;
        while (index >= 0) {
            ByteBuffer input = index > 0 ? this.outputBuffers[index - 1] : this.inputBuffer != null ? this.inputBuffer : AudioProcessor.EMPTY_BUFFER;
            if (index == count) {
                writeBuffer(input, avSyncPresentationTimeUs);
            } else {
                AudioProcessor audioProcessor = this.audioProcessors[index];
                audioProcessor.queueInput(input);
                ByteBuffer output = audioProcessor.getOutput();
                this.outputBuffers[index] = output;
                if (output.hasRemaining()) {
                    index++;
                }
            }
            if (!input.hasRemaining()) {
                index--;
            } else {
                return;
            }
        }
    }

    private void writeBuffer(ByteBuffer buffer, long avSyncPresentationTimeUs) throws WriteException {
        boolean z = true;
        if (buffer.hasRemaining()) {
            int bytesRemaining;
            if (this.outputBuffer != null) {
                boolean z2;
                if (this.outputBuffer == buffer) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                Assertions.checkArgument(z2);
            } else {
                this.outputBuffer = buffer;
                if (Util.SDK_INT < 21) {
                    bytesRemaining = buffer.remaining();
                    if (this.preV21OutputBuffer == null || this.preV21OutputBuffer.length < bytesRemaining) {
                        this.preV21OutputBuffer = new byte[bytesRemaining];
                    }
                    int originalPosition = buffer.position();
                    buffer.get(this.preV21OutputBuffer, 0, bytesRemaining);
                    buffer.position(originalPosition);
                    this.preV21OutputBufferOffset = 0;
                }
            }
            bytesRemaining = buffer.remaining();
            int bytesWritten = 0;
            if (Util.SDK_INT < 21) {
                int bytesToWrite = this.bufferSize - ((int) (this.writtenPcmBytes - (this.audioTrackUtil.getPlaybackHeadPosition() * ((long) this.outputPcmFrameSize))));
                if (bytesToWrite > 0) {
                    bytesWritten = this.audioTrack.write(this.preV21OutputBuffer, this.preV21OutputBufferOffset, Math.min(bytesRemaining, bytesToWrite));
                    if (bytesWritten > 0) {
                        this.preV21OutputBufferOffset += bytesWritten;
                        buffer.position(buffer.position() + bytesWritten);
                    }
                }
            } else if (this.tunneling) {
                if (avSyncPresentationTimeUs == C0600C.TIME_UNSET) {
                    z = false;
                }
                Assertions.checkState(z);
                bytesWritten = writeNonBlockingWithAvSyncV21(this.audioTrack, buffer, bytesRemaining, avSyncPresentationTimeUs);
            } else {
                bytesWritten = writeNonBlockingV21(this.audioTrack, buffer, bytesRemaining);
            }
            this.lastFeedElapsedRealtimeMs = SystemClock.elapsedRealtime();
            if (bytesWritten < 0) {
                throw new WriteException(bytesWritten);
            }
            if (this.isInputPcm) {
                this.writtenPcmBytes += (long) bytesWritten;
            }
            if (bytesWritten == bytesRemaining) {
                if (!this.isInputPcm) {
                    this.writtenEncodedFrames += (long) this.framesPerEncodedSample;
                }
                this.outputBuffer = null;
            }
        }
    }

    public void playToEndOfStream() throws WriteException {
        if (!this.handledEndOfStream && isInitialized() && drainAudioProcessorsToEndOfStream()) {
            this.audioTrackUtil.handleEndOfStream(getWrittenFrames());
            this.bytesUntilNextAvSync = 0;
            this.handledEndOfStream = true;
        }
    }

    private boolean drainAudioProcessorsToEndOfStream() throws WriteException {
        boolean audioProcessorNeedsEndOfStream = false;
        if (this.drainingAudioProcessorIndex == -1) {
            int i;
            if (this.processingEnabled) {
                i = 0;
            } else {
                i = this.audioProcessors.length;
            }
            this.drainingAudioProcessorIndex = i;
            audioProcessorNeedsEndOfStream = true;
        }
        while (this.drainingAudioProcessorIndex < this.audioProcessors.length) {
            AudioProcessor audioProcessor = this.audioProcessors[this.drainingAudioProcessorIndex];
            if (audioProcessorNeedsEndOfStream) {
                audioProcessor.queueEndOfStream();
            }
            processBuffers(C0600C.TIME_UNSET);
            if (!audioProcessor.isEnded()) {
                return false;
            }
            audioProcessorNeedsEndOfStream = true;
            this.drainingAudioProcessorIndex++;
        }
        if (this.outputBuffer != null) {
            writeBuffer(this.outputBuffer, C0600C.TIME_UNSET);
            if (this.outputBuffer != null) {
                return false;
            }
        }
        this.drainingAudioProcessorIndex = -1;
        return true;
    }

    public boolean isEnded() {
        return !isInitialized() || (this.handledEndOfStream && !hasPendingData());
    }

    public boolean hasPendingData() {
        return isInitialized() && (getWrittenFrames() > this.audioTrackUtil.getPlaybackHeadPosition() || overrideHasPendingData());
    }

    public PlaybackParameters setPlaybackParameters(PlaybackParameters playbackParameters) {
        if (!isInitialized() || this.canApplyPlaybackParameters) {
            PlaybackParameters playbackParameters2 = new PlaybackParameters(this.sonicAudioProcessor.setSpeed(playbackParameters.speed), this.sonicAudioProcessor.setPitch(playbackParameters.pitch));
            PlaybackParameters lastSetPlaybackParameters = this.drainingPlaybackParameters != null ? this.drainingPlaybackParameters : !this.playbackParametersCheckpoints.isEmpty() ? ((PlaybackParametersCheckpoint) this.playbackParametersCheckpoints.getLast()).playbackParameters : this.playbackParameters;
            if (!playbackParameters2.equals(lastSetPlaybackParameters)) {
                if (isInitialized()) {
                    this.drainingPlaybackParameters = playbackParameters2;
                } else {
                    this.playbackParameters = playbackParameters2;
                }
            }
            playbackParameters = playbackParameters2;
            return this.playbackParameters;
        }
        this.playbackParameters = PlaybackParameters.DEFAULT;
        return this.playbackParameters;
    }

    public PlaybackParameters getPlaybackParameters() {
        return this.playbackParameters;
    }

    public void setAudioAttributes(AudioAttributes audioAttributes) {
        if (!this.audioAttributes.equals(audioAttributes)) {
            this.audioAttributes = audioAttributes;
            if (!this.tunneling) {
                reset();
                this.audioSessionId = 0;
            }
        }
    }

    public void setAudioSessionId(int audioSessionId) {
        if (this.audioSessionId != audioSessionId) {
            this.audioSessionId = audioSessionId;
            reset();
        }
    }

    public void enableTunnelingV21(int tunnelingAudioSessionId) {
        Assertions.checkState(Util.SDK_INT >= 21);
        if (!this.tunneling || this.audioSessionId != tunnelingAudioSessionId) {
            this.tunneling = true;
            this.audioSessionId = tunnelingAudioSessionId;
            reset();
        }
    }

    public void disableTunneling() {
        if (this.tunneling) {
            this.tunneling = false;
            this.audioSessionId = 0;
            reset();
        }
    }

    public void setVolume(float volume) {
        if (this.volume != volume) {
            this.volume = volume;
            setVolumeInternal();
        }
    }

    private void setVolumeInternal() {
        if (!isInitialized()) {
            return;
        }
        if (Util.SDK_INT >= 21) {
            setVolumeInternalV21(this.audioTrack, this.volume);
        } else {
            setVolumeInternalV3(this.audioTrack, this.volume);
        }
    }

    public void pause() {
        this.playing = false;
        if (isInitialized()) {
            resetSyncParams();
            this.audioTrackUtil.pause();
        }
    }

    public void reset() {
        if (isInitialized()) {
            this.submittedPcmBytes = 0;
            this.submittedEncodedFrames = 0;
            this.writtenPcmBytes = 0;
            this.writtenEncodedFrames = 0;
            this.framesPerEncodedSample = 0;
            if (this.drainingPlaybackParameters != null) {
                this.playbackParameters = this.drainingPlaybackParameters;
                this.drainingPlaybackParameters = null;
            } else if (!this.playbackParametersCheckpoints.isEmpty()) {
                this.playbackParameters = ((PlaybackParametersCheckpoint) this.playbackParametersCheckpoints.getLast()).playbackParameters;
            }
            this.playbackParametersCheckpoints.clear();
            this.playbackParametersOffsetUs = 0;
            this.playbackParametersPositionUs = 0;
            this.inputBuffer = null;
            this.outputBuffer = null;
            for (int i = 0; i < this.audioProcessors.length; i++) {
                AudioProcessor audioProcessor = this.audioProcessors[i];
                audioProcessor.flush();
                this.outputBuffers[i] = audioProcessor.getOutput();
            }
            this.handledEndOfStream = false;
            this.drainingAudioProcessorIndex = -1;
            this.avSyncHeader = null;
            this.bytesUntilNextAvSync = 0;
            this.startMediaTimeState = 0;
            this.latencyUs = 0;
            resetSyncParams();
            if (this.audioTrack.getPlayState() == 3) {
                this.audioTrack.pause();
            }
            final AudioTrack toRelease = this.audioTrack;
            this.audioTrack = null;
            this.audioTrackUtil.reconfigure(null, false);
            this.releasingConditionVariable.close();
            new Thread() {
                public void run() {
                    try {
                        toRelease.flush();
                        toRelease.release();
                    } finally {
                        DefaultAudioSink.this.releasingConditionVariable.open();
                    }
                }
            }.start();
        }
    }

    public void release() {
        reset();
        releaseKeepSessionIdAudioTrack();
        for (AudioProcessor audioProcessor : this.toIntPcmAvailableAudioProcessors) {
            audioProcessor.reset();
        }
        for (AudioProcessor audioProcessor2 : this.toFloatPcmAvailableAudioProcessors) {
            audioProcessor2.reset();
        }
        this.audioSessionId = 0;
        this.playing = false;
    }

    private void releaseKeepSessionIdAudioTrack() {
        if (this.keepSessionIdAudioTrack != null) {
            final AudioTrack toRelease = this.keepSessionIdAudioTrack;
            this.keepSessionIdAudioTrack = null;
            new Thread() {
                public void run() {
                    toRelease.release();
                }
            }.start();
        }
    }

    private boolean hasCurrentPositionUs() {
        return isInitialized() && this.startMediaTimeState != 0;
    }

    private long applySpeedup(long positionUs) {
        while (!this.playbackParametersCheckpoints.isEmpty() && positionUs >= ((PlaybackParametersCheckpoint) this.playbackParametersCheckpoints.getFirst()).positionUs) {
            PlaybackParametersCheckpoint checkpoint = (PlaybackParametersCheckpoint) this.playbackParametersCheckpoints.remove();
            this.playbackParameters = checkpoint.playbackParameters;
            this.playbackParametersPositionUs = checkpoint.positionUs;
            this.playbackParametersOffsetUs = checkpoint.mediaTimeUs - this.startMediaTimeUs;
        }
        if (this.playbackParameters.speed == 1.0f) {
            return (this.playbackParametersOffsetUs + positionUs) - this.playbackParametersPositionUs;
        }
        if (this.playbackParametersCheckpoints.isEmpty()) {
            return this.playbackParametersOffsetUs + this.sonicAudioProcessor.scaleDurationForSpeedup(positionUs - this.playbackParametersPositionUs);
        }
        return this.playbackParametersOffsetUs + Util.getMediaDurationForPlayoutDuration(positionUs - this.playbackParametersPositionUs, this.playbackParameters.speed);
    }

    private void maybeSampleSyncParams() {
        long playbackPositionUs = this.audioTrackUtil.getPositionUs();
        if (playbackPositionUs != 0) {
            long systemClockUs = System.nanoTime() / 1000;
            if (systemClockUs - this.lastPlayheadSampleTimeUs >= 30000) {
                this.playheadOffsets[this.nextPlayheadOffsetIndex] = playbackPositionUs - systemClockUs;
                this.nextPlayheadOffsetIndex = (this.nextPlayheadOffsetIndex + 1) % 10;
                if (this.playheadOffsetCount < 10) {
                    this.playheadOffsetCount++;
                }
                this.lastPlayheadSampleTimeUs = systemClockUs;
                this.smoothedPlayheadOffsetUs = 0;
                for (int i = 0; i < this.playheadOffsetCount; i++) {
                    this.smoothedPlayheadOffsetUs += this.playheadOffsets[i] / ((long) this.playheadOffsetCount);
                }
            }
            if (!needsPassthroughWorkarounds() && systemClockUs - this.lastTimestampSampleTimeUs >= 500000) {
                this.audioTimestampSet = this.audioTrackUtil.updateTimestamp();
                if (this.audioTimestampSet) {
                    long audioTimestampUs = this.audioTrackUtil.getTimestampNanoTime() / 1000;
                    long audioTimestampFramePosition = this.audioTrackUtil.getTimestampFramePosition();
                    if (audioTimestampUs < this.resumeSystemTimeUs) {
                        this.audioTimestampSet = false;
                    } else if (Math.abs(audioTimestampUs - systemClockUs) > 5000000) {
                        message = "Spurious audio timestamp (system clock mismatch): " + audioTimestampFramePosition + ", " + audioTimestampUs + ", " + systemClockUs + ", " + playbackPositionUs + ", " + getSubmittedFrames() + ", " + getWrittenFrames();
                        if (failOnSpuriousAudioTimestamp) {
                            throw new InvalidAudioTrackTimestampException(message);
                        }
                        Log.w(TAG, message);
                        this.audioTimestampSet = false;
                    } else if (Math.abs(framesToDurationUs(audioTimestampFramePosition) - playbackPositionUs) > 5000000) {
                        message = "Spurious audio timestamp (frame position mismatch): " + audioTimestampFramePosition + ", " + audioTimestampUs + ", " + systemClockUs + ", " + playbackPositionUs + ", " + getSubmittedFrames() + ", " + getWrittenFrames();
                        if (failOnSpuriousAudioTimestamp) {
                            throw new InvalidAudioTrackTimestampException(message);
                        }
                        Log.w(TAG, message);
                        this.audioTimestampSet = false;
                    }
                }
                if (this.getLatencyMethod != null && this.isInputPcm) {
                    try {
                        this.latencyUs = (((long) ((Integer) this.getLatencyMethod.invoke(this.audioTrack, (Object[]) null)).intValue()) * 1000) - this.bufferSizeUs;
                        this.latencyUs = Math.max(this.latencyUs, 0);
                        if (this.latencyUs > 5000000) {
                            Log.w(TAG, "Ignoring impossibly large audio latency: " + this.latencyUs);
                            this.latencyUs = 0;
                        }
                    } catch (Exception e) {
                        this.getLatencyMethod = null;
                    }
                }
                this.lastTimestampSampleTimeUs = systemClockUs;
            }
        }
    }

    private boolean isInitialized() {
        return this.audioTrack != null;
    }

    private long inputFramesToDurationUs(long frameCount) {
        return (C0600C.MICROS_PER_SECOND * frameCount) / ((long) this.inputSampleRate);
    }

    private long framesToDurationUs(long frameCount) {
        return (C0600C.MICROS_PER_SECOND * frameCount) / ((long) this.sampleRate);
    }

    private long durationUsToFrames(long durationUs) {
        return (((long) this.sampleRate) * durationUs) / C0600C.MICROS_PER_SECOND;
    }

    private long getSubmittedFrames() {
        return this.isInputPcm ? this.submittedPcmBytes / ((long) this.pcmFrameSize) : this.submittedEncodedFrames;
    }

    private long getWrittenFrames() {
        return this.isInputPcm ? this.writtenPcmBytes / ((long) this.outputPcmFrameSize) : this.writtenEncodedFrames;
    }

    private void resetSyncParams() {
        this.smoothedPlayheadOffsetUs = 0;
        this.playheadOffsetCount = 0;
        this.nextPlayheadOffsetIndex = 0;
        this.lastPlayheadSampleTimeUs = 0;
        this.audioTimestampSet = false;
        this.lastTimestampSampleTimeUs = 0;
    }

    private boolean needsPassthroughWorkarounds() {
        return Util.SDK_INT < 23 && (this.outputEncoding == 5 || this.outputEncoding == 6);
    }

    private boolean overrideHasPendingData() {
        return needsPassthroughWorkarounds() && this.audioTrack.getPlayState() == 2 && this.audioTrack.getPlaybackHeadPosition() == 0;
    }

    private AudioTrack initializeAudioTrack() throws InitializationException {
        AudioTrack audioTrack;
        if (Util.SDK_INT >= 21) {
            audioTrack = createAudioTrackV21();
        } else {
            int streamType = Util.getStreamTypeForAudioUsage(this.audioAttributes.usage);
            if (this.audioSessionId == 0) {
                audioTrack = new AudioTrack(streamType, this.sampleRate, this.channelConfig, this.outputEncoding, this.bufferSize, 1);
            } else {
                audioTrack = new AudioTrack(streamType, this.sampleRate, this.channelConfig, this.outputEncoding, this.bufferSize, 1, this.audioSessionId);
            }
        }
        int state = audioTrack.getState();
        if (state == 1) {
            return audioTrack;
        }
        try {
            audioTrack.release();
        } catch (Exception e) {
        }
        throw new InitializationException(state, this.sampleRate, this.channelConfig, this.bufferSize);
    }

    @TargetApi(21)
    private AudioTrack createAudioTrackV21() {
        AudioAttributes attributes;
        if (this.tunneling) {
            attributes = new Builder().setContentType(3).setFlags(16).setUsage(1).build();
        } else {
            attributes = this.audioAttributes.getAudioAttributesV21();
        }
        return new AudioTrack(attributes, new AudioFormat.Builder().setChannelMask(this.channelConfig).setEncoding(this.outputEncoding).setSampleRate(this.sampleRate).build(), this.bufferSize, 1, this.audioSessionId != 0 ? this.audioSessionId : 0);
    }

    private AudioTrack initializeKeepSessionIdAudioTrack(int audioSessionId) {
        return new AudioTrack(3, 4000, 4, 2, 2, 0, audioSessionId);
    }

    private AudioProcessor[] getAvailableAudioProcessors() {
        return this.shouldConvertHighResIntPcmToFloat ? this.toFloatPcmAvailableAudioProcessors : this.toIntPcmAvailableAudioProcessors;
    }

    private static boolean isEncodingPcm(int encoding) {
        return encoding == 3 || encoding == 2 || encoding == Integer.MIN_VALUE || encoding == 1073741824 || encoding == 4;
    }

    private static int getFramesPerEncodedSample(int encoding, ByteBuffer buffer) {
        if (encoding == 7 || encoding == 8) {
            return DtsUtil.parseDtsAudioSampleCount(buffer);
        }
        if (encoding == 5) {
            return Ac3Util.getAc3SyncframeAudioSampleCount();
        }
        if (encoding == 6) {
            return Ac3Util.parseEAc3SyncframeAudioSampleCount(buffer);
        }
        if (encoding == 14) {
            return Ac3Util.parseTrueHdSyncframeAudioSampleCount(buffer) * 8;
        }
        throw new IllegalStateException("Unexpected audio encoding: " + encoding);
    }

    @TargetApi(21)
    private static int writeNonBlockingV21(AudioTrack audioTrack, ByteBuffer buffer, int size) {
        return audioTrack.write(buffer, size, 1);
    }

    @TargetApi(21)
    private int writeNonBlockingWithAvSyncV21(AudioTrack audioTrack, ByteBuffer buffer, int size, long presentationTimeUs) {
        int result;
        if (this.avSyncHeader == null) {
            this.avSyncHeader = ByteBuffer.allocate(16);
            this.avSyncHeader.order(ByteOrder.BIG_ENDIAN);
            this.avSyncHeader.putInt(1431633921);
        }
        if (this.bytesUntilNextAvSync == 0) {
            this.avSyncHeader.putInt(4, size);
            this.avSyncHeader.putLong(8, 1000 * presentationTimeUs);
            this.avSyncHeader.position(0);
            this.bytesUntilNextAvSync = size;
        }
        int avSyncHeaderBytesRemaining = this.avSyncHeader.remaining();
        if (avSyncHeaderBytesRemaining > 0) {
            result = audioTrack.write(this.avSyncHeader, avSyncHeaderBytesRemaining, 1);
            if (result < 0) {
                this.bytesUntilNextAvSync = 0;
                return result;
            } else if (result < avSyncHeaderBytesRemaining) {
                int i = result;
                return 0;
            }
        }
        result = writeNonBlockingV21(audioTrack, buffer, size);
        if (result < 0) {
            this.bytesUntilNextAvSync = 0;
            i = result;
            return result;
        }
        this.bytesUntilNextAvSync -= result;
        i = result;
        return result;
    }

    @TargetApi(21)
    private static void setVolumeInternalV21(AudioTrack audioTrack, float volume) {
        audioTrack.setVolume(volume);
    }

    private static void setVolumeInternalV3(AudioTrack audioTrack, float volume) {
        audioTrack.setStereoVolume(volume, volume);
    }
}
