package org.cocos2dx.lua.service;

import com.zuiai.nn.obj.AccessTokenResult;
import com.zuiai.nn.obj.UpdateEntity;
import com.zuiai.nn.obj.UserEntity;
import com.zuiai.nn.obj.WeiChatUserInfo;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 功能
 * Created by Jiang on 2017/10/27.
 */

public interface CommonApi {

    //登录会员后台 http://web-dx.3600d.net:83/w9811088/api.php?name=xhxx_login&c1=10001&c2=111&c3=xhxxpwd&c4=1.0&c5=%E6%9C%BA%E5%99%A8%E7%A0%81&c6=1&c7=1,2,3
    @GET
    Observable<UserEntity> requestLogin(@Url String url, @QueryMap Map<String, String> querys);

    //下载更新 http://web-dx.3600d.net:83/w9811088/api.php?name=xhxx_gx&c1=10001
    @GET
    Observable<UpdateEntity > requestUpdate(@Url String url);


}
