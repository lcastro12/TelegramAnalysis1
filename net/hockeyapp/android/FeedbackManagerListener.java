package net.hockeyapp.android;

public abstract class FeedbackManagerListener extends StringListener {
    public Class<? extends FeedbackActivity> getFeedbackActivityClass() {
        return FeedbackActivity.class;
    }
}
