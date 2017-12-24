package org.cocos2dx.lua.service;

/**
 * 功能
 * Created by Jiang on 2017/11/6.
 */

public interface UrlConnect {
    //wechatLogin
    String wechatGetAccessToken = "https://api.weixin.qq.com/sns/oauth2/access_token";
    String wechatGetUserInfo = "https://api.weixin.qq.com/sns/userinfo";
    String login = "http://web-dx.3600d.net:83/w9811088/api.php";
    String update = "http://web-dx.3600d.net:83/w9811088/api.php?name=xhxx_gx&c1=10001";
}
