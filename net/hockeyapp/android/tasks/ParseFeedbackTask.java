package net.hockeyapp.android.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import net.hockeyapp.android.objects.FeedbackResponse;
import net.hockeyapp.android.utils.FeedbackParser;

public class ParseFeedbackTask extends AsyncTask<Void, Void, FeedbackResponse> {
    private Context context;
    private String feedbackResponse;
    private Handler handler;

    public ParseFeedbackTask(Context context, String feedbackResponse, Handler handler) {
        this.context = context;
        this.feedbackResponse = feedbackResponse;
        this.handler = handler;
    }

    protected FeedbackResponse doInBackground(Void... params) {
        if (this.context == null || this.feedbackResponse == null) {
            return null;
        }
        return FeedbackParser.getInstance().parseFeedbackResponse(this.feedbackResponse);
    }

    protected void onPostExecute(FeedbackResponse result) {
        if (result != null && this.handler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putSerializable("parse_feedback_response", result);
            msg.setData(bundle);
            this.handler.sendMessage(msg);
        }
    }
}
