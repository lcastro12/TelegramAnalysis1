package net.hockeyapp.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.wallet.WalletConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import net.hockeyapp.android.adapters.MessagesAdapter;
import net.hockeyapp.android.objects.ErrorObject;
import net.hockeyapp.android.objects.FeedbackMessage;
import net.hockeyapp.android.objects.FeedbackResponse;
import net.hockeyapp.android.tasks.ParseFeedbackTask;
import net.hockeyapp.android.tasks.SendFeedbackTask;
import net.hockeyapp.android.utils.PrefsUtil;
import net.hockeyapp.android.views.FeedbackView;
import org.telegram.messenger.BuildConfig;

public class FeedbackActivity extends Activity implements FeedbackActivityInterface, OnClickListener {
    private final int DIALOG_ERROR_ID = 0;
    private Button addResponseButton;
    private Context context;
    private EditText emailInput;
    private ErrorObject error;
    private Handler feedbackHandler;
    private ArrayList<FeedbackMessage> feedbackMessages;
    private ScrollView feedbackScrollView;
    private boolean inSendFeedback;
    private TextView lastUpdatedTextView;
    private MessagesAdapter messagesAdapter;
    private ListView messagesListView;
    private EditText nameInput;
    private Handler parseFeedbackHandler;
    private ParseFeedbackTask parseFeedbackTask;
    private Button refreshButton;
    private Button sendFeedbackButton;
    private SendFeedbackTask sendFeedbackTask;
    private EditText subjectInput;
    private EditText textInput;
    private String token;
    private String url;
    private LinearLayout wrapperLayoutFeedbackAndMessages;

    class C02721 implements Runnable {
        C02721() {
        }

        public void run() {
            PrefsUtil.getInstance().saveFeedbackTokenToPrefs(FeedbackActivity.this, null);
            FeedbackActivity.this.configureFeedbackView(false);
        }
    }

    class C02742 extends Handler {

        class C02731 implements Runnable {
            C02731() {
            }

            public void run() {
                FeedbackActivity.this.enableDisableSendFeedbackButton(true);
                FeedbackActivity.this.showDialog(0);
            }
        }

        C02742() {
        }

        public void handleMessage(Message msg) {
            boolean success = false;
            FeedbackActivity.this.error = new ErrorObject();
            if (msg == null || msg.getData() == null) {
                FeedbackActivity.this.error.setMessage("Message couldn't be posted. Please check your input values and try again.");
            } else {
                Bundle bundle = msg.getData();
                String responseString = bundle.getString("feedback_response");
                String statusCode = bundle.getString("feedback_status");
                String requestType = bundle.getString("request_type");
                if (requestType.equals("send") && (responseString == null || Integer.parseInt(statusCode) != 201)) {
                    FeedbackActivity.this.error.setMessage("Message couldn't be posted. Please check your input values and try again.");
                } else if (requestType.equals("fetch") && (Integer.parseInt(statusCode) == WalletConstants.ERROR_CODE_INVALID_PARAMETERS || Integer.parseInt(statusCode) == 422)) {
                    FeedbackActivity.this.resetFeedbackView();
                    success = true;
                } else {
                    FeedbackActivity.this.startParseFeedbackTask(responseString);
                    success = true;
                }
            }
            if (!success) {
                FeedbackActivity.this.runOnUiThread(new C02731());
            }
        }
    }

    class C02763 extends Handler {

        class C02751 implements Runnable {
            C02751() {
            }

            public void run() {
                FeedbackActivity.this.showDialog(0);
            }
        }

        C02763() {
        }

        public void handleMessage(Message msg) {
            boolean success = false;
            FeedbackActivity.this.error = new ErrorObject();
            if (!(msg == null || msg.getData() == null)) {
                FeedbackResponse feedbackResponse = (FeedbackResponse) msg.getData().getSerializable("parse_feedback_response");
                if (feedbackResponse != null) {
                    if (feedbackResponse.getStatus().equalsIgnoreCase("success")) {
                        success = true;
                        if (feedbackResponse.getToken() != null) {
                            PrefsUtil.getInstance().saveFeedbackTokenToPrefs(FeedbackActivity.this.context, feedbackResponse.getToken());
                            FeedbackActivity.this.loadFeedbackMessages(feedbackResponse);
                            FeedbackActivity.this.inSendFeedback = false;
                        }
                    } else {
                        success = false;
                    }
                }
            }
            if (!success) {
                FeedbackActivity.this.runOnUiThread(new C02751());
            }
            FeedbackActivity.this.enableDisableSendFeedbackButton(true);
        }
    }

    class C02785 implements DialogInterface.OnClickListener {
        C02785() {
        }

        public void onClick(DialogInterface dialog, int id) {
            FeedbackActivity.this.error = null;
            dialog.cancel();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());
        setTitle("Feedback");
        this.context = this;
        this.inSendFeedback = false;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.url = extras.getString(PlusShare.KEY_CALL_TO_ACTION_URL);
        }
        initFeedbackHandler();
        initParseFeedbackHandler();
        configureAppropriateView();
    }

    private void configureAppropriateView() {
        this.token = PrefsUtil.getInstance().getFeedbackTokenFromPrefs(this);
        if (this.token == null) {
            configureFeedbackView(false);
            return;
        }
        configureFeedbackView(true);
        sendFetchFeedback(this.url, null, null, null, null, this.token, this.feedbackHandler, true);
    }

    private void resetFeedbackView() {
        runOnUiThread(new C02721());
    }

    private void initFeedbackHandler() {
        this.feedbackHandler = new C02742();
    }

    private void initParseFeedbackHandler() {
        this.parseFeedbackHandler = new C02763();
    }

    protected void configureFeedbackView(boolean haveToken) {
        this.feedbackScrollView = (ScrollView) findViewById(FeedbackView.FEEDBACK_SCROLLVIEW_ID);
        this.wrapperLayoutFeedbackAndMessages = (LinearLayout) findViewById(FeedbackView.WRAPPER_LAYOUT_FEEDBACK_AND_MESSAGES_ID);
        this.messagesListView = (ListView) findViewById(FeedbackView.MESSAGES_LISTVIEW_ID);
        if (haveToken) {
            this.wrapperLayoutFeedbackAndMessages.setVisibility(0);
            this.feedbackScrollView.setVisibility(8);
            this.lastUpdatedTextView = (TextView) findViewById(8192);
            this.addResponseButton = (Button) findViewById(FeedbackView.ADD_RESPONSE_BUTTON_ID);
            this.addResponseButton.setOnClickListener(this);
            this.refreshButton = (Button) findViewById(FeedbackView.REFRESH_BUTTON_ID);
            this.refreshButton.setOnClickListener(this);
            return;
        }
        this.wrapperLayoutFeedbackAndMessages.setVisibility(8);
        this.feedbackScrollView.setVisibility(0);
        this.nameInput = (EditText) findViewById(8194);
        this.emailInput = (EditText) findViewById(FeedbackView.EMAIL_EDIT_TEXT_ID);
        this.subjectInput = (EditText) findViewById(FeedbackView.SUBJECT_EDIT_TEXT_ID);
        this.textInput = (EditText) findViewById(FeedbackView.TEXT_EDIT_TEXT_ID);
        String nameEmailSubject = PrefsUtil.getInstance().getNameEmailFromPrefs(this.context);
        if (nameEmailSubject != null) {
            String[] nameEmailSubjectArray = nameEmailSubject.split("\\|");
            if (nameEmailSubjectArray != null && nameEmailSubjectArray.length == 3) {
                this.nameInput.setText(nameEmailSubjectArray[0]);
                this.emailInput.setText(nameEmailSubjectArray[1]);
                this.subjectInput.setText(nameEmailSubjectArray[2]);
            }
        } else {
            this.nameInput.setText(BuildConfig.FLAVOR);
            this.emailInput.setText(BuildConfig.FLAVOR);
            this.subjectInput.setText(BuildConfig.FLAVOR);
        }
        this.textInput.setText(BuildConfig.FLAVOR);
        if (PrefsUtil.getInstance().getFeedbackTokenFromPrefs(this.context) != null) {
            this.subjectInput.setVisibility(8);
        } else {
            this.subjectInput.setVisibility(0);
        }
        this.sendFeedbackButton = (Button) findViewById(FeedbackView.SEND_FEEDBACK_BUTTON_ID);
        this.sendFeedbackButton.setOnClickListener(this);
    }

    public ViewGroup getLayoutView() {
        return new FeedbackView(this);
    }

    private void loadFeedbackMessages(final FeedbackResponse feedbackResponse) {
        runOnUiThread(new Runnable() {
            public void run() {
                FeedbackActivity.this.configureFeedbackView(true);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                SimpleDateFormat formatNew = new SimpleDateFormat("d MMM h:mm a");
                if (feedbackResponse != null && feedbackResponse.getFeedback() != null && feedbackResponse.getFeedback().getMessages() != null && feedbackResponse.getFeedback().getMessages().size() > 0) {
                    FeedbackActivity.this.feedbackMessages = feedbackResponse.getFeedback().getMessages();
                    Collections.reverse(FeedbackActivity.this.feedbackMessages);
                    try {
                        Date date = format.parse(((FeedbackMessage) FeedbackActivity.this.feedbackMessages.get(0)).getCreatedAt());
                        FeedbackActivity.this.lastUpdatedTextView.setText(String.format("Last Updated: %s", new Object[]{formatNew.format(date)}));
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    if (FeedbackActivity.this.messagesAdapter == null) {
                        FeedbackActivity.this.messagesAdapter = new MessagesAdapter(FeedbackActivity.this.context, FeedbackActivity.this.feedbackMessages);
                    } else {
                        FeedbackActivity.this.messagesAdapter.clear();
                        Iterator i$ = FeedbackActivity.this.feedbackMessages.iterator();
                        while (i$.hasNext()) {
                            FeedbackActivity.this.messagesAdapter.add((FeedbackMessage) i$.next());
                        }
                        FeedbackActivity.this.messagesAdapter.notifyDataSetChanged();
                    }
                    FeedbackActivity.this.messagesListView.setAdapter(FeedbackActivity.this.messagesAdapter);
                }
            }
        });
    }

    private void sendFeedback() {
        enableDisableSendFeedbackButton(false);
        if (this.nameInput.getText().toString().trim().length() <= 0 || this.emailInput.getText().toString().trim().length() <= 0 || this.subjectInput.getText().toString().trim().length() <= 0 || this.textInput.getText().toString().trim().length() <= 0) {
            this.error = new ErrorObject();
            this.error.setMessage("Please provide all details");
            showDialog(0);
            enableDisableSendFeedbackButton(true);
            return;
        }
        PrefsUtil.getInstance().saveNameEmailSubjectToPrefs(this.context, this.nameInput.getText().toString(), this.emailInput.getText().toString(), this.subjectInput.getText().toString());
        sendFetchFeedback(this.url, this.nameInput.getText().toString(), this.emailInput.getText().toString(), this.subjectInput.getText().toString(), this.textInput.getText().toString(), PrefsUtil.getInstance().getFeedbackTokenFromPrefs(this.context), this.feedbackHandler, false);
    }

    private void sendFetchFeedback(String url, String name, String email, String subject, String text, String token, Handler feedbackHandler, boolean isFetchMessages) {
        this.sendFeedbackTask = new SendFeedbackTask(this.context, url, name, email, subject, text, token, feedbackHandler, isFetchMessages);
        this.sendFeedbackTask.execute(new Void[0]);
    }

    private void startParseFeedbackTask(String feedbackResponseString) {
        createParseFeedbackTask(feedbackResponseString);
        this.parseFeedbackTask.execute(new Void[0]);
    }

    private void createParseFeedbackTask(String feedbackResponseString) {
        this.parseFeedbackTask = new ParseFeedbackTask(this, feedbackResponseString, this.parseFeedbackHandler);
    }

    public void enableDisableSendFeedbackButton(boolean isEnable) {
        if (this.sendFeedbackButton != null) {
            this.sendFeedbackButton.setEnabled(isEnable);
        }
    }

    public Object onRetainNonConfigurationInstance() {
        if (this.sendFeedbackTask != null) {
            this.sendFeedbackTask.detach();
        }
        return this.sendFeedbackTask;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case FeedbackView.SEND_FEEDBACK_BUTTON_ID /*8201*/:
                sendFeedback();
                return;
            case FeedbackView.ADD_RESPONSE_BUTTON_ID /*131088*/:
                configureFeedbackView(false);
                this.inSendFeedback = true;
                return;
            case FeedbackView.REFRESH_BUTTON_ID /*131089*/:
                sendFetchFeedback(this.url, null, null, null, null, PrefsUtil.getInstance().getFeedbackTokenFromPrefs(this.context), this.feedbackHandler, true);
                return;
            default:
                return;
        }
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new Builder(this).setMessage("An error has occured").setCancelable(false).setTitle("Error").setIcon(17301543).setPositiveButton("OK", new C02785()).create();
            default:
                return null;
        }
    }

    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case 0:
                AlertDialog messageDialogError = (AlertDialog) dialog;
                if (this.error != null) {
                    messageDialogError.setMessage(this.error.getMessage());
                    return;
                } else {
                    messageDialogError.setMessage("An error has occured");
                    return;
                }
            default:
                return;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.inSendFeedback) {
            this.inSendFeedback = false;
            configureAppropriateView();
        } else {
            finish();
        }
        return true;
    }
}
