package org.mistu.android.exam.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mistu.android.exam.R;
import org.mistu.android.exam.fragment.AdFragment;
import org.mistu.android.exam.fragment.IntroFragment;
import org.mistu.android.exam.util.PrefUtil;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener, IntroFragment.OnFragmentInteractionListener {

    private static final int FRAG_COUNT = 4;
    private static final int API_LEVEL = Build.VERSION.SDK_INT;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TextView skipTV;
    private TextView nextTV;
    private LinearLayout dotsContainer;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!PrefUtil.isFirstLaunch(this)) {
            goToHomeScreen();
            finish();
        }

        setContentView(R.layout.activity_intro);

        // Making notification bar transparent
        if (API_LEVEL >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        initVariables();
        setViewPager();
        changeStatusBarColor();
    }

    private void setViewPager() {
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        addBottomDots(0);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if (position == FRAG_COUNT -1 ) {
                nextTV.setText("START");
                skipTV.setVisibility(View.GONE);
            } else {
                nextTV.setText("NEXT");
                skipTV.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void addBottomDots(int position) {
        dots = new TextView[FRAG_COUNT];

        int[] colorsActive = getResources().getIntArray(R.array.intro_slider_colors);

        dotsContainer.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setTextSize(35);

            if (API_LEVEL >= 24) {
                dots[i].setText(Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY));
            }
            else {
                dots[i].setText(Html.fromHtml("&#8226;"));
            }

            if (API_LEVEL >= Build.VERSION_CODES.M) {
                dots[i].setTextColor(getResources().getColor(R.color.cardview_light_background, this.getTheme()));
            }
            else {
                dots[i].setTextColor(getResources().getColor(R.color.cardview_light_background));
            }


            dotsContainer.addView(dots[i]);
        }

        if (dots.length > 0) {
            if (API_LEVEL >= Build.VERSION_CODES.M) {
                dots[position].setTextColor(getResources().getColor(R.color.card_bg2, this.getTheme()));
            }
            else {
                dots[position].setTextColor(getResources().getColor(R.color.card_bg2));
            }
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void initVariables() {
        viewPager = (ViewPager) findViewById(R.id.activity_intro_view_pager);
        skipTV = (TextView) findViewById(R.id.activity_intro_skip);
        nextTV = (TextView) findViewById(R.id.activity_intro_next);
        dotsContainer = (LinearLayout) findViewById(R.id.activity_intro_dots_container);

        skipTV.setOnClickListener(this);
        nextTV.setOnClickListener(this);
    }

    private void goToHomeScreen() {
        PrefUtil.setFirstLaunch(this);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == nextTV.getId()) {
            int current = getItem(+1);
            if (current < FRAG_COUNT) {
                viewPager.setCurrentItem(current);
            } else {
                goToHomeScreen();
            }
        }
        else if (v.getId() == skipTV.getId()) {
            goToHomeScreen();
        }
    }

    @Override
    public void onIntroFragmentInteraction(int position) {

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return IntroFragment.newInstance(position, "");
        }

        @Override
        public int getCount() {
            return FRAG_COUNT;
        }

    }
}
