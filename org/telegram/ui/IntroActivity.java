package org.telegram.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.Utilities;

public class IntroActivity extends ActionBarActivity {
    private ViewGroup bottomPages;
    private int[] icons;
    private boolean justCreated = false;
    private int lastPage = 0;
    private int[] messages;
    private boolean startPressed = false;
    private int[] titles;
    private ImageView topImage1;
    private ImageView topImage2;
    private ViewPager viewPager;

    class C05222 implements OnClickListener {
        C05222() {
        }

        public void onClick(View view) {
            if (!IntroActivity.this.startPressed) {
                IntroActivity.this.startPressed = true;
                IntroActivity.this.startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                IntroActivity.this.finish();
            }
        }
    }

    class C08661 implements OnPageChangeListener {
        C08661() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int i) {
        }

        public void onPageScrollStateChanged(int i) {
            if ((i == 0 || i == 2) && IntroActivity.this.lastPage != IntroActivity.this.viewPager.getCurrentItem()) {
                ImageView fadeoutImage;
                ImageView fadeinImage;
                IntroActivity.this.lastPage = IntroActivity.this.viewPager.getCurrentItem();
                if (IntroActivity.this.topImage1.getVisibility() == 0) {
                    fadeoutImage = IntroActivity.this.topImage1;
                    fadeinImage = IntroActivity.this.topImage2;
                } else {
                    fadeoutImage = IntroActivity.this.topImage2;
                    fadeinImage = IntroActivity.this.topImage1;
                }
                fadeinImage.bringToFront();
                fadeinImage.setImageResource(IntroActivity.this.icons[IntroActivity.this.lastPage]);
                fadeinImage.clearAnimation();
                fadeoutImage.clearAnimation();
                Animation outAnimation = AnimationUtils.loadAnimation(IntroActivity.this, C0419R.anim.icon_anim_fade_out);
                outAnimation.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        fadeoutImage.setVisibility(8);
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                Animation inAnimation = AnimationUtils.loadAnimation(IntroActivity.this, C0419R.anim.icon_anim_fade_in);
                inAnimation.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                        fadeinImage.setVisibility(0);
                    }

                    public void onAnimationEnd(Animation animation) {
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                fadeoutImage.startAnimation(outAnimation);
                fadeinImage.startAnimation(inAnimation);
            }
        }
    }

    private class IntroAdapter extends PagerAdapter {
        private IntroAdapter() {
        }

        public int getCount() {
            return 7;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(container.getContext(), C0419R.layout.intro_view_layout, null);
            TextView headerTextView = (TextView) view.findViewById(C0419R.id.header_text);
            TextView messageTextView = (TextView) view.findViewById(C0419R.id.message_text);
            container.addView(view, 0);
            headerTextView.setText(IntroActivity.this.getString(IntroActivity.this.titles[position]));
            messageTextView.setText(Html.fromHtml(IntroActivity.this.getString(IntroActivity.this.messages[position])));
            return view;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            int count = IntroActivity.this.bottomPages.getChildCount();
            for (int a = 0; a < count; a++) {
                View child = IntroActivity.this.bottomPages.getChildAt(a);
                if (a == position) {
                    child.setBackgroundColor(-13851168);
                } else {
                    child.setBackgroundColor(-4473925);
                }
            }
        }

        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        public void finishUpdate(View arg0) {
        }

        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        public Parcelable saveState() {
            return null;
        }

        public void startUpdate(View arg0) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0419R.layout.intro_layout);
        if (Utilities.isRTL) {
            this.icons = new int[]{C0419R.drawable.intro7, C0419R.drawable.intro6, C0419R.drawable.intro5, C0419R.drawable.intro4, C0419R.drawable.intro3, C0419R.drawable.intro2, C0419R.drawable.intro1};
            this.titles = new int[]{C0419R.string.Page7Title, C0419R.string.Page6Title, C0419R.string.Page5Title, C0419R.string.Page4Title, C0419R.string.Page3Title, C0419R.string.Page2Title, C0419R.string.Page1Title};
            this.messages = new int[]{C0419R.string.Page7Message, C0419R.string.Page6Message, C0419R.string.Page5Message, C0419R.string.Page4Message, C0419R.string.Page3Message, C0419R.string.Page2Message, C0419R.string.Page1Message};
        } else {
            this.icons = new int[]{C0419R.drawable.intro1, C0419R.drawable.intro2, C0419R.drawable.intro3, C0419R.drawable.intro4, C0419R.drawable.intro5, C0419R.drawable.intro6, C0419R.drawable.intro7};
            this.titles = new int[]{C0419R.string.Page1Title, C0419R.string.Page2Title, C0419R.string.Page3Title, C0419R.string.Page4Title, C0419R.string.Page5Title, C0419R.string.Page6Title, C0419R.string.Page7Title};
            this.messages = new int[]{C0419R.string.Page1Message, C0419R.string.Page2Message, C0419R.string.Page3Message, C0419R.string.Page4Message, C0419R.string.Page5Message, C0419R.string.Page6Message, C0419R.string.Page7Message};
        }
        this.viewPager = (ViewPager) findViewById(C0419R.id.intro_view_pager);
        TextView startMessagingButton = (TextView) findViewById(C0419R.id.start_messaging_button);
        this.topImage1 = (ImageView) findViewById(C0419R.id.icon_image1);
        this.topImage2 = (ImageView) findViewById(C0419R.id.icon_image2);
        this.bottomPages = (ViewGroup) findViewById(C0419R.id.bottom_pages);
        this.topImage2.setVisibility(8);
        this.viewPager.setAdapter(new IntroAdapter());
        this.viewPager.setPageMargin(0);
        this.viewPager.setOffscreenPageLimit(1);
        this.viewPager.setOnPageChangeListener(new C08661());
        startMessagingButton.setOnClickListener(new C05222());
        this.justCreated = true;
        getSupportActionBar().hide();
    }

    protected void onResume() {
        super.onResume();
        if (this.justCreated) {
            if (Utilities.isRTL) {
                this.viewPager.setCurrentItem(6);
                this.lastPage = 6;
            } else {
                this.viewPager.setCurrentItem(0);
                this.lastPage = 0;
            }
            this.justCreated = false;
        }
    }
}
