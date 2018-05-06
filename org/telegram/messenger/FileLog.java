package org.telegram.messenger;

import android.net.Uri;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.ui.ApplicationLoader;

public class FileLog {
    public static FileLog Instance = new FileLog();
    private File currentFile = null;
    private FastDateFormat dateFormat = null;
    private DispatchQueue logQueue = null;
    private OutputStreamWriter streamWriter = null;

    public FileLog() {
        if (ConnectionsManager.DEBUG_VERSION) {
            this.dateFormat = FastDateFormat.getInstance("dd_MM_yyyy_HH_mm_ss", Locale.US);
            File sdCard = ApplicationLoader.applicationContext.getExternalFilesDir(null);
            if (sdCard != null) {
                File dir = new File(sdCard.getAbsolutePath() + "/logs");
                if (dir != null) {
                    dir.mkdirs();
                    this.currentFile = new File(dir, this.dateFormat.format(System.currentTimeMillis()) + ".txt");
                    if (this.currentFile != null) {
                        try {
                            this.currentFile.createNewFile();
                            this.streamWriter = new OutputStreamWriter(new FileOutputStream(this.currentFile));
                            this.streamWriter.write("-----start log " + this.dateFormat.format(System.currentTimeMillis()) + "-----\n");
                            this.streamWriter.flush();
                            this.logQueue = new DispatchQueue("logQueue");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void m801e(final String tag, final String message, final Throwable exception) {
        if (ConnectionsManager.DEBUG_VERSION) {
            Log.e(tag, message, exception);
            if (Instance.streamWriter != null) {
                Instance.logQueue.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            FileLog.Instance.streamWriter.write(FileLog.Instance.dateFormat.format(System.currentTimeMillis()) + " E/" + tag + "﹕ " + message + "\n");
                            FileLog.Instance.streamWriter.write(exception.toString());
                            FileLog.Instance.streamWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public static void m800e(final String tag, final String message) {
        if (ConnectionsManager.DEBUG_VERSION) {
            Log.e(tag, message);
            if (Instance.streamWriter != null) {
                Instance.logQueue.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            FileLog.Instance.streamWriter.write(FileLog.Instance.dateFormat.format(System.currentTimeMillis()) + " E/" + tag + "﹕ " + message + "\n");
                            FileLog.Instance.streamWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public static void m799e(final String tag, final Exception e) {
        if (ConnectionsManager.DEBUG_VERSION) {
            e.printStackTrace();
            if (Instance.streamWriter != null) {
                Instance.logQueue.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            FileLog.Instance.streamWriter.write(FileLog.Instance.dateFormat.format(System.currentTimeMillis()) + " E/" + tag + "﹕ " + e + "\n");
                            for (StackTraceElement el : e.getStackTrace()) {
                                FileLog.Instance.streamWriter.write(FileLog.Instance.dateFormat.format(System.currentTimeMillis()) + " E/" + tag + "﹕ " + el + "\n");
                            }
                            FileLog.Instance.streamWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                e.printStackTrace();
            }
        }
    }

    public static void m798d(final String tag, final String message) {
        if (ConnectionsManager.DEBUG_VERSION) {
            Log.d(tag, message);
            if (Instance.streamWriter != null) {
                Instance.logQueue.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            FileLog.Instance.streamWriter.write(FileLog.Instance.dateFormat.format(System.currentTimeMillis()) + " D/" + tag + "﹕ " + message + "\n");
                            FileLog.Instance.streamWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public static void cleanupLogs() {
        ArrayList<Uri> uris = new ArrayList();
        for (File file : new File(ApplicationLoader.applicationContext.getExternalFilesDir(null).getAbsolutePath() + "/logs").listFiles()) {
            if (Instance.currentFile == null || !file.getAbsolutePath().equals(Instance.currentFile.getAbsolutePath())) {
                file.delete();
            }
        }
    }
}
