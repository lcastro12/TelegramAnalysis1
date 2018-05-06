package net.hockeyapp.android.tasks;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Environment;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import net.hockeyapp.android.Strings;
import net.hockeyapp.android.listeners.DownloadFileListener;

public class DownloadFileTask extends AsyncTask<String, Integer, Boolean> {
    private Context context;
    private String filePath = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");
    private String filename = (UUID.randomUUID() + ".apk");
    private DownloadFileListener notifier;
    private ProgressDialog progressDialog;
    private String urlString;

    class C02841 implements OnClickListener {
        C02841() {
        }

        public void onClick(DialogInterface dialog, int which) {
            DownloadFileTask.this.notifier.downloadFailed(DownloadFileTask.this, Boolean.valueOf(false));
        }
    }

    class C02852 implements OnClickListener {
        C02852() {
        }

        public void onClick(DialogInterface dialog, int which) {
            DownloadFileTask.this.notifier.downloadFailed(DownloadFileTask.this, Boolean.valueOf(true));
        }
    }

    public DownloadFileTask(Context context, String urlString, DownloadFileListener notifier) {
        this.context = context;
        this.urlString = urlString;
        this.notifier = notifier;
    }

    public void attach(Context context) {
        this.context = context;
    }

    public void detach() {
        this.context = null;
        this.progressDialog = null;
    }

    protected Boolean doInBackground(String... args) {
        try {
            URLConnection connection = createConnection(new URL(getURLString()));
            connection.connect();
            int lenghtOfFile = connection.getContentLength();
            File dir = new File(this.filePath);
            if (dir.mkdirs() || dir.exists()) {
                File file = new File(dir, this.filename);
                InputStream input = new BufferedInputStream(connection.getInputStream());
                OutputStream output = new FileOutputStream(file);
                byte[] data = new byte[1024];
                long total = 0;
                while (true) {
                    int count = input.read(data);
                    if (count == -1) {
                        break;
                    }
                    total += (long) count;
                    publishProgress(new Integer[]{Integer.valueOf((int) ((100 * total) / ((long) lenghtOfFile)))});
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                return Boolean.valueOf(total > 0);
            }
            throw new IOException("Could not create the dir(s):" + dir.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.valueOf(false);
        }
    }

    protected URLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("User-Agent", "HockeySDK/Android");
        connection.setInstanceFollowRedirects(true);
        if (VERSION.SDK_INT <= 9) {
            connection.setRequestProperty("connection", "close");
        }
        return connection;
    }

    protected void onProgressUpdate(Integer... args) {
        try {
            if (this.progressDialog == null) {
                this.progressDialog = new ProgressDialog(this.context);
                this.progressDialog.setProgressStyle(1);
                this.progressDialog.setMessage("Loading...");
                this.progressDialog.setCancelable(false);
                this.progressDialog.show();
            }
            this.progressDialog.setProgress(args[0].intValue());
        } catch (Exception e) {
        }
    }

    protected void onPostExecute(Boolean result) {
        if (this.progressDialog != null) {
            try {
                this.progressDialog.dismiss();
            } catch (Exception e) {
            }
        }
        if (result.booleanValue()) {
            this.notifier.downloadSuccessful(this);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(new File(this.filePath, this.filename)), "application/vnd.android.package-archive");
            intent.setFlags(268435456);
            this.context.startActivity(intent);
            return;
        }
        try {
            Builder builder = new Builder(this.context);
            builder.setTitle(Strings.get(this.notifier, 4));
            builder.setMessage(Strings.get(this.notifier, 5));
            builder.setNegativeButton(Strings.get(this.notifier, 6), new C02841());
            builder.setPositiveButton(Strings.get(this.notifier, 7), new C02852());
            builder.create().show();
        } catch (Exception e2) {
        }
    }

    private String getURLString() {
        return this.urlString + "&type=apk";
    }
}
