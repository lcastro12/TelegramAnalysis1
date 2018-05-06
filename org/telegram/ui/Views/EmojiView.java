package org.telegram.ui.Views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.Emoji;
import org.telegram.ui.Views.PagerSlidingTabStrip.IconTabProvider;

public class EmojiView extends LinearLayout {
    private ArrayList<EmojiGridAdapter> adapters = new ArrayList();
    private int[] icons = new int[]{C0419R.drawable.ic_emoji_recent, C0419R.drawable.ic_emoji_smile, C0419R.drawable.ic_emoji_flower, C0419R.drawable.ic_emoji_bell, C0419R.drawable.ic_emoji_car, C0419R.drawable.ic_emoji_symbol};
    private Listener listener;
    private ViewPager pager;
    private FrameLayout recentsWrap;
    private ArrayList<GridView> views = new ArrayList();

    class C06041 implements OnClickListener {
        C06041() {
        }

        public void onClick(View paramAnonymousView) {
            if (EmojiView.this.listener != null) {
                EmojiView.this.listener.onBackspace();
            }
        }
    }

    class C06052 implements OnLongClickListener {
        C06052() {
        }

        public boolean onLongClick(View paramAnonymousView) {
            EmojiView.this.getContext().getSharedPreferences("emoji", 0).edit().clear().commit();
            return true;
        }
    }

    private class EmojiGridAdapter extends BaseAdapter {
        long[] data;

        class C06072 implements OnClickListener {
            C06072() {
            }

            public void onClick(View paramAnonymousView) {
                if (EmojiView.this.listener != null) {
                    EmojiView.this.listener.onEmojiSelected(EmojiView.this.convert(((Long) paramAnonymousView.getTag()).longValue()));
                }
                EmojiView.this.addToRecent(((Long) paramAnonymousView.getTag()).longValue());
            }
        }

        public EmojiGridAdapter(long[] arg2) {
            this.data = arg2;
        }

        public int getCount() {
            return this.data.length;
        }

        public Object getItem(int paramInt) {
            return null;
        }

        public long getItemId(int paramInt) {
            return this.data[paramInt];
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            ImageView localObject;
            if (paramView != null) {
                localObject = (ImageView) paramView;
            } else {
                localObject = new ImageView(EmojiView.this.getContext()) {
                    public void onMeasure(int paramAnonymousInt1, int paramAnonymousInt2) {
                        setMeasuredDimension(MeasureSpec.getSize(paramAnonymousInt1), MeasureSpec.getSize(paramAnonymousInt1));
                    }
                };
                localObject.setOnClickListener(new C06072());
                localObject.setBackgroundResource(C0419R.drawable.list_selector);
                localObject.setScaleType(ScaleType.CENTER);
            }
            localObject.setImageDrawable(Emoji.getEmojiBigDrawable(this.data[paramInt]));
            localObject.setTag(Long.valueOf(this.data[paramInt]));
            return localObject;
        }
    }

    public interface Listener {
        void onBackspace();

        void onEmojiSelected(String str);
    }

    private class EmojiPagesAdapter extends PagerAdapter implements IconTabProvider {
        private EmojiPagesAdapter() {
        }

        public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
            View localObject;
            if (paramInt == 0) {
                localObject = EmojiView.this.recentsWrap;
            } else {
                localObject = (View) EmojiView.this.views.get(paramInt);
            }
            paramViewGroup.removeView(localObject);
        }

        public int getCount() {
            return EmojiView.this.views.size();
        }

        public int getPageIconResId(int paramInt) {
            return EmojiView.this.icons[paramInt];
        }

        public Object instantiateItem(ViewGroup paramViewGroup, int paramInt) {
            View localObject;
            if (paramInt == 0) {
                localObject = EmojiView.this.recentsWrap;
            } else {
                localObject = (View) EmojiView.this.views.get(paramInt);
            }
            paramViewGroup.addView(localObject);
            return localObject;
        }

        public boolean isViewFromObject(View paramView, Object paramObject) {
            return paramView == paramObject;
        }
    }

    public EmojiView(Context paramContext) {
        super(paramContext);
        init();
    }

    public EmojiView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public EmojiView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private void addToRecent(long paramLong) {
        if (this.pager.getCurrentItem() != 0) {
            ArrayList<Long> localArrayList = new ArrayList();
            boolean was = false;
            for (long aCurrentRecent : Emoji.data[0]) {
                if (paramLong == aCurrentRecent) {
                    localArrayList.add(0, Long.valueOf(paramLong));
                    was = true;
                } else {
                    localArrayList.add(Long.valueOf(aCurrentRecent));
                }
            }
            if (!was) {
                localArrayList.add(0, Long.valueOf(paramLong));
            }
            Emoji.data[0] = new long[Math.min(localArrayList.size(), 50)];
            for (int q = 0; q < Emoji.data[0].length; q++) {
                Emoji.data[0][q] = ((Long) localArrayList.get(q)).longValue();
            }
            ((EmojiGridAdapter) this.adapters.get(0)).data = Emoji.data[0];
            ((EmojiGridAdapter) this.adapters.get(0)).notifyDataSetChanged();
            saveRecents();
        }
    }

    private String convert(long paramLong) {
        String str = BuildConfig.FLAVOR;
        for (int i = 0; i < 4; i++) {
            int j = (int) (65535 & (paramLong >> ((3 - i) * 16)));
            if (j != 0) {
                str = str + ((char) j);
            }
        }
        return str;
    }

    private void init() {
        setOrientation(1);
        for (long[] emojiGridAdapter : Emoji.data) {
            GridView localGridView = new GridView(getContext());
            localGridView.setColumnWidth(Emoji.scale(45.0f));
            localGridView.setNumColumns(-1);
            EmojiGridAdapter localEmojiGridAdapter = new EmojiGridAdapter(emojiGridAdapter);
            localGridView.setAdapter(localEmojiGridAdapter);
            this.adapters.add(localEmojiGridAdapter);
            this.views.add(localGridView);
        }
        setBackgroundDrawable(new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-14145496, ViewCompat.MEASURED_STATE_MASK}));
        this.pager = new ViewPager(getContext());
        this.pager.setAdapter(new EmojiPagesAdapter());
        PagerSlidingTabStrip tabs = new PagerSlidingTabStrip(getContext());
        tabs.setViewPager(this.pager);
        tabs.setShouldExpand(true);
        tabs.setIndicatorColor(-13388315);
        tabs.setIndicatorHeight(Emoji.scale(2.0f));
        tabs.setUnderlineHeight(Emoji.scale(2.0f));
        tabs.setUnderlineColor(1711276032);
        tabs.setTabBackground(0);
        LinearLayout localLinearLayout = new LinearLayout(getContext());
        localLinearLayout.setOrientation(0);
        localLinearLayout.addView(tabs, new LayoutParams(-1, -1, 1.0f));
        ImageView localImageView = new ImageView(getContext());
        localImageView.setImageResource(C0419R.drawable.ic_emoji_backspace);
        localImageView.setScaleType(ScaleType.CENTER);
        localImageView.setBackgroundResource(C0419R.drawable.bg_emoji_bs);
        localImageView.setOnClickListener(new C06041());
        localImageView.setOnLongClickListener(new C06052());
        localLinearLayout.addView(localImageView, new LayoutParams(Emoji.scale(61.0f), -1));
        this.recentsWrap = new FrameLayout(getContext());
        this.recentsWrap.addView((View) this.views.get(0));
        TextView localTextView = new TextView(getContext());
        localTextView.setText(C0419R.string.NoRecent);
        localTextView.setTextSize(18.0f);
        localTextView.setTextColor(-7829368);
        localTextView.setGravity(17);
        this.recentsWrap.addView(localTextView);
        ((GridView) this.views.get(0)).setEmptyView(localTextView);
        addView(localLinearLayout, new LayoutParams(-1, Emoji.scale(48.0f)));
        addView(this.pager);
        loadRecents();
    }

    private void saveRecents() {
        ArrayList<Long> localArrayList = new ArrayList();
        for (long valueOf : Emoji.data[0]) {
            localArrayList.add(Long.valueOf(valueOf));
        }
        getContext().getSharedPreferences("emoji", 0).edit().putString("recents", TextUtils.join(",", localArrayList)).commit();
    }

    public void loadRecents() {
        String str = getContext().getSharedPreferences("emoji", 0).getString("recents", BuildConfig.FLAVOR);
        String[] arrayOfString = null;
        if (str != null && str.length() > 0) {
            arrayOfString = str.split(",");
            Emoji.data[0] = new long[arrayOfString.length];
        }
        if (arrayOfString != null) {
            for (int i = 0; i < arrayOfString.length; i++) {
                Emoji.data[0][i] = Long.parseLong(arrayOfString[i]);
            }
            ((EmojiGridAdapter) this.adapters.get(0)).data = Emoji.data[0];
            ((EmojiGridAdapter) this.adapters.get(0)).notifyDataSetChanged();
        }
    }

    public void onMeasure(int paramInt1, int paramInt2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(paramInt1), 1073741824), MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(paramInt2), 1073741824));
    }

    public void setListener(Listener paramListener) {
        this.listener = paramListener;
    }

    public void invalidateViews() {
        Iterator i$ = this.views.iterator();
        while (i$.hasNext()) {
            GridView gridView = (GridView) i$.next();
            if (gridView != null) {
                gridView.invalidateViews();
            }
        }
    }
}
