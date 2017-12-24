package org.cocos2dx.lua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.zuiai.nn.R;

import org.cocos2dx.lua.ui.fragment.GuideFragment;
import org.cocos2dx.lua.ui.fragment.GuideFragment2;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 功能
 * Created by Jiang on 2017/10/16.
 */

public class SplashActivity extends BaseActivity  {

    private int count;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();
    }

    private void initData() {
        ViewPager vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        GuideFragment guideFragment = new GuideFragment();
        GuideFragment2 guideFragment2 = new GuideFragment2();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(guideFragment);
        fragments.add(guideFragment2);
        vpGuide.setAdapter(new InnerPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"1", "2"}));
        final Handler handler = new Handler();
        final ViewPager finalVpGuide = vpGuide;
        final TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        finalVpGuide.setCurrentItem(count);
                        if(count == 2) {
                            timer.cancel();
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1500, 1500);
    }

    class InnerPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments = new ArrayList<>();
        private String[] titles;

        public InnerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
            // super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
