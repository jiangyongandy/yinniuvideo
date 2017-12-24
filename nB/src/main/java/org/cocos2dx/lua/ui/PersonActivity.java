package org.cocos2dx.lua.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test_webview_demo.utils.X5WebView;
import com.google.gson.Gson;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import org.cocos2dx.lua.VipHelperUtils;

public class PersonActivity extends BaseActivity  {
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private ViewGroup mViewParent;
    private ImageButton mBack;
    private ImageButton mForward;
    private ImageButton mExit;
    private ImageButton mHome;
    private ImageButton mMore;
    private Button mGo;
    private EditText mUrl;

    //	private static final String mHomeUrl = "http://www.youku.com/v_feelist/pt_1.html";
    private static final String mHomeUrl = "https://v.qq.com/";
    private static final String TAG = "SdkDemo";
    private static final int MAX_LENGTH = 14;
    private boolean mNeedTestPage = false;

    private final int disable = 120;
    private final int enable = 255;

    private ProgressBar mPageLoadingProgressBar = null;

    private ValueCallback<Uri> uploadFile;

    private String mIntentUrl;
    private TextView mChangeLine;
    private ViewGroup mViewMask;
    private View mAdView;
    private int resumeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                mIntentUrl = intent.getStringExtra("URL");
            } catch (NullPointerException e) {

            } catch (Exception e) {
            }
        }
        //
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }

		/*
         * getWindow().addFlags(
		 * android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
        setContentView(com.example.test_webview_demo.R.layout.activity_play);
        mViewParent = (ViewGroup) findViewById(com.example.test_webview_demo.R.id.webView1);
        mViewMask = (ViewGroup) findViewById(com.example.test_webview_demo.R.id.fl_mask);
        mAdView =  findViewById(com.example.test_webview_demo.R.id.iv_ad);
        mAdView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);

    }

    private void changGoForwardButton(WebView view) {
        if (view.canGoBack())
            mBack.setAlpha(enable);
        else
            mBack.setAlpha(disable);
        if (view.canGoForward())
            mForward.setAlpha(enable);
        else
            mForward.setAlpha(disable);
        if (view.getUrl() != null && view.getUrl().equalsIgnoreCase(mHomeUrl)) {
            mHome.setAlpha(disable);
            mHome.setEnabled(false);
        } else {
            mHome.setAlpha(enable);
            mHome.setEnabled(true);
        }
    }

    private void initProgressBar() {
        mPageLoadingProgressBar = (ProgressBar) findViewById(com.example.test_webview_demo.R.id.progressBar1);// new
        mChangeLine = (TextView) findViewById(com.example.test_webview_demo.R.id.tv_change_line);// new
        // ProgressBar(getApplicationContext(),
        // null,
        // android.R.attr.progressBarStyleHorizontal);
        mPageLoadingProgressBar.setMax(100);
        mPageLoadingProgressBar.setProgressDrawable(this.getResources()
                .getDrawable(com.example.test_webview_demo.R.drawable.color_progressbar));

        mChangeLine.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                VipHelperUtils.getInstance().changePlayLine(mWebView);
                mViewMask.setVisibility(View.VISIBLE);
            }
        });
    }

    private void init() {

        mWebView = new X5WebView(this, null);

        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));

        initProgressBar();

    /*    mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                *//*if(event.getAction() == MotionEvent.ACTION_DOWN) {

                    Toast.makeText(PlayActivity.this, "setOnTouchdianji", Toast.LENGTH_LONG).show();
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            View childAt = mWebView.getChildAt(0);
                            if(childAt != null) {
                                if(childAt instanceof  ViewGroup) {
                                    ViewGroup.LayoutParams layoutParams3 = childAt.getLayoutParams();
                                    layoutParams3.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                    layoutParams3.height = ViewGroup.LayoutParams.MATCH_PARENT;
                                    childAt.setLayoutParams(layoutParams3);
                                    View childAt1 = ((ViewGroup) childAt).getChildAt(0);
                                    if(childAt1 != null) {

                                        ViewGroup.LayoutParams layoutParams = childAt1.getLayoutParams();
                                        layoutParams.width = 500;
                                        layoutParams.height = 500;
                                        childAt1.setLayoutParams(layoutParams);
                                        if(childAt1 instanceof ViewGroup) {
                                            View childAt2 = ((ViewGroup) childAt).getChildAt(0);
                                            if(childAt2 != null) {

                                                ViewGroup.LayoutParams layoutParams2 = childAt2.getLayoutParams();
                                                layoutParams2.width = 400;
                                                layoutParams2.height = 400;
                                                childAt2.setLayoutParams(layoutParams);
                                                mViewMask.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }, 200);
                }
                return false;
            }
        });*/

        mWebView.addJavascriptInterface(new JSHook(), "jsHook");

        mWebView.setWebViewClient(new WebViewClient() {

            /**
             * 防止加载网页时调起系统浏览器
             */
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("onPageFinished", url);
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
                Log.i("onPageFinished", s);
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                Log.i("onPageFinished", s);
                super.onPageFinished(webView, s);
                // mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
                if(s.equals("file:///android_asset/about/index.html") && VipHelperUtils.getInstance().isWechatLogin()) {
                    Gson gson = new Gson();
                    String userInfoJson = gson.toJson(VipHelperUtils.getInstance().getUserInfo());
                    Log.i("uesrinfojson","-----------------" + userInfoJson);
                    webView.loadUrl("javascript: setcon(" + userInfoJson + ",'" + VipHelperUtils.getInstance().getLoginResult() + "')");
                }
                mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
                    changGoForwardButton(webView);
                }

            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                Log.i("onReceivedError", "code--"+i+"  "+ s + "  "+s1);
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                Log.i("WebResourceError", "code--"+webResourceError.getErrorCode()+ "  "+ webResourceError.getDescription());
            }

            @Override
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                Log.i("onReceivedHttpError", "code--"+webResourceResponse.getStatusCode()+"  "+ webResourceResponse.getReasonPhrase());
                VipHelperUtils.getInstance().changePlayLine(mWebView);
                mViewMask.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                Log.i("onReceivedSslError", "code--onReceivedSslErroronReceivedSslError");
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
                FrameLayout normalView = mWebView;
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
                if (mPageLoadingProgressBar == null)
                    return;
                if (i < 100) {
                    mPageLoadingProgressBar.setVisibility(View.VISIBLE);
                } else if (i >= 100) {
                    mPageLoadingProgressBar.setVisibility(View.GONE);
//                    invalidateOptionsMenu();
                }
                mPageLoadingProgressBar.setProgress(i);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
                TbsLog.d(TAG, "url: " + arg0);
                new AlertDialog.Builder(PersonActivity.this)
                        .setTitle("allow to download？")
                        .setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Toast.makeText(
                                                PersonActivity.this,
                                                "fake message: i'll download...",
                                                1000).show();
                                    }
                                })
                        .setNegativeButton("no",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                PersonActivity.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                PersonActivity.this,
                                                "fake message: refuse download...",
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
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();
        if (mIntentUrl == null) {
            mWebView.loadUrl(mHomeUrl);
        } else {
//            Toast.makeText(PlayActivity.this, "" + mIntentUrl, Toast.LENGTH_SHORT).show();
//            mWebView.loadUrl(VipHelperUtils.getInstance().getCurrentApi() + mIntentUrl);
//            mWebView.loadUrl("file:///android_asset/play.html");
            mWebView.loadUrl(mIntentUrl);
//            mWebView.loadUrl("http://hoooz.cn/v1/#");
            Log.i("vipvideo_url_pa", VipHelperUtils.getInstance().getCurrentApi() + mIntentUrl);
        }
        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    boolean[] m_selected = new boolean[]{true, true, true, true, false,
            false, true};

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resumeCount++;
//        Toast.makeText(PlayActivity.this, "onActivityResult, requestCode:    " + resumeCount, Toast.LENGTH_SHORT).show();
        if(resumeCount > 1)
            finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode
                + ",resultCode:" + resultCode);
        Toast.makeText(PersonActivity.this, "onActivityResult, requestCode:" + requestCode + ",resultCode:" + resultCode, Toast.LENGTH_SHORT).show();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (null != uploadFile) {
                        Uri result = data == null || resultCode != RESULT_OK ? null
                                : data.getData();
                        uploadFile.onReceiveValue(result);
                        uploadFile = null;
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (null != uploadFile) {
                uploadFile.onReceiveValue(null);
                uploadFile = null;
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null)
            return;
        mWebView.loadUrl(intent.getData().toString());
    }

    @Override
    protected void onDestroy() {
        if (mTestHandler != null)
            mTestHandler.removeCallbacksAndMessages(null);
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
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
        public void hideMask() {
            mViewMask.setVisibility(View.GONE);
        }

        @JavascriptInterface
        public void loadPlayPage() {
            mWebView.loadUrl("javascript:" +
                    "var itta = document.getElementById(\"iframe\");\n" +
                    "itta.src = '" +VipHelperUtils.getInstance().getCurrentApi() + mIntentUrl +"'");
        }

    }

}
