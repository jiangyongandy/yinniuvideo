package org.cocos2dx.lua.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.test_webview_demo.utils.X5WebView;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;
import com.zuiai.nn.R;
import com.zuiai.nn.obj.UserEntity;
import com.zuiai.nn.obj.WeiChatUserInfo;
import com.zuiai.nn.wxapi.WXEntryActivity;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.service.Service;
import org.cocos2dx.lua.service.UrlConnect;
import org.cocos2dx.lua.ui.BrowserActivity;
import org.cocos2dx.lua.ui.HomeActivity;
import org.cocos2dx.lua.ui.PlayActivity;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static org.cocos2dx.lua.APPAplication.api;


public class HomeWebFragment extends BaseFragment {
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private ViewGroup mViewParent;
    private EditText mUrl;

    private final String mAboutUrl = "https://555.cocos4dx.com/niu";
    //    private final String mHomeUrl = "http://dwjzywapp.com/appindex.php";
    private static final String TAG = "SdkDemo";
    private static final int MAX_LENGTH = 14;
    private boolean mNeedTestPage = false;

    private final int disable = 120;
    private final int enable = 255;

    private ValueCallback<Uri> uploadFile;

    private void init() {

        mWebView = new X5WebView(mActivity, null);

        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));

        mWebView.addJavascriptInterface(new JSHook(), "jsHook");

        mWebView.setWebViewClient(new WebViewClient() {

            /**
             * 防止加载网页时调起系统浏览器
             */
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("should", url);
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return false;
                } else {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
                    return true;
                }
            }

            @Override
            public void onPageStarted(final WebView webView, String s, Bitmap bitmap) {
                Log.i("onPageStarted", s);
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(final WebView webView, String s) {
                Log.i("onPageFinished", s);
                super.onPageFinished(webView, s);
//				Toast.makeText(getApplicationContext(), "页面加载完成", Toast.LENGTH_SHORT).show();
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            View myVideoView;
            View myNormalView;
            CustomViewCallback callback;

            // /////////////////////////////////////////////////////////
            //

            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view,
                                         CustomViewCallback customViewCallback) {
                FrameLayout normalView = (FrameLayout) mActivity.findViewById(com.example.test_webview_demo.R.id.web_filechooser);
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
            }

            @Override
            public void onHideCustomView() {
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }

            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2,
                                     JsResult arg3) {
                /**
                 * 这里写入你自定义的window alert
                 */
                return super.onJsAlert(null, arg1, arg2, arg3);
            }

            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
                TbsLog.d(TAG, "url: " + arg0);
                new AlertDialog.Builder(mActivity)
                        .setTitle("允许下载吗？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Toast.makeText(
                                                mActivity,
                                                "准备下载...",
                                                1000).show();
                                    }
                                })
                        .setNegativeButton("否",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                mActivity,
                                                "拒绝下载...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                mActivity,
                                                "取消下载...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
            }
        });

        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setAllowFileAccessFromFileURLs(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(mActivity.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(mActivity.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(mActivity.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();
        mWebView.loadUrl(mAboutUrl);
        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(mActivity);
        CookieSyncManager.getInstance().sync();

    }


    boolean[] m_selected = new boolean[]{true, true, true, true, false,
            false, true};

    @Override
    protected void initData() {
        mViewParent = (ViewGroup) mRootView.findViewById(com.example.test_webview_demo.R.id.webView1);

        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_person;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroyView() {
        if (mTestHandler != null)
            mTestHandler.removeCallbacksAndMessages(null);
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroyView();
    }

    public static final int MSG_OPEN_TEST_URL = 0;
    public static final int MSG_INIT_UI = 1;
    private final int mUrlStartNum = 0;
    private int mCurrentUrl = mUrlStartNum;

    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_TEST_URL:
                    if (!mNeedTestPage) {
                        return;
                    }

                    String testUrl = "file:///sdcard/outputHtml/html/"
                            + Integer.toString(mCurrentUrl) + ".html";
                    if (mWebView != null) {
                        mWebView.loadUrl(testUrl);
                    }

                    mCurrentUrl++;
                    break;
                case MSG_INIT_UI:
                    init();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public class JSHook {

        @JavascriptInterface
        public void goToPlayPage(String url) {
            Intent intent = new Intent(getActivity(), PlayActivity.class);
            intent.putExtra("URL", url);
            VipHelperUtils.getInstance().setCurrentPlayUrl(url);
            HomeWebFragment.this.startActivity(intent);
        }

        @JavascriptInterface
        public void go2Site(int position, String apiString) {
            VipHelperUtils.getInstance().setCurrentApi(apiString);
            Log.i("jiamizifuchuan","-------------"+apiString + " --------position:" + position);
            VipHelperUtils.getInstance().changeCurrentSite(position);

            if(CommonConstant.buildConfig.isDebug) {
                Intent intent = new Intent(getActivity(), BrowserActivity.class);
                getActivity().startActivity(intent);
                return;
            }

            if (VipHelperUtils.getInstance().isValidVip()) {
                VipHelperUtils.getInstance().changeCurrentSite(position);
                Intent intent = new Intent(APPAplication.instance, BrowserActivity.class);
                getActivity().startActivity(intent);

            }else if(VipHelperUtils.getInstance().isWechatLogin()) {

                if(VipHelperUtils.getInstance().getCurrentPosition() == 8 || VipHelperUtils.getInstance().getCurrentPosition() == 13 || VipHelperUtils.getInstance().getCurrentPosition() == 16) {
                    Intent intent = new Intent(getActivity(), BrowserActivity.class);
                    getActivity().startActivity(intent);
                    return;
                }

                Toast.makeText(
                        getActivity(),
                        "Vip已经过期啦，需要重新激活哦",
                        Toast.LENGTH_SHORT).show();

            } else {

                new AlertDialog.Builder(getActivity())
                        .setTitle("需要登陆微信")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // send oauth request
                                        final SendAuth.Req req = new SendAuth.Req();
                                        req.scope = "snsapi_userinfo";
                                        req.state = "none";
                                        boolean sendReq = api.sendReq(req);
                                        if (sendReq) {
                                            Log.v(TAG, "sendReq  sendReq ---------------true");
                                        }
                                    }
                                })
                        .setNegativeButton("否",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                getActivity(),
                                                "拒绝登录无法观看...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                getActivity(),
                                                "取消...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).show();

            }
        }

        @JavascriptInterface
        public void paySuccess(String result) {
            Log.i("充值回调接口", "----------------" + result);
            WeiChatUserInfo userInfo = VipHelperUtils.getInstance().getUserInfo();
            HashMap<String, String> map = new HashMap<>();
            map.put("name", "xhxx_login");
            map.put("c1", "10001");
            map.put("c2", userInfo.getUnionid());
            map.put("c3", "xhxxpwd");
            map.put("c4", "1.1");
            map.put("c5", userInfo.getOpenid());
            map.put("c6", "1");
            map.put("c7", "1");
            Service.getComnonService2().requestLogin(UrlConnect.login, map)
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
                    .subscribe(new Subscriber<UserEntity>() {
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
                        public void onNext(UserEntity result) {
                            VipHelperUtils.getInstance().setLoginResult(result.getMsg());
                            VipHelperUtils.getInstance().setWechatLogin(true);
                            if (result.getVip().equals("yes")) {
                                VipHelperUtils.getInstance().setValidVip(true);
                            } else {
                                Toast.makeText(
                                        APPAplication.instance,
                                        result.getMsg(),
                                        Toast.LENGTH_SHORT).show();
                                VipHelperUtils.getInstance().setValidVip(false);
                            }
                        }
                    });
        }

    }

}
