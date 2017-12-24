package com.zuiai.nn.wxapi;

//import org.cocos2dx.lua.AppActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.zuiai.nn.obj.AccessTokenResult;
import com.zuiai.nn.obj.HttpUtil;
import com.zuiai.nn.obj.UserEntity;
import com.zuiai.nn.obj.WXAPIConst;
import com.zuiai.nn.obj.WeiChatUserInfo;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.service.Service;
import org.cocos2dx.lua.service.UrlConnect;
import org.cocos2dx.lua.ui.BrowserActivity;

import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static org.cocos2dx.lua.APPAplication.api;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private String TAG = "WXEntryActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "WXEntryActivity  onCreate ---------");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.v("WeiChatLogin", "onReq++++++++++++");
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.v(TAG, "onResp-------------" + resp.getType());
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
                    String code = ((SendAuth.Resp) resp).code; // 这里的code就是接入指南里要拿到的code
                    Log.v("tiantianniu", "this is WXLogin callBack .... " + code);
//	                GetWeChatToken(code);
//	      	        AppActivity.CallFunction(code);
                    //这里写获取到code之后的事件
//                    Toast.makeText(WXEntryActivity.this, "微信登陆成功,可以愉快观看了", Toast.LENGTH_LONG).show();
                    GetWeChatToken(code);

//                    Intent intent = new Intent(this, HomeActivity.class);
//                    startActivity(intent);
                } else if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
//	            AppActivity.CallFunction("-2");
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//	            AppActivity.CallFunction("-4");
                finish();
                break;
            default:
                Log.v(TAG, "login--unknown---");
                finish();
                break;
        }

    }

    private void GetWeChatToken(String code) {

        Service.getWeCHatService().getAccessToken(UrlConnect.wechatGetAccessToken, WXAPIConst.APP_ID, WXAPIConst.AppSecret, code, "authorization_code")
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<AccessTokenResult, Observable<WeiChatUserInfo>>() {
                    @Override
                    public Observable<WeiChatUserInfo> call(AccessTokenResult accessTokenResult) {
                        return Service.getWeCHatService().getUserInfo(UrlConnect.wechatGetUserInfo, accessTokenResult.getAccess_token(), accessTokenResult.getOpenid());
                    }
                })
                .flatMap(new Func1<WeiChatUserInfo, Observable<UserEntity >>() {
                    @Override
                    public Observable<UserEntity > call(WeiChatUserInfo userInfo) {
                        VipHelperUtils.getInstance().setUserInfo(userInfo);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", "xhxx_login");
                        map.put("c1", "10001");
                        map.put("c2", userInfo.getUnionid());
                        map.put("c3", "xhxxpwd");
                        map.put("c4", "1.1");
                        map.put("c5", userInfo.getOpenid());
                        map.put("c6", "1");
                        map.put("c7", "1");
                        return Service.getComnonService2().requestLogin(UrlConnect.login, map);
                    }
                })
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
                .subscribe(new Subscriber<UserEntity >() {
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
                    public void onNext(UserEntity  result) {
                        VipHelperUtils.getInstance().setLoginResult(result.getMsg());
                        Toast.makeText(
                                APPAplication.instance,
                                "登陆成功~~",
                                Toast.LENGTH_SHORT).show();
                        VipHelperUtils.getInstance().setWechatLogin(true);
                        if(VipHelperUtils.getInstance().getCurrentPosition() == 8 || VipHelperUtils.getInstance().getCurrentPosition() == 13 || VipHelperUtils.getInstance().getCurrentPosition() == 16) {
                            Intent intent = new Intent(WXEntryActivity .this, BrowserActivity.class);
                            WXEntryActivity  .this.startActivity(intent);
                            finish();//必须要有，用于点击返回游戏的时候不会留在微信
                            return;
                        }
                        if(result.getVip().equals("yes")) {
                            //没有到期才能观看
                            Intent intent = new Intent(WXEntryActivity .this, BrowserActivity.class);
                            WXEntryActivity  .this.startActivity(intent);
                            VipHelperUtils.getInstance().setValidVip(true);
                        }else {
                            Toast.makeText(
                                    APPAplication.instance,
                                    result.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                            VipHelperUtils.getInstance().setValidVip(false);
                        }
                        finish();//必须要有，用于点击返回游戏的时候不会留在微信
                    }
                });

    }

    public static void WeChatReflushTokenLogin(String token) {
        String url_constant1 = HttpUtil.urlConnection("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" +
                WXAPIConst.APP_ID + "&grant_type=refresh_token&refresh_token=" + token, "", null, null);
        AccessTokenResult result = JSON.parseObject(url_constant1, AccessTokenResult.class);

        if (result.getErrcode() != 0) {
//	    	Toast.makeText(AppActivity.self, result.getErrcode()+"__"+result.getErrmsg(), Toast.LENGTH_LONG).show();
//	    	AppActivity.CallFunction("-2");
            return;
        }

//		AppActivity.self.SaveAccessToken(result.getRefresh_token());

        String url_constant2 = HttpUtil.urlConnection("https://api.weixin.qq.com/sns/userinfo?access_token="
                + result.getAccess_token() + "&openid=" + result.getOpenid(), "", null, null);
        Object ob = JSON.parse(url_constant2);

//        AppActivity.CallFunction(ob.toString());
    }

}
