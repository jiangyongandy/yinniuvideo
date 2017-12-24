package org.cocos2dx.lua.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cocos2dx.lua.ui.BaseActivity;


/**
 * 功能
 * Created by Jiang on 2017/11/27.
 */

public abstract class BaseFragment extends Fragment {

    protected View mRootView;
    protected BaseActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getRootViewLayoutId() == 0)
            throw new RuntimeException("getRootViewLayoutId is null");
        else {
            mRootView = inflater.inflate(getRootViewLayoutId(), container, false);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        initData();
    }

    protected abstract void initData();

    protected abstract int getRootViewLayoutId();
}
