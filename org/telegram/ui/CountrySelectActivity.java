package org.telegram.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Views.PinnedHeaderListView;
import org.telegram.ui.Views.SectionedBaseAdapter;

public class CountrySelectActivity extends ActionBarActivity {
    private HashMap<String, ArrayList<Country>> countries = new HashMap();
    private TextView epmtyTextView;
    private PinnedHeaderListView listView;
    private SectionedBaseAdapter listViewAdapter;
    private Timer searchDialogsTimer;
    private SupportMenuItem searchItem;
    private BaseAdapter searchListViewAdapter;
    public ArrayList<Country> searchResult;
    private SearchView searchView;
    private boolean searchWas;
    private boolean searching;
    private ArrayList<String> sortedCountries = new ArrayList();

    class C04881 implements Comparator<String> {
        C04881() {
        }

        public int compare(String lhs, String rhs) {
            return lhs.compareTo(rhs);
        }
    }

    class C04892 implements Comparator<Country> {
        C04892() {
        }

        public int compare(Country country, Country country2) {
            return country.name.compareTo(country2.name);
        }
    }

    class C04903 implements OnItemClickListener {
        C04903() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Country c;
            Intent intent;
            if (!CountrySelectActivity.this.searching || !CountrySelectActivity.this.searchWas) {
                int section = CountrySelectActivity.this.listViewAdapter.getSectionForPosition(i);
                int row = CountrySelectActivity.this.listViewAdapter.getPositionInSectionForPosition(i);
                if (section < CountrySelectActivity.this.sortedCountries.size()) {
                    ArrayList<Country> arr = (ArrayList) CountrySelectActivity.this.countries.get((String) CountrySelectActivity.this.sortedCountries.get(section));
                    if (row < arr.size()) {
                        c = (Country) arr.get(row);
                        intent = new Intent();
                        intent.putExtra("country", c.name);
                        CountrySelectActivity.this.setResult(-1, intent);
                        CountrySelectActivity.this.finish();
                    }
                }
            } else if (i < CountrySelectActivity.this.searchResult.size()) {
                c = (Country) CountrySelectActivity.this.searchResult.get(i);
                intent = new Intent();
                intent.putExtra("country", c.name);
                CountrySelectActivity.this.setResult(-1, intent);
                CountrySelectActivity.this.finish();
            }
        }
    }

    public static class Country {
        public String code;
        public String name;
        public String shortname;
    }

    private class SearchAdapter extends BaseAdapter {
        private Context mContext;

        public SearchAdapter(Context context) {
            this.mContext = context;
        }

        public boolean areAllItemsEnabled() {
            return true;
        }

        public boolean isEnabled(int i) {
            return true;
        }

        public int getCount() {
            if (CountrySelectActivity.this.searchResult == null) {
                return 0;
            }
            return CountrySelectActivity.this.searchResult.size();
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
            if (view == null) {
                view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.country_row_layout, viewGroup, false);
            }
            TextView textView = (TextView) view.findViewById(C0419R.id.settings_row_text);
            TextView detailTextView = (TextView) view.findViewById(C0419R.id.settings_row_text_detail);
            View divider = view.findViewById(C0419R.id.settings_row_divider);
            Country c = (Country) CountrySelectActivity.this.searchResult.get(i);
            textView.setText(c.name);
            detailTextView.setText("+" + c.code);
            if (i == CountrySelectActivity.this.searchResult.size() - 1) {
                divider.setVisibility(8);
            } else {
                divider.setVisibility(0);
            }
            return view;
        }

        public int getItemViewType(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public boolean isEmpty() {
            return CountrySelectActivity.this.searchResult == null || CountrySelectActivity.this.searchResult.size() == 0;
        }
    }

    class C08634 implements OnQueryTextListener {
        C08634() {
        }

        public boolean onQueryTextSubmit(String s) {
            return true;
        }

        public boolean onQueryTextChange(String s) {
            CountrySelectActivity.this.searchDialogs(s);
            if (s.length() != 0) {
                CountrySelectActivity.this.searchWas = true;
                if (CountrySelectActivity.this.listView != null) {
                    CountrySelectActivity.this.listView.setPadding(Utilities.dp(16), CountrySelectActivity.this.listView.getPaddingTop(), Utilities.dp(16), CountrySelectActivity.this.listView.getPaddingBottom());
                    CountrySelectActivity.this.listView.setAdapter(CountrySelectActivity.this.searchListViewAdapter);
                    if (VERSION.SDK_INT >= 11) {
                        CountrySelectActivity.this.listView.setFastScrollAlwaysVisible(false);
                    }
                    CountrySelectActivity.this.listView.setFastScrollEnabled(false);
                    CountrySelectActivity.this.listView.setVerticalScrollBarEnabled(true);
                }
                if (CountrySelectActivity.this.epmtyTextView != null) {
                    CountrySelectActivity.this.epmtyTextView.setText(CountrySelectActivity.this.getString(C0419R.string.NoResult));
                }
            }
            return true;
        }
    }

    class C08645 implements OnActionExpandListener {
        C08645() {
        }

        public boolean onMenuItemActionExpand(MenuItem menuItem) {
            CountrySelectActivity.this.getSupportActionBar().setIcon((int) C0419R.drawable.ic_ab_search);
            CountrySelectActivity.this.searching = true;
            return true;
        }

        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
            CountrySelectActivity.this.searchView.setQuery(BuildConfig.FLAVOR, false);
            CountrySelectActivity.this.searchDialogs(null);
            CountrySelectActivity.this.searching = false;
            CountrySelectActivity.this.searchWas = false;
            ViewGroup group = (ViewGroup) CountrySelectActivity.this.listView.getParent();
            CountrySelectActivity.this.listView.setAdapter(CountrySelectActivity.this.listViewAdapter);
            if (Utilities.isRTL) {
                CountrySelectActivity.this.listView.setPadding(Utilities.dp(30), CountrySelectActivity.this.listView.getPaddingTop(), Utilities.dp(16), CountrySelectActivity.this.listView.getPaddingBottom());
            } else {
                CountrySelectActivity.this.listView.setPadding(Utilities.dp(16), CountrySelectActivity.this.listView.getPaddingTop(), Utilities.dp(30), CountrySelectActivity.this.listView.getPaddingBottom());
            }
            if (VERSION.SDK_INT >= 11) {
                CountrySelectActivity.this.listView.setFastScrollAlwaysVisible(true);
            }
            CountrySelectActivity.this.listView.setFastScrollEnabled(true);
            CountrySelectActivity.this.listView.setVerticalScrollBarEnabled(false);
            CountrySelectActivity.this.applySelfActionBar();
            CountrySelectActivity.this.epmtyTextView.setText(CountrySelectActivity.this.getString(C0419R.string.ChooseCountry));
            return true;
        }
    }

    private class ListAdapter extends SectionedBaseAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public Object getItem(int section, int position) {
            return null;
        }

        public long getItemId(int section, int position) {
            return 0;
        }

        public int getSectionCount() {
            return CountrySelectActivity.this.sortedCountries.size();
        }

        public int getCountForSection(int section) {
            return ((ArrayList) CountrySelectActivity.this.countries.get((String) CountrySelectActivity.this.sortedCountries.get(section))).size();
        }

        public View getItemView(int section, int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.country_row_layout, parent, false);
            }
            TextView textView = (TextView) convertView.findViewById(C0419R.id.settings_row_text);
            TextView detailTextView = (TextView) convertView.findViewById(C0419R.id.settings_row_text_detail);
            View divider = convertView.findViewById(C0419R.id.settings_row_divider);
            ArrayList<Country> arr = (ArrayList) CountrySelectActivity.this.countries.get((String) CountrySelectActivity.this.sortedCountries.get(section));
            Country c = (Country) arr.get(position);
            textView.setText(c.name);
            detailTextView.setText("+" + c.code);
            if (position == arr.size() - 1) {
                divider.setVisibility(8);
            } else {
                divider.setVisibility(0);
            }
            return convertView;
        }

        public int getItemViewType(int section, int position) {
            return 0;
        }

        public int getItemViewTypeCount() {
            return 1;
        }

        public int getSectionHeaderViewType(int section) {
            return 0;
        }

        public int getSectionHeaderViewTypeCount() {
            return 1;
        }

        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.settings_section_layout, parent, false);
                convertView.setBackgroundColor(-328966);
            }
            ((TextView) convertView.findViewById(C0419R.id.settings_section_text)).setText(((String) CountrySelectActivity.this.sortedCountries.get(section)).toUpperCase());
            return convertView;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().getAssets().open("countries.txt")));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                ArrayList<Country> arr;
                String[] args = line.split(";");
                Country c = new Country();
                c.name = args[2];
                c.code = args[0];
                c.shortname = args[1];
                String n = c.name.substring(0, 1).toUpperCase();
                arr = (ArrayList) this.countries.get(n);
                if (arr == null) {
                    arr = new ArrayList();
                    this.countries.put(n, arr);
                    this.sortedCountries.add(n);
                }
                arr.add(c);
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
        Collections.sort(this.sortedCountries, new C04881());
        for (ArrayList<Country> arr2 : this.countries.values()) {
            Collections.sort(arr2, new C04892());
        }
        setContentView((int) C0419R.layout.country_select_layout);
        this.epmtyTextView = (TextView) findViewById(C0419R.id.searchEmptyView);
        this.searchListViewAdapter = new SearchAdapter(this);
        this.listView = (PinnedHeaderListView) findViewById(C0419R.id.listView);
        this.listView.setEmptyView(this.epmtyTextView);
        this.listView.setVerticalScrollBarEnabled(false);
        PinnedHeaderListView pinnedHeaderListView = this.listView;
        android.widget.ListAdapter listAdapter = new ListAdapter(this);
        this.listViewAdapter = listAdapter;
        pinnedHeaderListView.setAdapter(listAdapter);
        this.listView.setOnItemClickListener(new C04903());
        getWindow().setBackgroundDrawableResource(C0419R.drawable.transparent);
        getWindow().setFormat(4);
    }

    public void applySelfActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setCustomView(null);
        actionBar.setSubtitle(null);
        actionBar.setTitle(getString(C0419R.string.ChooseCountry));
        fixBackButton();
    }

    protected void onResume() {
        super.onResume();
        applySelfActionBar();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                if (this.searchItem != null && this.searchItem.isActionViewExpanded()) {
                    this.searchItem.collapseActionView();
                }
                finish();
                break;
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0419R.menu.contacts_menu, menu);
        this.searchItem = (SupportMenuItem) menu.findItem(C0419R.id.messages_list_menu_search);
        this.searchView = (SearchView) this.searchItem.getActionView();
        TextView textView = (TextView) this.searchView.findViewById(C0419R.id.search_src_text);
        if (textView != null) {
            textView.setTextColor(-1);
            try {
                Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
                mCursorDrawableRes.setAccessible(true);
                mCursorDrawableRes.set(textView, Integer.valueOf(C0419R.drawable.search_carret));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ImageView img = (ImageView) this.searchView.findViewById(C0419R.id.search_close_btn);
        if (img != null) {
            img.setImageResource(C0419R.drawable.ic_msg_btn_cross_custom);
        }
        this.searchView.setOnQueryTextListener(new C08634());
        this.searchItem.setSupportOnActionExpandListener(new C08645());
        return super.onCreateOptionsMenu(menu);
    }

    public void fixBackButton() {
        if (VERSION.SDK_INT == 19) {
            try {
                Class aClass = getSupportActionBar().getClass().getSuperclass();
                if (aClass != ActionBar.class) {
                    Field field = aClass.getDeclaredField("mActionBar");
                    field.setAccessible(true);
                    android.app.ActionBar bar = (android.app.ActionBar) field.get(getSupportActionBar());
                    field = bar.getClass().getDeclaredField("mActionView");
                    field.setAccessible(true);
                    View v = (View) field.get(bar);
                    field = v.getClass().getDeclaredField("mHomeLayout");
                    field.setAccessible(true);
                    ((View) field.get(v)).setVisibility(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void searchDialogs(final String query) {
        if (query == null) {
            this.searchResult = null;
            return;
        }
        try {
            if (this.searchDialogsTimer != null) {
                this.searchDialogsTimer.cancel();
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
        this.searchDialogsTimer = new Timer();
        this.searchDialogsTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    CountrySelectActivity.this.searchDialogsTimer.cancel();
                    CountrySelectActivity.this.searchDialogsTimer = null;
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
                CountrySelectActivity.this.processSearch(query);
            }
        }, 100, 300);
    }

    private void processSearch(final String query) {
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                if (query.trim().toLowerCase().length() == 0) {
                    CountrySelectActivity.this.updateSearchResults(new ArrayList());
                    return;
                }
                long time = System.currentTimeMillis();
                ArrayList<Country> resultArray = new ArrayList();
                ArrayList<Country> arr = (ArrayList) CountrySelectActivity.this.countries.get(query.substring(0, 1).toUpperCase());
                if (arr != null) {
                    Iterator i$ = arr.iterator();
                    while (i$.hasNext()) {
                        Country c = (Country) i$.next();
                        if (c.name.toLowerCase().startsWith(query)) {
                            resultArray.add(c);
                        }
                    }
                }
                CountrySelectActivity.this.updateSearchResults(resultArray);
            }
        });
    }

    private void updateSearchResults(final ArrayList<Country> arrCounties) {
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                CountrySelectActivity.this.searchResult = arrCounties;
                CountrySelectActivity.this.searchListViewAdapter.notifyDataSetChanged();
            }
        });
    }
}
