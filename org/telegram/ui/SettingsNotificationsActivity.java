package org.telegram.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.System;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.TL_account_resetNotifySettings;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.RPCRequest;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.OnSwipeTouchListener;

public class SettingsNotificationsActivity extends BaseFragment {
    private ListView listView;
    private boolean reseting = false;

    class C05831 implements OnItemClickListener {

        class C08891 implements RPCRequestDelegate {

            class C05821 implements Runnable {
                C05821() {
                }

                public void run() {
                    ActionBarActivity inflaterActivity = SettingsNotificationsActivity.this.parentActivity;
                    if (inflaterActivity == null) {
                        inflaterActivity = (ActionBarActivity) SettingsNotificationsActivity.this.getActivity();
                    }
                    if (inflaterActivity != null) {
                        SettingsNotificationsActivity.this.reseting = false;
                        Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0).edit();
                        editor.clear();
                        editor.commit();
                        SettingsNotificationsActivity.this.listView.invalidateViews();
                        Toast.makeText(inflaterActivity, C0419R.string.ResetNotificationsText, 0).show();
                    }
                }
            }

            C08891() {
            }

            public void run(TLObject response, TL_error error) {
                Utilities.RunOnUIThread(new C05821());
            }
        }

        C05831() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SharedPreferences preferences;
            Editor editor;
            if (i == 1 || i == 6) {
                preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
                editor = preferences.edit();
                if (i == 1) {
                    editor.putBoolean("EnableAll", !preferences.getBoolean("EnableAll", true));
                } else if (i == 6) {
                    editor.putBoolean("EnableGroup", !preferences.getBoolean("EnableGroup", true));
                }
                editor.commit();
                SettingsNotificationsActivity.this.listView.invalidateViews();
            } else if (i == 2 || i == 7) {
                preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
                editor = preferences.edit();
                if (i == 2) {
                    editor.putBoolean("EnablePreviewAll", !preferences.getBoolean("EnablePreviewAll", true));
                } else if (i == 7) {
                    editor.putBoolean("EnablePreviewGroup", !preferences.getBoolean("EnablePreviewGroup", true));
                }
                editor.commit();
                SettingsNotificationsActivity.this.listView.invalidateViews();
            } else if (i == 3 || i == 8) {
                preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
                editor = preferences.edit();
                if (i == 3) {
                    editor.putBoolean("EnableVibrateAll", !preferences.getBoolean("EnableVibrateAll", true));
                } else if (i == 8) {
                    editor.putBoolean("EnableVibrateGroup", !preferences.getBoolean("EnableVibrateGroup", true));
                }
                editor.commit();
                SettingsNotificationsActivity.this.listView.invalidateViews();
            } else if (i == 4 || i == 9) {
                try {
                    preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
                    Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
                    intent.putExtra("android.intent.extra.ringtone.TYPE", 2);
                    intent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", false);
                    Uri currentSound = null;
                    String defaultPath = null;
                    Uri defaultUri = System.DEFAULT_NOTIFICATION_URI;
                    if (defaultUri != null) {
                        defaultPath = defaultUri.getPath();
                    }
                    String path;
                    if (i == 4) {
                        path = preferences.getString("GlobalSoundPath", defaultPath);
                        if (!(path == null || path.equals("NoSound"))) {
                            currentSound = path.equals(defaultPath) ? defaultUri : Uri.parse(path);
                        }
                    } else if (i == 9) {
                        path = preferences.getString("GroupSoundPath", defaultPath);
                        if (!(path == null || path.equals("NoSound"))) {
                            currentSound = path.equals(defaultPath) ? defaultUri : Uri.parse(path);
                        }
                    }
                    intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", currentSound);
                    SettingsNotificationsActivity.this.startActivityForResult(intent, i);
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            } else if (i == 15) {
                if (!SettingsNotificationsActivity.this.reseting) {
                    SettingsNotificationsActivity.this.reseting = true;
                    ConnectionsManager.Instance.performRpc(new TL_account_resetNotifySettings(), new C08891(), null, true, RPCRequest.RPCRequestClassGeneric);
                }
            } else if (i == 11) {
                preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
                editor = preferences.edit();
                editor.putBoolean("EnableInAppSounds", !preferences.getBoolean("EnableInAppSounds", true));
                editor.commit();
                SettingsNotificationsActivity.this.listView.invalidateViews();
            } else if (i == 12) {
                preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
                editor = preferences.edit();
                editor.putBoolean("EnableInAppVibrate", !preferences.getBoolean("EnableInAppVibrate", true));
                editor.commit();
                SettingsNotificationsActivity.this.listView.invalidateViews();
            } else if (i == 13) {
                preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
                editor = preferences.edit();
                editor.putBoolean("EnableInAppPreview", !preferences.getBoolean("EnableInAppPreview", true));
                editor.commit();
                SettingsNotificationsActivity.this.listView.invalidateViews();
            }
        }
    }

    private class ListAdapter extends BaseAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean areAllItemsEnabled() {
            return false;
        }

        public boolean isEnabled(int i) {
            boolean enabledAll = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0).getBoolean("EnableAll", true);
            if (i != 1 && !enabledAll && i != 13) {
                return false;
            }
            if ((i <= 0 || i >= 5) && ((i <= 5 || i >= 10) && ((i <= 10 || i >= 14) && i != 15))) {
                return false;
            }
            return true;
        }

        public int getCount() {
            return 16;
        }

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public boolean hasStableIds() {
            return false;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView;
            int type = getItemViewType(i);
            if (type == 0) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_section_layout, viewGroup, false);
                }
                textView = (TextView) view.findViewById(C0419R.id.settings_section_text);
                if (i == 0) {
                    textView.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.MessageNotifications));
                } else if (i == 5) {
                    textView.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.GroupNotifications));
                } else if (i == 10) {
                    textView.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.InAppNotifications));
                } else if (i == 14) {
                    textView.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.Reset));
                }
            }
            View divider;
            SharedPreferences preferences;
            boolean enabledAll;
            if (type == 1) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_row_check_notify_layout, viewGroup, false);
                }
                textView = (TextView) view.findViewById(C0419R.id.settings_row_text);
                divider = view.findViewById(C0419R.id.settings_row_divider);
                ImageView checkButton = (ImageView) view.findViewById(C0419R.id.settings_row_check_button);
                preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
                boolean enabled = false;
                enabledAll = preferences.getBoolean("EnableAll", true);
                if (i == 1 || i == 6) {
                    if (i == 1) {
                        enabled = enabledAll;
                    } else if (i == 6) {
                        enabled = preferences.getBoolean("EnableGroup", true);
                    }
                    textView.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.Alert));
                    divider.setVisibility(0);
                } else if (i == 2 || i == 7) {
                    if (i == 2) {
                        enabled = preferences.getBoolean("EnablePreviewAll", true);
                    } else if (i == 7) {
                        enabled = preferences.getBoolean("EnablePreviewGroup", true);
                    }
                    textView.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.MessagePreview));
                    divider.setVisibility(0);
                } else if (i == 3 || i == 8) {
                    if (i == 3) {
                        enabled = preferences.getBoolean("EnableVibrateAll", true);
                    } else if (i == 8) {
                        enabled = preferences.getBoolean("EnableVibrateGroup", true);
                    }
                    textView.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.Vibrate));
                    divider.setVisibility(0);
                } else if (i == 11) {
                    enabled = preferences.getBoolean("EnableInAppSounds", true);
                    textView.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.InAppSounds));
                    divider.setVisibility(0);
                } else if (i == 12) {
                    enabled = preferences.getBoolean("EnableInAppVibrate", true);
                    textView.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.InAppVibrate));
                    divider.setVisibility(0);
                } else if (i == 13) {
                    enabled = preferences.getBoolean("EnableInAppPreview", true);
                    textView.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.InAppPreview));
                    divider.setVisibility(4);
                }
                if (enabled) {
                    checkButton.setImageResource(C0419R.drawable.btn_check_on);
                } else {
                    checkButton.setImageResource(C0419R.drawable.btn_check_off);
                }
                if (i == 1 || enabledAll) {
                    if (VERSION.SDK_INT >= 11) {
                        checkButton.setAlpha(1.0f);
                        textView.setAlpha(1.0f);
                    }
                    view.setEnabled(true);
                } else {
                    view.setEnabled(false);
                    if (VERSION.SDK_INT >= 11) {
                        checkButton.setAlpha(0.3f);
                        textView.setAlpha(0.3f);
                    }
                }
            } else if (type == 2) {
                if (view == null) {
                    view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_row_detail_layout, viewGroup, false);
                }
                textView = (TextView) view.findViewById(C0419R.id.settings_row_text);
                TextView textViewDetail = (TextView) view.findViewById(C0419R.id.settings_row_text_detail);
                divider = view.findViewById(C0419R.id.settings_row_divider);
                preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
                enabledAll = preferences.getBoolean("EnableAll", true);
                if (i == 4 || i == 9) {
                    String name;
                    if (i == 4) {
                        name = preferences.getString("GlobalSound", SettingsNotificationsActivity.this.getStringEntry(C0419R.string.Default));
                        if (name.equals("NoSound")) {
                            textViewDetail.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.NoSound));
                        } else {
                            textViewDetail.setText(name);
                        }
                    } else if (i == 9) {
                        name = preferences.getString("GroupSound", SettingsNotificationsActivity.this.getStringEntry(C0419R.string.Default));
                        if (name.equals("NoSound")) {
                            textViewDetail.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.NoSound));
                        } else {
                            textViewDetail.setText(name);
                        }
                    }
                    textView.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.Sound));
                    divider.setVisibility(4);
                } else if (i == 15) {
                    textView.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.ResetAllNotifications));
                    textViewDetail.setText(SettingsNotificationsActivity.this.getStringEntry(C0419R.string.UndoAllCustom));
                    divider.setVisibility(4);
                }
                if (i == 15 || enabledAll) {
                    if (VERSION.SDK_INT >= 11) {
                        textView.setAlpha(1.0f);
                        textViewDetail.setAlpha(1.0f);
                        divider.setAlpha(1.0f);
                    }
                    view.setEnabled(true);
                } else {
                    view.setEnabled(false);
                    if (VERSION.SDK_INT >= 11) {
                        textView.setAlpha(0.3f);
                        textViewDetail.setAlpha(0.3f);
                        divider.setAlpha(0.3f);
                    }
                }
            }
            return view;
        }

        public int getItemViewType(int i) {
            if (i == 0 || i == 5 || i == 10 || i == 14) {
                return 0;
            }
            if ((i <= 0 || i >= 4) && ((i <= 5 || i >= 9) && (i <= 10 || i >= 14))) {
                return 2;
            }
            return 1;
        }

        public int getViewTypeCount() {
            return 3;
        }

        public boolean isEmpty() {
            return false;
        }
    }

    class C08902 extends OnSwipeTouchListener {
        C08902() {
        }

        public void onSwipeRight() {
            SettingsNotificationsActivity.this.finishFragment(true);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.settings_layout, container, false);
            ListAdapter listAdapter = new ListAdapter(this.parentActivity);
            this.listView = (ListView) this.fragmentView.findViewById(C0419R.id.listView);
            this.listView.setAdapter(listAdapter);
            this.listView.setOnItemClickListener(new C05831());
            this.listView.setOnTouchListener(new C08902());
        } else {
            ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
            if (parent != null) {
                parent.removeView(this.fragmentView);
            }
        }
        return this.fragmentView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            Uri ringtone = (Uri) data.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            String name = null;
            if (!(ringtone == null || this.parentActivity == null)) {
                Ringtone rng = RingtoneManager.getRingtone(this.parentActivity, ringtone);
                if (rng != null) {
                    name = rng.getTitle(this.parentActivity);
                    rng.stop();
                }
            }
            Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0).edit();
            if (requestCode == 4) {
                if (name == null || ringtone == null) {
                    editor.putString("GlobalSound", "NoSound");
                    editor.putString("GlobalSoundPath", "NoSound");
                } else {
                    editor.putString("GlobalSound", name);
                    editor.putString("GlobalSoundPath", ringtone.toString());
                }
            } else if (requestCode == 9) {
                if (name == null || ringtone == null) {
                    editor.putString("GroupSound", "NoSound");
                    editor.putString("GroupSoundPath", "NoSound");
                } else {
                    editor.putString("GroupSound", name);
                    editor.putString("GroupSoundPath", ringtone.toString());
                }
            }
            editor.commit();
            this.listView.invalidateViews();
        }
    }

    public void applySelfActionBar() {
        if (this.parentActivity != null) {
            ActionBar actionBar = this.parentActivity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setSubtitle(null);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setCustomView(null);
            actionBar.setTitle(getStringEntry(C0419R.string.NotificationsAndSounds));
            TextView title = (TextView) this.parentActivity.findViewById(C0419R.id.action_bar_title);
            if (title == null) {
                title = (TextView) this.parentActivity.findViewById(this.parentActivity.getResources().getIdentifier("action_bar_title", "id", "android"));
            }
            if (title != null) {
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                title.setCompoundDrawablePadding(0);
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (!this.isFinish && getActivity() != null) {
            ((ApplicationActivity) this.parentActivity).showActionBar();
            ((ApplicationActivity) this.parentActivity).updateActionBar();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finishFragment();
                break;
        }
        return true;
    }
}
