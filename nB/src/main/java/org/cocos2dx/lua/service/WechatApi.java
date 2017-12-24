package org.cocos2dx.lua.service;

import com.zuiai.nn.obj.AccessTokenResult;
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

public interface WechatApi {

    //wechat通过code获取access_token的接口。
    @GET
    Observable<AccessTokenResult> getAccessToken(@Url String url, @Query("appid") String appid,  @Query("secret") String secret, @Query("code") String code, @Query("grant_type") String grantType);

    //wechat获取用户个人信息（UnionID机制）
    @GET
    Observable<WeiChatUserInfo> getUserInfo(@Url String url, @Query("access_token") String token,  @Query("openid") String openid);


}
