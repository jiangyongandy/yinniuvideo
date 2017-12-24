package org.cocos2dx.lua.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zuiai.nn.R;


/**
 * Created by JIANG on 2017/1/5.
 */

public class BrowserActivity1 extends Activity
{

/*    @BindView(R.id.pgb_wv)
    ProgressBar progressBar;*/

    WebView mWebView;

    private final Handler mHandler = new Handler();

    private String url, mTitle;

    private WebViewClientBase webViewClient = new WebViewClientBase();
    private WebChromeClientBase webChromeViewClient = new WebChromeClientBase();

//webview 不能有//注释？
    private String fun1 =
            "var  elementName=\"=====\";\n" +
                    "\t\tfunction countTotalElement(node)\n" +
                    "\t   {\n" +
                    "\t\t\tvar total=0;\n" +
                    "\t\t\tif(node.nodeType==1)\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\ttotal++;\n" +
                    "\t\t\t\telementName=elementName+node.id+\"\\r\\n\";\n" +
                    "\n" +
                    "\t\t\t}\n" +
                    "\n" +
                    "\t\t\tvar childrens=node.childNodes;\n" +
                    "\t\t\tfor(var i=0;i<childrens.length;i++)\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\ttotal+=countTotalElement(childrens[i]);\n" +
                    "\t\t\t} \n" +
                    "\t\t\treturn total;\n" +
                    "\t   }\n" +
                    "\t   alert('标记总数'+countTotalElement(document)+'\\r\\n 全部标记如下:\\r\\n'+elementName);"
            ;
    private String fun2 =
            "var  elementName=\"=====\";\n" +
                    "\t\tfunction countTotalElement(node)\n" +
                    "\t   {\n" +
                    "\t\t\tvar total=0;\n" +
                    "\t\t\tif(node.nodeType==1)\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\ttotal++;\n" +
                    "\t\t\t\telementName=elementName+node.tagName+\"\\r\\n\";\n" +
                    "\n" +
                    "\t\t\t}\n" +
                    "\n" +
                    "\t\t\tvar childrens=node.childNodes;\n" +
                    "\t\t\tfor(var i=0;i<childrens.length;i++)\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\ttotal+=countTotalElement(childrens[i]);\n" +
                    "\t\t\t} \n" +
                    "\t\t\treturn total;\n" +
                    "\t   }\n" +
                    "\t   alert('标记总数'+countTotalElement(document)+'\\r\\n 全部标记如下:\\r\\n'+elementName);"
            ;

    private String fun3 =
            "\t\tvar player = document.getElementById(\"player\");\n" +
                    "\t\t//image[0].innerHTML=\"<img  src='http://www.runoob.com/images/pulpit.jpg' />\" \n" +
                    "\t\tvar parent = player.parentNode;\n" +
                    "\t\tvar div = document.createElement('img');\n" +
                    "\t\tdiv.innerHtml = player.innerHtml;\n" +
                    "\t\tdiv.id = player.id;\n" +
                    "\t\tdiv.src = \"http://www.runoob.com/images/pulpit.jpg\";\n" +
                    "\t\tdiv.width = 990;\n" +
                    "\t\tdiv.height = 200;\n" +
                    "\t\tdiv.onclick = function (){\n" +
                    "\t\t\tconsole.log(\"jjskdfsdfdsjfkdsfj\"); \n" +
                    "window.location.href=\"http://hoooz.cn/v/?url=\" + window.location.href;"+
                    "\t\t}\n" +
                    "\t\tparent.removeChild(player);\n" +
                    "\t\tparent.appendChild(div);";

/*    public static void launch(Activity activity, String url, String title)
    {

        Intent intent = new Intent(activity, BrowserActivity1.class);
        intent.putExtra(Constants.EXTRA_URL, url);
        intent.putExtra(Constants.EXTRA_TITLE, title);
        activity.startActivity(intent);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initViews(savedInstanceState);
    }

    public void initViews(Bundle savedInstanceState)
    {

/*        Intent intent = getIntent();
        if (intent != null)
        {
            url = intent.getStringExtra(Constants.EXTRA_URL);
            mTitle = intent.getStringExtra(Constants.EXTRA_TITLE);
        }*/

        initToolBar();
        setupWebView();

    }

    public void initToolBar()
    {
        setTitle(TextUtils.isEmpty(mTitle) ? "详情" : mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

//        getMenuInflater().inflate(R.menu.menu_browser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        /*switch (item.getItemId())
        {
            case R.id.menu_open:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;

            case R.id.menu_copy:
                ClipboardUtil.setText(BrowserActivity1.this, url);
                showToast("已复制");
                break;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Deprecated
    private void share()
    {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "来自「哔哩哔哩」的分享:" + url);
        startActivity(Intent.createChooser(intent, mTitle));

    }


    @Override
    public void onBackPressed()
    {

        if (mWebView.canGoBack())
        {
            mWebView.goBack();
        } else
        {
            finish();
        }
        finish();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView()
    {

//        progressBar.spin();
        mWebView = (WebView) findViewById(R.id.wv);
        final WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
//        mWebView.getSettings().setBlockNetworkImage(true);
        mWebView.setWebViewClient(webViewClient);
        mWebView.requestFocus(View.FOCUS_DOWN);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.setWebChromeClient(webChromeViewClient);
        mWebView.loadUrl("http://film.qq.com/");
    }

    public class WebChromeClientBase extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result)
        {

            AlertDialog.Builder b2 = new AlertDialog
                    .Builder(BrowserActivity1.this)
                    .setTitle(R.string.app_name)
                    .setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            result.confirm();
                        }
                    });

            b2.setCancelable(false);
            b2.create();
            b2.show();
            return true;
        }

        public void onProgressChanged(WebView view, int progress) {
/*            if(progressBar == null)
                return;
            if (progress < 100) {
                progressBar.setVisibility(View.VISIBLE);
            } else if (progress == 100) {
                progressBar.setVisibility(View.GONE);
//                    invalidateOptionsMenu();
            }
            progressBar.setProgress(progress);*/

            super.onProgressChanged(view, progress);
        }

    }

    public class WebViewClientBase extends WebViewClient
    {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(url.startsWith("http:") || url.startsWith("https:") ) {
                view.loadUrl(url);
                return false;
            }else{
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
                return true;
            }
/*            try {
                if(url.startsWith("weixin://") //微信
                        || url.startsWith("alipays://") //支付宝
                        || url.startsWith("mailto://") //邮件
                        || url.startsWith("tel://")//电话
                        || url.startsWith("dianping://")//大众点评
                    //其他自定义的scheme
                        ) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
                    return true;
                }
            } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
            }
            view.loadUrl(url);
            return true;*/

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {

            super.onPageStarted(view, url, favicon);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:" + fun3);
                }
            }, 3000);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {

            super.onPageFinished(view, url);
            Toast.makeText(getApplicationContext(), "页面加载完成", Toast.LENGTH_SHORT).show();
//            mWebView.loadUrl("javascript:" + fun1);
            mWebView.loadUrl("javascript:" + fun3);
//            mWebView.loadUrl("javascript:" + fun1);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
        {

            super.onReceivedError(view, request, error);
            String errorHtml = "<html><body><h2>找不到网页</h2></body></html>";
            view.loadDataWithBaseURL(null, errorHtml, "text/html", "UTF-8", null);
        }
    }

    @Override
    protected void onPause()
    {

        mWebView.reload();
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {

        mWebView.destroy();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
