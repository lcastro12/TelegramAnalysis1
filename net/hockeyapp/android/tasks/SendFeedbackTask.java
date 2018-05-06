package net.hockeyapp.android.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.hockeyapp.android.Constants;
import net.hockeyapp.android.utils.ConnectionManager;
import net.hockeyapp.android.utils.Util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.telegram.messenger.BuildConfig;

public class SendFeedbackTask extends AsyncTask<Void, Void, HashMap<String, String>> {
    private Context context;
    private String email;
    private Handler handler;
    private boolean isFetchMessages;
    private String name;
    private ProgressDialog progressDialog;
    private String subject;
    private String text;
    private String token;
    private String urlString;

    public SendFeedbackTask(Context context, String urlString, String name, String email, String subject, String text, String token, Handler handler, boolean isFetchMessages) {
        this.context = context;
        this.urlString = urlString;
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.text = text;
        this.token = token;
        this.handler = handler;
        this.isFetchMessages = isFetchMessages;
        if (context != null) {
            Constants.loadFromContext(context);
        }
    }

    public void attach(Context context) {
        this.context = context;
    }

    public void detach() {
        this.context = null;
        this.progressDialog = null;
    }

    protected void onPreExecute() {
        String loadingMessage = "Sending feedback..";
        if (this.isFetchMessages) {
            loadingMessage = "Retrieving discussions...";
        }
        if (this.progressDialog == null || !this.progressDialog.isShowing()) {
            this.progressDialog = ProgressDialog.show(this.context, BuildConfig.FLAVOR, loadingMessage, true, false);
        }
    }

    protected HashMap<String, String> doInBackground(Void... args) {
        HttpClient httpclient = ConnectionManager.getInstance().getHttpClient();
        if (this.isFetchMessages && this.token != null) {
            return doGet(httpclient);
        }
        if (this.isFetchMessages) {
            return null;
        }
        return doPostPut(httpclient);
    }

    protected void onPostExecute(HashMap<String, String> result) {
        if (this.progressDialog != null) {
            try {
                this.progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.handler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("request_type", (String) result.get("type"));
            bundle.putString("feedback_response", (String) result.get("response"));
            bundle.putString("feedback_status", (String) result.get("status"));
            msg.setData(bundle);
            this.handler.sendMessage(msg);
        }
    }

    private HashMap<String, String> doPostPut(HttpClient httpClient) {
        try {
            List<NameValuePair> nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("name", this.name));
            nameValuePairs.add(new BasicNameValuePair("email", this.email));
            nameValuePairs.add(new BasicNameValuePair("subject", this.subject));
            nameValuePairs.add(new BasicNameValuePair("text", this.text));
            nameValuePairs.add(new BasicNameValuePair("bundle_identifier", Constants.APP_PACKAGE));
            nameValuePairs.add(new BasicNameValuePair("bundle_short_version", Constants.APP_VERSION_NAME));
            nameValuePairs.add(new BasicNameValuePair("bundle_version", Constants.APP_VERSION));
            nameValuePairs.add(new BasicNameValuePair("os_version", Constants.ANDROID_VERSION));
            nameValuePairs.add(new BasicNameValuePair("oem", Constants.PHONE_MANUFACTURER));
            nameValuePairs.add(new BasicNameValuePair("model", Constants.PHONE_MODEL));
            UrlEncodedFormEntity form = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            form.setContentEncoding("UTF-8");
            HttpPost httpPost = null;
            HttpPut httpPut = null;
            if (this.token != null) {
                this.urlString += this.token + "/";
                httpPut = new HttpPut(this.urlString);
            } else {
                httpPost = new HttpPost(this.urlString);
            }
            HttpResponse response = null;
            if (httpPut != null) {
                httpPut.setEntity(form);
                response = httpClient.execute(httpPut);
            } else if (httpPost != null) {
                httpPost.setEntity(form);
                response = httpClient.execute(httpPost);
            }
            if (response == null) {
                return null;
            }
            HttpEntity resEntity = response.getEntity();
            HashMap<String, String> result = new HashMap();
            result.put("type", "send");
            result.put("response", EntityUtils.toString(resEntity));
            result.put("status", BuildConfig.FLAVOR + response.getStatusLine().getStatusCode());
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
            return null;
        } catch (IOException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    private HashMap<String, String> doGet(HttpClient httpClient) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.urlString + Util.encodeParam(this.token));
        try {
            HttpResponse response = httpClient.execute(new HttpGet(sb.toString()));
            HttpEntity responseEntity = response.getEntity();
            HashMap<String, String> result = new HashMap();
            result.put("type", "fetch");
            result.put("response", EntityUtils.toString(responseEntity));
            result.put("status", BuildConfig.FLAVOR + response.getStatusLine().getStatusCode());
            return result;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IllegalStateException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return null;
    }
}
