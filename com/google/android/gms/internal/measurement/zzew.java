package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.exoplayer2.C0600C;
import org.telegram.messenger.exoplayer2.DefaultRenderersFactory;
import org.telegram.messenger.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;

public final class zzew {
    static List<zzex<Integer>> zzaga = new ArrayList();
    static List<zzex<Long>> zzagb = new ArrayList();
    static List<zzex<Boolean>> zzagc = new ArrayList();
    static List<zzex<String>> zzagd = new ArrayList();
    static List<zzex<Double>> zzage = new ArrayList();
    private static zzex<Boolean> zzagf = zzex.zzb("measurement.log_third_party_store_events_enabled", false, false);
    private static zzex<Boolean> zzagg = zzex.zzb("measurement.log_installs_enabled", false, false);
    private static zzex<Boolean> zzagh = zzex.zzb("measurement.log_upgrades_enabled", false, false);
    private static zzex<Boolean> zzagi = zzex.zzb("measurement.log_androidId_enabled", false, false);
    public static zzex<Boolean> zzagj = zzex.zzb("measurement.upload_dsid_enabled", false, false);
    public static zzex<Boolean> zzagk = zzex.zzb("measurement.event_sampling_enabled", false, false);
    public static zzex<String> zzagl = zzex.zzd("measurement.log_tag", "FA", "FA-SVC");
    public static zzex<Long> zzagm = zzex.zzb("measurement.ad_id_cache_time", 10000, 10000);
    public static zzex<Long> zzagn = zzex.zzb("measurement.monitoring.sample_period_millis", 86400000, 86400000);
    public static zzex<Long> zzago = zzex.zzb("measurement.config.cache_time", 86400000, 3600000);
    public static zzex<String> zzagp;
    public static zzex<String> zzagq;
    public static zzex<Integer> zzagr = zzex.zzc("measurement.upload.max_bundles", 100, 100);
    public static zzex<Integer> zzags = zzex.zzc("measurement.upload.max_batch_size", C0600C.DEFAULT_BUFFER_SEGMENT_SIZE, C0600C.DEFAULT_BUFFER_SEGMENT_SIZE);
    public static zzex<Integer> zzagt = zzex.zzc("measurement.upload.max_bundle_size", C0600C.DEFAULT_BUFFER_SEGMENT_SIZE, C0600C.DEFAULT_BUFFER_SEGMENT_SIZE);
    public static zzex<Integer> zzagu = zzex.zzc("measurement.upload.max_events_per_bundle", 1000, 1000);
    public static zzex<Integer> zzagv = zzex.zzc("measurement.upload.max_events_per_day", DefaultOggSeeker.MATCH_BYTE_RANGE, DefaultOggSeeker.MATCH_BYTE_RANGE);
    public static zzex<Integer> zzagw = zzex.zzc("measurement.upload.max_error_events_per_day", 1000, 1000);
    public static zzex<Integer> zzagx = zzex.zzc("measurement.upload.max_public_events_per_day", 50000, 50000);
    public static zzex<Integer> zzagy = zzex.zzc("measurement.upload.max_conversions_per_day", 500, 500);
    public static zzex<Integer> zzagz = zzex.zzc("measurement.upload.max_realtime_events_per_day", 10, 10);
    public static zzex<Integer> zzaha = zzex.zzc("measurement.store.max_stored_events_per_app", DefaultOggSeeker.MATCH_BYTE_RANGE, DefaultOggSeeker.MATCH_BYTE_RANGE);
    public static zzex<String> zzahb;
    public static zzex<Long> zzahc = zzex.zzb("measurement.upload.backoff_period", 43200000, 43200000);
    public static zzex<Long> zzahd = zzex.zzb("measurement.upload.window_interval", 3600000, 3600000);
    public static zzex<Long> zzahe = zzex.zzb("measurement.upload.interval", 3600000, 3600000);
    public static zzex<Long> zzahf = zzex.zzb("measurement.upload.realtime_upload_interval", 10000, 10000);
    public static zzex<Long> zzahg = zzex.zzb("measurement.upload.debug_upload_interval", 1000, 1000);
    public static zzex<Long> zzahh = zzex.zzb("measurement.upload.minimum_delay", 500, 500);
    public static zzex<Long> zzahi = zzex.zzb("measurement.alarm_manager.minimum_interval", (long) ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS, (long) ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS);
    public static zzex<Long> zzahj = zzex.zzb("measurement.upload.stale_data_deletion_interval", 86400000, 86400000);
    public static zzex<Long> zzahk = zzex.zzb("measurement.upload.refresh_blacklisted_config_interval", 604800000, 604800000);
    public static zzex<Long> zzahl = zzex.zzb("measurement.upload.initial_upload_delay_time", 15000, 15000);
    public static zzex<Long> zzahm = zzex.zzb("measurement.upload.retry_time", 1800000, 1800000);
    public static zzex<Integer> zzahn = zzex.zzc("measurement.upload.retry_count", 6, 6);
    public static zzex<Long> zzaho = zzex.zzb("measurement.upload.max_queue_time", 2419200000L, 2419200000L);
    public static zzex<Integer> zzahp = zzex.zzc("measurement.lifetimevalue.max_currency_tracked", 4, 4);
    public static zzex<Integer> zzahq = zzex.zzc("measurement.audience.filter_result_max_count", Callback.DEFAULT_DRAG_ANIMATION_DURATION, Callback.DEFAULT_DRAG_ANIMATION_DURATION);
    public static zzex<Long> zzahr = zzex.zzb("measurement.service_client.idle_disconnect_millis", (long) DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS, (long) DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
    public static zzex<Boolean> zzahs = zzex.zzb("measurement.test.boolean_flag", false, false);
    public static zzex<String> zzaht;
    public static zzex<Long> zzahu = zzex.zzb("measurement.test.long_flag", -1, -1);
    public static zzex<Integer> zzahv = zzex.zzc("measurement.test.int_flag", -2, -2);
    public static zzex<Double> zzahw = zzex.zza("measurement.test.double_flag", -3.0d, -3.0d);
    public static zzex<Boolean> zzahx = zzex.zzb("measurement.lifetimevalue.user_engagement_tracking_enabled", false, false);
    public static zzex<Boolean> zzahy = zzex.zzb("measurement.audience.complex_param_evaluation", false, false);
    public static zzex<Boolean> zzahz = zzex.zzb("measurement.validation.internal_limits_internal_event_params", false, false);
    public static zzex<Boolean> zzaia = zzex.zzb("measurement.quality.unsuccessful_update_retry_counter", false, false);
    public static zzex<Boolean> zzaib = zzex.zzb("measurement.iid.disable_on_collection_disabled", true, true);
    public static zzex<Boolean> zzaic = zzex.zzb("measurement.app_launch.call_only_when_enabled", true, true);

    static {
        String str = "https";
        zzagp = zzex.zzd("measurement.config.url_scheme", str, str);
        str = "app-measurement.com";
        zzagq = zzex.zzd("measurement.config.url_authority", str, str);
        str = "https://app-measurement.com/a";
        zzahb = zzex.zzd("measurement.upload.url", str, str);
        str = "---";
        zzaht = zzex.zzd("measurement.test.string_flag", str, str);
    }
}
