package net.hockeyapp.android.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import net.hockeyapp.android.UpdateInfoListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.BuildConfig;

public class VersionHelper {
    UpdateInfoListener listener;
    JSONObject newest;
    ArrayList<JSONObject> sortedVersions;

    class C02931 implements Comparator<JSONObject> {
        C02931() {
        }

        public int compare(JSONObject object1, JSONObject object2) {
            try {
                return object1.getInt("version") > object2.getInt("version") ? 0 : 0;
            } catch (JSONException e) {
            } catch (NullPointerException e2) {
            }
        }
    }

    public VersionHelper(String infoJSON, UpdateInfoListener listener) {
        this.listener = listener;
        loadVersions(infoJSON);
        sortVersions();
    }

    private void loadVersions(String infoJSON) {
        this.newest = new JSONObject();
        this.sortedVersions = new ArrayList();
        try {
            JSONArray versions = new JSONArray(infoJSON);
            int versionCode = this.listener.getCurrentVersionCode();
            for (int index = 0; index < versions.length(); index++) {
                JSONObject entry = versions.getJSONObject(index);
                if (entry.getInt("version") > versionCode) {
                    this.newest = entry;
                    versionCode = entry.getInt("version");
                }
                this.sortedVersions.add(entry);
            }
        } catch (JSONException e) {
        } catch (NullPointerException e2) {
        }
    }

    private void sortVersions() {
        Collections.sort(this.sortedVersions, new C02931());
    }

    public String getVersionString() {
        return failSafeGetStringFromJSON(this.newest, "shortversion", BuildConfig.FLAVOR) + " (" + failSafeGetStringFromJSON(this.newest, "version", BuildConfig.FLAVOR) + ")";
    }

    public String getFileInfoString() {
        int appSize = failSafeGetIntFromJSON(this.newest, "appsize", 0);
        Date date = new Date(1000 * ((long) failSafeGetIntFromJSON(this.newest, "timestamp", 0)));
        return new SimpleDateFormat("dd.MM.yyyy").format(date) + " - " + String.format("%.2f", new Object[]{Float.valueOf((((float) appSize) / 1024.0f) / 1024.0f)}) + " MB";
    }

    private static String failSafeGetStringFromJSON(JSONObject json, String name, String defaultValue) {
        try {
            defaultValue = json.getString(name);
        } catch (JSONException e) {
        }
        return defaultValue;
    }

    private static int failSafeGetIntFromJSON(JSONObject json, String name, int defaultValue) {
        try {
            defaultValue = json.getInt(name);
        } catch (JSONException e) {
        }
        return defaultValue;
    }

    public String getReleaseNotes(boolean showRestore) {
        StringBuilder result = new StringBuilder();
        result.append("<html>");
        result.append("<body style='padding: 0px 0px 20px 0px'>");
        int count = 0;
        Iterator i$ = this.sortedVersions.iterator();
        while (i$.hasNext()) {
            JSONObject version = (JSONObject) i$.next();
            if (count > 0) {
                result.append(getSeparator());
                if (showRestore) {
                    result.append(getRestoreButton(count, version));
                }
            }
            result.append(getVersionLine(count, version));
            result.append(getVersionNotes(count, version));
            count++;
        }
        result.append("</body>");
        result.append("</html>");
        return result.toString();
    }

    private Object getSeparator() {
        return "<hr style='border-top: 1px solid #c8c8c8; border-bottom: 0px; margin: 40px 10px 0px 10px;' />";
    }

    private String getRestoreButton(int count, JSONObject version) {
        StringBuilder result = new StringBuilder();
        String versionID = getVersionID(version);
        if (versionID.length() > 0) {
            result.append("<a href='restore:" + versionID + "'  style='background: #c8c8c8; color: #000; display: block; float: right; padding: 7px; margin: 0px 10px 10px; text-decoration: none;'>Restore</a>");
        }
        return result.toString();
    }

    private String getVersionID(JSONObject version) {
        String versionID = BuildConfig.FLAVOR;
        try {
            versionID = version.getString("id");
        } catch (JSONException e) {
        }
        return versionID;
    }

    private String getVersionLine(int count, JSONObject version) {
        StringBuilder result = new StringBuilder();
        int versionCode = getVersionCode(version);
        String versionName = getVersionName(version);
        result.append("<div style='padding: 20px 10px 10px;'><strong>");
        if (count == 0) {
            result.append("Newest version:");
        } else {
            result.append("Version " + versionName + " (" + versionCode + "): " + (versionCode == this.listener.getCurrentVersionCode() ? "[INSTALLED]" : BuildConfig.FLAVOR));
        }
        result.append("</strong></div>");
        return result.toString();
    }

    private int getVersionCode(JSONObject version) {
        int versionCode = 0;
        try {
            versionCode = version.getInt("version");
        } catch (JSONException e) {
        }
        return versionCode;
    }

    private String getVersionName(JSONObject version) {
        String versionName = BuildConfig.FLAVOR;
        try {
            versionName = version.getString("shortversion");
        } catch (JSONException e) {
        }
        return versionName;
    }

    private String getVersionNotes(int count, JSONObject version) {
        StringBuilder result = new StringBuilder();
        String notes = failSafeGetStringFromJSON(version, "notes", BuildConfig.FLAVOR);
        result.append("<div style='padding: 0px 10px;'>");
        if (notes.trim().length() == 0) {
            result.append("<em>No information.</em>");
        } else {
            result.append(notes);
        }
        result.append("</div>");
        return result.toString();
    }

    public static int compareVersionStrings(String left, String right) {
        if (left == null || right == null) {
            return 0;
        }
        try {
            Scanner leftScanner = new Scanner(left.replaceAll("\\-.*", BuildConfig.FLAVOR));
            Scanner rightScanner = new Scanner(right.replaceAll("\\-.*", BuildConfig.FLAVOR));
            leftScanner.useDelimiter("\\.");
            rightScanner.useDelimiter("\\.");
            while (leftScanner.hasNextInt() && rightScanner.hasNextInt()) {
                int leftValue = leftScanner.nextInt();
                int rightValue = rightScanner.nextInt();
                if (leftValue < rightValue) {
                    return -1;
                }
                if (leftValue > rightValue) {
                    return 1;
                }
            }
            if (leftScanner.hasNextInt()) {
                return 1;
            }
            if (rightScanner.hasNextInt()) {
                return -1;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
