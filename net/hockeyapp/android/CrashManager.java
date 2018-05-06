package net.hockeyapp.android;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import com.google.android.gms.plus.PlusShare;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.hockeyapp.android.utils.ConnectionManager;
import net.hockeyapp.android.utils.PrefsUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.telegram.messenger.BuildConfig;

public class CrashManager {
    private static String identifier = null;
    private static String urlString = null;

    static class C02714 implements FilenameFilter {
        C02714() {
        }

        public boolean accept(File dir, String name) {
            return name.endsWith(".stacktrace");
        }
    }

    public static void register(Context context, String appIdentifier) {
        register(context, Constants.BASE_URL, appIdentifier, null);
    }

    public static void register(Context context, String appIdentifier, CrashManagerListener listener) {
        register(context, Constants.BASE_URL, appIdentifier, listener);
    }

    public static void register(Context context, String urlString, String appIdentifier, CrashManagerListener listener) {
        initialize(context, urlString, appIdentifier, listener, false);
        execute(context, listener);
    }

    public static void initialize(Context context, String appIdentifier, CrashManagerListener listener) {
        initialize(context, Constants.BASE_URL, appIdentifier, listener, true);
    }

    public static void initialize(Context context, String urlString, String appIdentifier, CrashManagerListener listener) {
        initialize(context, urlString, appIdentifier, listener, true);
    }

    public static void execute(Context context, CrashManagerListener listener) {
        boolean z;
        if (listener == null || !listener.ignoreDefaultHandler()) {
            z = false;
        } else {
            z = true;
        }
        Boolean ignoreDefaultHandler = Boolean.valueOf(z);
        WeakReference<Context> weakContext = new WeakReference(context);
        int foundOrSend = hasStackTraces(weakContext);
        if (foundOrSend == 1) {
            Boolean autoSend = Boolean.valueOf(false);
            if (listener != null) {
                autoSend = Boolean.valueOf(Boolean.valueOf(autoSend.booleanValue() | listener.shouldAutoUploadCrashes()).booleanValue() | listener.onCrashesFound());
                listener.onNewCrashesFound();
            }
            if (autoSend.booleanValue()) {
                sendCrashes(weakContext, listener, ignoreDefaultHandler.booleanValue());
            } else {
                showDialog(weakContext, listener, ignoreDefaultHandler.booleanValue());
            }
        } else if (foundOrSend == 2) {
            if (listener != null) {
                listener.onConfirmedCrashesFound();
            }
            sendCrashes(weakContext, listener, ignoreDefaultHandler.booleanValue());
        } else {
            registerHandler(weakContext, listener, ignoreDefaultHandler.booleanValue());
        }
    }

    public static int hasStackTraces(WeakReference<Context> weakContext) {
        String[] filenames = searchForStackTraces();
        List<String> confirmedFilenames = null;
        if (filenames == null || filenames.length <= 0) {
            return 0;
        }
        if (weakContext != null) {
            try {
                Context context = (Context) weakContext.get();
                if (context != null) {
                    confirmedFilenames = Arrays.asList(context.getSharedPreferences(Constants.SDK_NAME, 0).getString("ConfirmedFilenames", BuildConfig.FLAVOR).split("\\|"));
                }
            } catch (Exception e) {
            }
        }
        if (confirmedFilenames == null) {
            return 1;
        }
        for (String filename : filenames) {
            if (!confirmedFilenames.contains(filename)) {
                return 1;
            }
        }
        return 2;
    }

    public static void submitStackTraces(WeakReference<Context> weakContext, CrashManagerListener listener) {
        String[] list = searchForStackTraces();
        Boolean successful = Boolean.valueOf(false);
        if (list != null && list.length > 0) {
            Log.d(Constants.TAG, "Found " + list.length + " stacktrace(s).");
            for (int index = 0; index < list.length; index++) {
                try {
                    String filename = list[index];
                    String stacktrace = contentsOfFile(weakContext, filename);
                    if (stacktrace.length() > 0) {
                        Log.d(Constants.TAG, "Transmitting crash data: \n" + stacktrace);
                        DefaultHttpClient httpClient = (DefaultHttpClient) ConnectionManager.getInstance().getHttpClient();
                        HttpPost httpPost = new HttpPost(getURLString());
                        List<NameValuePair> parameters = new ArrayList();
                        parameters.add(new BasicNameValuePair("raw", stacktrace));
                        parameters.add(new BasicNameValuePair("userID", contentsOfFile(weakContext, filename.replace(".stacktrace", ".user"))));
                        parameters.add(new BasicNameValuePair("contact", contentsOfFile(weakContext, filename.replace(".stacktrace", ".contact"))));
                        parameters.add(new BasicNameValuePair(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, contentsOfFile(weakContext, filename.replace(".stacktrace", ".description"))));
                        parameters.add(new BasicNameValuePair("sdk", Constants.SDK_NAME));
                        parameters.add(new BasicNameValuePair("sdk_version", Constants.SDK_VERSION));
                        httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
                        httpClient.execute(httpPost);
                        successful = Boolean.valueOf(true);
                    }
                    if (successful.booleanValue()) {
                        deleteStackTrace(weakContext, list[index]);
                        if (listener != null) {
                            listener.onCrashesSent();
                        }
                    } else if (listener != null) {
                        listener.onCrashesNotSent();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (successful.booleanValue()) {
                        deleteStackTrace(weakContext, list[index]);
                        if (listener != null) {
                            listener.onCrashesSent();
                        }
                    } else if (listener != null) {
                        listener.onCrashesNotSent();
                    }
                } catch (Throwable th) {
                    if (successful.booleanValue()) {
                        deleteStackTrace(weakContext, list[index]);
                        if (listener != null) {
                            listener.onCrashesSent();
                        }
                    } else if (listener != null) {
                        listener.onCrashesNotSent();
                    }
                }
            }
        }
    }

    public static void deleteStackTraces(WeakReference<Context> weakContext) {
        String[] list = searchForStackTraces();
        if (list != null && list.length > 0) {
            Log.d(Constants.TAG, "Found " + list.length + " stacktrace(s).");
            for (int index = 0; index < list.length; index++) {
                if (weakContext != null) {
                    try {
                        Log.d(Constants.TAG, "Delete stacktrace " + list[index] + ".");
                        deleteStackTrace(weakContext, list[index]);
                        Context context = (Context) weakContext.get();
                        if (context != null) {
                            context.deleteFile(list[index]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void initialize(Context context, String urlString, String appIdentifier, CrashManagerListener listener, boolean registerHandler) {
        if (context != null) {
            urlString = urlString;
            identifier = appIdentifier;
            Constants.loadFromContext(context);
            if (identifier == null) {
                identifier = Constants.APP_PACKAGE;
            }
            if (registerHandler) {
                boolean z = listener != null && listener.ignoreDefaultHandler();
                registerHandler(new WeakReference(context), listener, Boolean.valueOf(z).booleanValue());
            }
        }
    }

    private static void showDialog(final WeakReference<Context> weakContext, final CrashManagerListener listener, final boolean ignoreDefaultHandler) {
        Context context = null;
        if (weakContext != null) {
            context = (Context) weakContext.get();
        }
        if (context != null) {
            Builder builder = new Builder(context);
            builder.setTitle(Strings.get(listener, 0));
            builder.setMessage(Strings.get(listener, 1));
            builder.setNegativeButton(Strings.get(listener, 2), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onUserDeniedCrashes();
                    }
                    CrashManager.deleteStackTraces(weakContext);
                    CrashManager.registerHandler(weakContext, listener, ignoreDefaultHandler);
                }
            });
            builder.setPositiveButton(Strings.get(listener, 3), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    CrashManager.sendCrashes(weakContext, listener, ignoreDefaultHandler);
                }
            });
            builder.create().show();
        }
    }

    private static void sendCrashes(final WeakReference<Context> weakContext, final CrashManagerListener listener, final boolean ignoreDefaultHandler) {
        saveConfirmedStackTraces(weakContext);
        new Thread() {
            public void run() {
                CrashManager.submitStackTraces(weakContext, listener);
                CrashManager.registerHandler(weakContext, listener, ignoreDefaultHandler);
            }
        }.start();
    }

    private static void registerHandler(WeakReference<Context> weakReference, CrashManagerListener listener, boolean ignoreDefaultHandler) {
        if (Constants.APP_VERSION == null || Constants.APP_PACKAGE == null) {
            Log.d(Constants.TAG, "Exception handler not set because version or package is null.");
            return;
        }
        UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (currentHandler != null) {
            Log.d(Constants.TAG, "Current handler class = " + currentHandler.getClass().getName());
        }
        if (currentHandler instanceof ExceptionHandler) {
            ((ExceptionHandler) currentHandler).setListener(listener);
        } else {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(currentHandler, listener, ignoreDefaultHandler));
        }
    }

    private static String getURLString() {
        return urlString + "api/2/apps/" + identifier + "/crashes/";
    }

    private static void deleteStackTrace(WeakReference<Context> weakContext, String filename) {
        if (weakContext != null) {
            Context context = (Context) weakContext.get();
            if (context != null) {
                context.deleteFile(filename);
                context.deleteFile(filename.replace(".stacktrace", ".user"));
                context.deleteFile(filename.replace(".stacktrace", ".contact"));
                context.deleteFile(filename.replace(".stacktrace", ".description"));
            }
        }
    }

    private static String contentsOfFile(WeakReference<Context> weakContext, String filename) {
        IOException e;
        Throwable th;
        if (weakContext != null) {
            Context context = (Context) weakContext.get();
            if (context != null) {
                StringBuilder contents = new StringBuilder();
                BufferedReader reader = null;
                try {
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
                    while (true) {
                        try {
                            String line = reader2.readLine();
                            if (line == null) {
                                break;
                            }
                            contents.append(line);
                            contents.append(System.getProperty("line.separator"));
                        } catch (FileNotFoundException e2) {
                            reader = reader2;
                        } catch (IOException e3) {
                            e = e3;
                            reader = reader2;
                        } catch (Throwable th2) {
                            th = th2;
                            reader = reader2;
                        }
                    }
                    if (reader2 != null) {
                        try {
                            reader2.close();
                            reader = reader2;
                        } catch (IOException e4) {
                            reader = reader2;
                        }
                    }
                } catch (FileNotFoundException e5) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e6) {
                        }
                    }
                    return contents.toString();
                } catch (IOException e7) {
                    e = e7;
                    try {
                        e.printStackTrace();
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e8) {
                            }
                        }
                        return contents.toString();
                    } catch (Throwable th3) {
                        th = th3;
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e9) {
                            }
                        }
                        throw th;
                    }
                }
                return contents.toString();
            }
        }
        return null;
    }

    private static void saveConfirmedStackTraces(WeakReference<Context> weakContext) {
        if (weakContext != null) {
            Context context = (Context) weakContext.get();
            if (context != null) {
                try {
                    String[] filenames = searchForStackTraces();
                    Editor editor = context.getSharedPreferences(Constants.SDK_NAME, 0).edit();
                    editor.putString("ConfirmedFilenames", joinArray(filenames, "|"));
                    PrefsUtil.applyChanges(editor);
                } catch (Exception e) {
                }
            }
        }
    }

    private static String joinArray(String[] array, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < array.length; index++) {
            buffer.append(array[index]);
            if (index < array.length - 1) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }

    private static String[] searchForStackTraces() {
        if (Constants.FILES_PATH != null) {
            Log.d(Constants.TAG, "Looking for exceptions in: " + Constants.FILES_PATH);
            File dir = new File(Constants.FILES_PATH + "/");
            if (dir.mkdir() || dir.exists()) {
                return dir.list(new C02714());
            }
            return new String[0];
        }
        Log.d(Constants.TAG, "Can't search for exception as file path is null.");
        return null;
    }
}
