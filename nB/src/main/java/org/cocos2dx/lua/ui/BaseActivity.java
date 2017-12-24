package org.cocos2dx.lua.ui;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class BaseActivity extends FragmentActivity {

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
