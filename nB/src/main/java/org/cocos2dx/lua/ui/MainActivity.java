package org.cocos2dx.lua.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.zuiai.nn.R;
import com.zuiai.nn.obj.DownLoadUtil;
import com.zuiai.nn.obj.UpdateEntity;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.service.Service;
import org.cocos2dx.lua.service.UrlConnect;
import org.cocos2dx.lua.ui.fragment.HomeWebFragment;
import org.cocos2dx.lua.ui.fragment.PersonFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by JIANG on 2017/9/16.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.navigation1)
    LinearLayout mNavigation1;
    @BindView(R.id.rb_niuying)
    RadioButton mRbNiuying;
    @BindView(R.id.rb_person)
    RadioButton mRbPerson;
    @BindView(R.id.rb)
    RadioGroup mRb;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    private Unbinder mUnbinder;
    private final String mAboutUrl = "file:///android_asset/about/index.html";
    private Fragment mHomeFragment;
    private Fragment mPersonFragment;
    private int currentTabIndex;
    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init() {
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        mRb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_niuying:
                        setCurrentTab(0);
                        break;
                    case R.id.rb_person:
                        setCurrentTab(1);
                        break;
                }
            }
        });

        mHomeFragment = new HomeWebFragment();
        mPersonFragment = new PersonFragment();
        fragments = new Fragment[]{mHomeFragment, mPersonFragment};
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mHomeFragment)
                .add(R.id.fragment_container, mPersonFragment)
                .hide(mPersonFragment)
                .show(mHomeFragment)
                .commit();
        setCurrentTab(0);
        currentTabIndex = 0;

        Service.getComnonService2().requestUpdate(UrlConnect.update)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(
                                APPAplication.instance,
                                "错误:" + throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(new Subscriber<UpdateEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                APPAplication.instance,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(UpdateEntity result) {
                        if (result.getBbh() > AppUtils.getAppVersionCode()) {

                            Toast.makeText(
                                    APPAplication.instance,
                                    "存在新版本，自动更新~~",
                                    Toast.LENGTH_SHORT).show();
                            if (result.getAddress() != null) {
                                DownLoadUtil downLoadUtil = new DownLoadUtil(MainActivity.this);
                                downLoadUtil.downloadAPK(result.getAddress(), "新版本");
                            }

                        }else {
                            Toast.makeText(
                                    APPAplication.instance,
                                    "已是最新版本~~",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setCurrentTab(int index) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(fragments[currentTabIndex])
                .show(fragments[index])
                .commit();
        currentTabIndex = index;
    }

}
