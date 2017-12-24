package org.cocos2dx.lua.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zuiai.nn.R;
import com.zuiai.nn.obj.WXAPIConst;

import static com.tencent.smtt.sdk.TbsReaderView.TAG;

public class LoginActivity extends BaseActivity  {

	private IWXAPI api;
	
	 @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.loginlayout);
	        new Handler().postDelayed(new Runnable() {
			 public void run() {
	                /* Create an Intent that will start the Main WordPress Activity. */
//	                Intent mainIntent = new Intent(LoginActivity.this, AppActivity.class);
//	                LoginActivity.this.startActivity(mainIntent);
//	                LoginActivity.this.finish();
			 }
		 }, 2000);
		 	init();
	    }

	private void init() {
		api = WXAPIFactory.createWXAPI(this, WXAPIConst.APP_ID, true);
		//注册appid
		api.registerApp(WXAPIConst.APP_ID);

		Button mLoginBtn= (Button) findViewById(R.id.btn_login);
		mLoginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// send oauth request
				final SendAuth.Req req = new SendAuth.Req();
				req.scope = "snsapi_userinfo";
				req.state = "none";
				boolean sendReq = api.sendReq(req);
				if(sendReq) {
					finish();
					Log.v(TAG,"sendReq  sendReq ---------------true");
				}
			}
		});
	}

}
