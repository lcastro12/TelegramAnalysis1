package org.telegram.messenger.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import org.telegram.messenger.exoplayer2.C0600C;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;
import org.telegram.messenger.exoplayer2.util.TimestampAdjuster;

public final class TimeSignalCommand extends SpliceCommand {
    public static final Creator<TimeSignalCommand> CREATOR = new C06671();
    public final long playbackPositionUs;
    public final long ptsTime;

    static class C06671 implements Creator<TimeSignalCommand> {
        C06671() {
        }

        public TimeSignalCommand createFromParcel(Parcel in) {
            return new TimeSignalCommand(in.readLong(), in.readLong());
        }

        public TimeSignalCommand[] newArray(int size) {
            return new TimeSignalCommand[size];
        }
    }

    private TimeSignalCommand(long ptsTime, long playbackPositionUs) {
        this.ptsTime = ptsTime;
        this.playbackPositionUs = playbackPositionUs;
    }

    static TimeSignalCommand parseFromSection(ParsableByteArray sectionData, long ptsAdjustment, TimestampAdjuster timestampAdjuster) {
        long ptsTime = parseSpliceTime(sectionData, ptsAdjustment);
        return new TimeSignalCommand(ptsTime, timestampAdjuster.adjustTsTimestamp(ptsTime));
    }

    static long parseSpliceTime(ParsableByteArray sectionData, long ptsAdjustment) {
        long firstByte = (long) sectionData.readUnsignedByte();
        if ((128 & firstByte) != 0) {
            return ((((1 & firstByte) << 32) | sectionData.readUnsignedInt()) + ptsAdjustment) & 8589934591L;
        }
        return C0600C.TIME_UNSET;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.ptsTime);
        dest.writeLong(this.playbackPositionUs);
    }
}
