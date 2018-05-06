package net.hockeyapp.android;

import android.content.Context;
import android.content.Intent;
import com.google.android.gms.plus.PlusShare;

public class FeedbackManager {
    private static String identifier = null;
    private static FeedbackManagerListener lastListener = null;
    private static String urlString = null;

    public static void register(Context context, String appIdentifier) {
        register(context, appIdentifier, null);
    }

    public static void register(Context context, String appIdentifier, FeedbackManagerListener listener) {
        register(context, Constants.BASE_URL, appIdentifier, listener);
    }

    public static void register(Context context, String urlString, String appIdentifier, FeedbackManagerListener listener) {
        if (context != null) {
            identifier = appIdentifier;
            urlString = urlString;
            lastListener = listener;
            Constants.loadFromContext(context);
        }
    }

    public static void unregister() {
        lastListener = null;
    }

    public static void showFeedbackActivity(Context context) {
        if (context != null) {
            Class<?> activityClass = null;
            if (lastListener != null) {
                activityClass = lastListener.getFeedbackActivityClass();
            }
            if (activityClass == null) {
                activityClass = FeedbackActivity.class;
            }
            Intent intent = new Intent();
            intent.setFlags(268435456);
            intent.setClass(context, activityClass);
            intent.putExtra(PlusShare.KEY_CALL_TO_ACTION_URL, getURLString(context));
            context.startActivity(intent);
        }
    }

    private static String getURLString(Context context) {
        return urlString + "api/2/apps/" + identifier + "/feedback/";
    }

    public static FeedbackManagerListener getLastListener() {
        return lastListener;
    }
}
