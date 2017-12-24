package org.cocos2dx.lua;

import com.zuiai.nn.R;

/**
 * 功能
 * Created by Jiang on 2017/11/6.
 */

public interface CommonConstant {

    interface buildConfig {
        /* 是否是debug模式 */
        boolean isDebug = APPAplication.instance.getResources().getBoolean(R.bool.is_debug);
    }

}
