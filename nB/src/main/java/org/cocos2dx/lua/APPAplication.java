package org.cocos2dx.lua;

import android.app.Application;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;
import com.umeng.analytics.MobclickAgent;
import com.zuiai.nn.obj.WXAPIConst;

public class APPAplication extends Application {

 	public static APPAplication instance;
	public boolean qbsdkIsInit = false;
	public QbInitListener  qbInitListener;
	public static IWXAPI api = null;

	@Override
	public void onCreate() {
		super.onCreate();
		instance=  this;
		//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
		
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
			
			@Override
			public void onViewInitFinished(boolean arg0) {
				qbsdkIsInit = true;
				if(qbInitListener != null) {
					qbInitListener.onQbInit();
				}
				//x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
				Log.d("app", " onViewInitFinished is " + arg0);
			}
			
			@Override
			public void onCoreInitFinished() {
				Log.d("app", " onCoreInitFinished " );
			}
		};
		//x5内核初始化接口
		QbSdk.initX5Environment(getApplicationContext(),  cb);
		//友盟
		MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
		MobclickAgent.setDebugMode( true);
		//上报内核错误信息
//		MobclickAgent.reportError(this, WebView.getCrashExtraMessage(this));
		Log.e("app", "x5内核错误信息------------------"+WebView.getCrashExtraMessage(this) );
		//注册微信
		api = WXAPIFactory.createWXAPI(this, WXAPIConst.APP_ID, true);
		//注册appid
		api.registerApp(WXAPIConst.APP_ID);
		Utils.init(instance);
	}

	public interface QbInitListener {
		void onQbInit();
	}

}
