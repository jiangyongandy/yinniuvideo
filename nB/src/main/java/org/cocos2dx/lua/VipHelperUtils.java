package org.cocos2dx.lua;

import android.content.Context;
import android.util.Log;

import com.tencent.smtt.sdk.WebView;
import com.zuiai.nn.R;
import com.zuiai.nn.obj.WeiChatUserInfo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by JIANG on 2017/9/16.
 */

public class VipHelperUtils {

    public static VipHelperUtils instance;

    //接口地址
    public static String api1 = "https://api.47ks.com/webcloud/?v=";
    public static String api2 = "http://hoooz.cn/v/?url=";
    public static String api3 = "http://p2.api.47ks.com/webcloud/?v=";
    public static String api4 = "http://000o.cc/jx/ty.php?url=";
    public static String api5 = "http://www.wmxz.wang/video.php?url=";
    public static String api6 = "http://www.ou522.cn/t2/1.php?url=";
    public static String api7 = "http://2gty.com/apiurl/yun.php?url=";
    public static String api8 = "http://api.pucms.com/index.php?url=";
    public static String api9 = "http://www.sfsft.com/video.php?url=";
    public static String api10 = "http://api.wlzhan.com/sudu/?url=";
    public static String apiYouKu = "http://at520.cn/atjx/jiexi.php?url=";
    public static String noAdUrl = "http://api.baiyug.cn/vip/index.php?url=";
    private final Random random = new Random();

    private String[] apis = {api1, api2, api3, api4, api5, api6, api7, api8, api9, api10, apiYouKu, noAdUrl};
    /*
    *            腾讯                 土豆              芒果                  SOUHU
    * pc         container_player     td-player         c-player-video      player-content player
    * mobile       vip_player           player          video-area          player-view top-poster
    *
    *
    * 修改了 支持的影视数目 同时注意修改changePlayURLbyPositon
    * */
    private String[] ids = {"j-player", "vip_player", "m-video-player", "player", "td-player", "c-player-video"
            , "player", "", "", "ws_play relative", "player_section"
            , "playerbox", "", "", "","",""};

    private String[] names = {"乐视TV视频", "腾讯视频", "爱奇艺视频", "优酷视频", "土豆视频", "芒果TV视频",
            "搜狐视频", "Ac弹幕网", "哔哩哔哩", "WASU华数视频", "1905电影网视频",
            "PPTV聚力", "糖豆视频", "音悦台MV", "虎牙视频", "",
            "影牛","56视频"

    };

    private String[] urls = {"http://vip.le.com/", "http://film.qq.com/", "http://vip.iqiyi.com/", "http://vip.youku.com/", "http://www.tudou.com/category", "http://www.mgtv.com/vip/",
            "https://film.sohu.com/", "http://www.acfun.tv/", "http://www.bilibili.com/", "http://www.wasu.cn/", "http://www.1905.com/",
            "http://www.pptv.com/", "http://tv.tangdou.com/", "http://www.yinyuetai.com/", "http://v.huya.com/",
            "http://facai.cocos4dx.com/","http://www.56.com/"
    };

    private int[] icons = {R.drawable.letvlogo, R.drawable.qqlogo, R.drawable.iqiyi, R.drawable.youkulogo, R.drawable.tudoulogo, R.drawable.hunantvlogo
            , R.drawable.sohulogo, R.drawable.acfun, R.drawable.bilibili, R.drawable.oneninezerofivelogo
            , R.drawable.pptv, R.drawable.tangdoulogo, R.drawable.yinyuetailogo, R.drawable.huya
    };

    private int currentPosition = 0;
    private int currentApiIndex = apis.length - 1;
    private String currentApi = apis[currentApiIndex];
    private String currentPlayUrl = "";
    private boolean isWechatLogin = false;
    private boolean isValidVip = false;
    private WeiChatUserInfo userInfo;
    private String loginResult;

    public static VipHelperUtils getInstance() {
        if (instance == null) {
            instance = new VipHelperUtils();
        }
        return instance;

    }

    public VipHelperUtils() {
//        currentApi = apis[DataHelper.getIntergerSF(APPAplication.instance, "currentapi", apis.length - 1)];
        currentApi = DataHelper.getStringSF(APPAplication.instance, "currentapi_String");
        if(currentApi == null) {
            currentApi = VipHelperUtils.noAdUrl;
        }
    }

    public String getURLbyPositon() {
        return urls[currentPosition];
    }

    private String tips = "卡顿，播放不了，不清晰，试试右上角切换线路（自备八条线路）！！！";
    private String imgUrl = "file:///android_asset/player.png";
    private String imgAdUrl = "file:///android_asset/iv_ad.png";
    private String imgAdUrl2 = "file:///android_asset/iv_ad.png";

    public String changePlayURLbyPositon(Context context) {
        String temp;
        if (currentPosition == 5 || currentPosition == 4 || currentPosition == 2) {
            String tempHeight = "400px";
            if (currentPosition == 2) {
                tempHeight = "203px";
            }
            temp = "javascript:" +
                    "\t\t\tvar  elementName=\"=====\";\n" +
                    "            function countTotalElement(node){\n" +
                    "                var total=0;\n" +
                    "                if(node.nodeType==1){\n" +
                    "                   total++;\n" +
                    "                   elementName=elementName+node.tagName+\"\\\\r\\\\n\";\n" +
                    "\t\t\t\t   if(node.className == '" + ids[currentPosition] + "') {\n" +
                    "console.log(\"找到对应的node------------------\");" +
                    "\t\t\t\t\t\tvar parent = node.parentNode;\n" +
                    "\t\t\t\t\t\tvar div = document.createElement('div');\n" +
                    "\t\t\t\t\t\tdiv.innerHtml = node.innerHtml;\n" +
                    "\t\t\t\t\t\tdiv.id = node.id;\n" +
                    "div.style.cssText =\"\t\tbackground-repeat: no-repeat; background-position: center; background-size: cover; background-image: url(http://p0dmc0w17.bkt.clouddn.com/ply.png);\"\n" +
                    "\t\t\t\t\t\tdiv.style.width = '100%';\n" +
                    "\t\t\t\t\t\tdiv.style.height = '" + tempHeight + "';\n" +
                    "\t\t\t\t\t\tdiv.onclick = function (){\n" +
                    "\t\t\t\t\t\t\tconsole.log(\"jjskdfsdfdsjfkdsfj\");\n" +
                    "\t\t\t\t\t\t\twindow.jsHook.goToPlayPage(window.location.href)\n" +
                    "\t\t\t\t\t\t}\n" +
                    "\t\t\t\t\t\tparent.replaceChild(div, node);\n" +
                    "\t\t\t\t   }\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\tvar childrens=node.childNodes;\n" +
                    "\t\t\t\tfor(var i=0;i<childrens.length;i++){\n" +
                    "\t\t\t\t\ttotal+=countTotalElement(childrens[i]);\n" +
                    "\t\t\t\t} \n" +
                    "\t\t\t\treturn total;\n" +
                    "\t\t\t}\n" +
                    "\t\t\tcountTotalElement(document);"
            ;
        } else {
            String tempheight = "203px";
            if ( currentPosition == 3) {
                tempheight = "400px";
            }
            temp = "javascript:" +
                    "var player = document.getElementById('" + ids[currentPosition] + "');\n" +
                    "\t\tvar parent = player.parentNode;\n" +
                    "\t\t\t\t\t\tvar div = document.createElement('div');\n" +
                    "\t\t\t\t\t\tdiv.innerHtml = player.innerHtml;\n" +
                    "\t\t\t\t\t\tdiv.id = player.id;\n" +
                    "div.style.cssText =\"\t\tbackground-repeat: no-repeat; background-position: center; background-size: cover; background-image: url(http://p0dmc0w17.bkt.clouddn.com/ply.png);\"\n" +
                    "\t\tdiv.style.width = '100%';\n" +
                    "\t\tdiv.style.height = '" + tempheight + "';\n" +
                    "\t\tdiv.onclick = function (){\n" +
                    "\t\t\tconsole.log(\"jjskdfsdfdsjfkdsfj\");\n" +
                    "       window.jsHook.goToPlayPage(window.location.href)" +
                    "\t\t} \n" +
                    "\t\tparent.replaceChild(div, player); "
            ;
        }
        return temp;
    }

    public String changeSouhuPlayURLbyPositon(WebView webView) {
        String temp;
        String tempHeight = "203px";
        temp = "javascript:" +
                "\t\t\tvar  elementName=\"=====\";\n" +
                "            function countTotalElement(node){\n" +
                "                var total=0;\n" +
                "                if(node.nodeType==1){\n" +
                "                   total++;\n" +
                "                   elementName=elementName+node.tagName+\"\\\\r\\\\n\";\n" +
                "\t\t\t\t   if(node.className == 'player-view') {\n" +
                "console.log(\"找到对应的node------------------\");" +
                "\t\t\t\t\t\tvar parent = node.parentNode;\n" +
                "\t\t\t\t\t\tvar div = document.createElement('div');\n" +
                "\t\t\t\t\t\tdiv.innerHtml = node.innerHtml;\n" +
                "\t\t\t\t\t\tdiv.id = node.id;\n" +
                "div.style.cssText =\"\t\tbackground-repeat: no-repeat; background-position: center; background-size: cover; background-image: url(http://p0dmc0w17.bkt.clouddn.com/ply.png);\"\n" +
                "\t\t\t\t\t\tdiv.style.width = '100%';\n" +
                "\t\t\t\t\t\tdiv.style.height = '" + tempHeight + "';\n" +
                "\t\t\t\t\t\tdiv.onclick = function (){\n" +
                "\t\t\t\t\t\t\tconsole.log(\"jjskdfsdfdsjfkdsfj\");\n" +
                "\t\t\t\t\t\t\twindow.jsHook.goToPlayPage(window.location.href)\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\tparent.replaceChild(div, node);\n" +
                "\t\t\t\t   }\n" +
                "\t\t\t\t}\n" +
                "\t\t\t\tvar childrens=node.childNodes;\n" +
                "\t\t\t\tfor(var i=0;i<childrens.length;i++){\n" +
                "\t\t\t\t\ttotal+=countTotalElement(childrens[i]);\n" +
                "\t\t\t\t} \n" +
                "\t\t\t\treturn total;\n" +
                "\t\t\t}\n" +
                "\t\t\tcountTotalElement(document);"
        ;
        webView.loadUrl(temp);

        String tempheight = "203px";
        temp = "javascript:" +
                "var player = document.getElementById('top-poster');\n" +
                "\t\tvar parent = player.parentNode;\n" +
                "\t\t\t\t\t\tvar div = document.createElement('div');\n" +
                "\t\t\t\t\t\tdiv.innerHtml = player.innerHtml;\n" +
                "\t\t\t\t\t\tdiv.id = player.id;\n" +
                "div.style.cssText =\"\t\tbackground-repeat: no-repeat; background-position: center; background-size: cover; background-image: url(http://p0dmc0w17.bkt.clouddn.com/ply.png);\"\n" +
                "\t\tdiv.style.width = '100%';\n" +
                "\t\tdiv.style.height = '" + tempheight + "';\n" +
                "\t\tdiv.onclick = function (){\n" +
                "\t\t\tconsole.log(\"jjskdfsdfdsjfkdsfj\");\n" +
                "       window.jsHook.goToPlayPage(window.location.href)" +
                "\t\t} \n" +
                "\t\tparent.replaceChild(div, player); "
        ;
        webView.loadUrl(temp);
        return temp;
    }

    public String changeHUASHUPlayURLbyPositon(WebView webView) {
        String temp;

        String tempheight = "203px";
        temp = "javascript:" +
                "var player = document.getElementById('pop');\n" +
                "\t\tvar parent = player.parentNode;\n" +
                "\t\t\t\t\t\tvar div = document.createElement('div');\n" +
                "\t\t\t\t\t\tdiv.innerHtml = player.innerHtml;\n" +
                "\t\t\t\t\t\tdiv.id = player.id;\n" +
                "div.style.cssText =\"\t\tdisplay: block; background-repeat: no-repeat; background-position: center; background-size: cover; background-image: url(http://p0dmc0w17.bkt.clouddn.com/ply.png);\"\n" +
                "\t\tdiv.style.width = '100%';\n" +
                "\t\tdiv.style.height = '" + tempheight + "';\n" +
                "\t\tdiv.onclick = function (){\n" +
                "\t\t\tconsole.log(\"jjskdfsdfdsjfkdsfj\");\n" +
                "       window.jsHook.goToPlayPage(window.location.href)" +
                "\t\t} \n" +
                "\t\tparent.replaceChild(div, player); "
        ;
        webView.loadUrl(temp);

        String tempHeight = "203px";
        temp = "javascript:" +
                "\t\t\tvar  elementName=\"=====\";\n" +
                "            function countTotalElement(node){\n" +
                "                var total=0;\n" +
                "                if(node.nodeType==1){\n" +
                "                   total++;\n" +
                "                   elementName=elementName+node.tagName+\"\\\\r\\\\n\";\n" +
                "\t\t\t\t   if(node.className == 'ws_play relative') {\n" +
                "console.log(\"找到对应的node------------------\");" +
                "\t\t\t\t\t\tvar parent = node.parentNode;\n" +
                "\t\t\t\t\t\tvar div = document.createElement('div');\n" +
                "\t\t\t\t\t\tdiv.innerHtml = node.innerHtml;\n" +
                "\t\t\t\t\t\tdiv.id = node.id;\n" +
                "div.style.cssText =\"\t\tdisplay: block; background-repeat: no-repeat; background-position: center; background-size: cover; background-image: url(http://p0dmc0w17.bkt.clouddn.com/ply.png);\"\n" +
                "\t\t\t\t\t\tdiv.style.width = '100%';\n" +
                "\t\t\t\t\t\tdiv.style.height = '" + tempHeight + "';\n" +
                "\t\t\t\t\t\tdiv.onclick = function (){\n" +
                "\t\t\t\t\t\t\tconsole.log(\"jjskdfsdfdsjfkdsfj\");\n" +
                "\t\t\t\t\t\t\twindow.jsHook.goToPlayPage(window.location.href)\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\tparent.replaceChild(div, node);\n" +
                "\t\t\t\t   }\n" +
                "\t\t\t\t}\n" +
                "\t\t\t\tvar childrens=node.childNodes;\n" +
                "\t\t\t\tfor(var i=0;i<childrens.length;i++){\n" +
                "\t\t\t\t\ttotal+=countTotalElement(childrens[i]);\n" +
                "\t\t\t\t} \n" +
                "\t\t\t\treturn total;\n" +
                "\t\t\t}\n" +
                "\t\t\tcountTotalElement(document);"
        ;
        webView.loadUrl(temp);

        return temp;
    }

    /*public String changePlayURLbyPositon(Context context) {
        String temp;
        if(currentPosition == 6 || currentPosition == 5 || currentPosition == 10 || currentPosition == 2 || currentPosition == 4) {
            temp = "javascript:" +
                    "\t\t\tvar  elementName=\"=====\";\n" +
                    "            function countTotalElement(node){\n" +
                    "                var total=0;\n" +
                    "                if(node.nodeType==1){\n" +
                    "                   total++;\n" +
                    "                   elementName=elementName+node.tagName+\"\\\\r\\\\n\";\n" +
                    "\t\t\t\t   if(node.className == '" + ids[currentPosition] + "') {\n" +
                    "console.log(\"找到对应的node------------------\");"+
                    "\t\t\t\t\t\tvar parent = node.parentNode;\n" +
                    "\t\t\t\t\t\tvar div = document.createElement('div');\n" +
                    "\t\t\t\t\t\tdiv.innerHtml = node.innerHtml;\n" +
                    "\t\t\t\t\t\tdiv.id = node.id;\n" +
                    "div.style.cssText =\"\t\tbackground-repeat: no-repeat; background-position: center; background-size: cover; background-image: url(http://p0dmc0w17.bkt.clouddn.com/ply.png);\"\n"+
                    "\t\t\t\t\t\tdiv.style.width = '100%';\n" +
                    "\t\t\t\t\t\tdiv.style.height = '400px';\n" +
                    "\t\t\t\t\t\tdiv.onclick = function (){\n" +
                    "\t\t\t\t\t\t\tconsole.log(\"jjskdfsdfdsjfkdsfj\");\n" +
                    "\t\t\t\t\t\t\twindow.jsHook.goToPlayPage(window.location.href)\n" +
                    "\t\t\t\t\t\t}\n" +
                    "\t\t\t\t\t\tparent.replaceChild(div, node);\n" +
                    "\t\t\t\t   }\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\tvar childrens=node.childNodes;\n" +
                    "\t\t\t\tfor(var i=0;i<childrens.length;i++){\n" +
                    "\t\t\t\t\ttotal+=countTotalElement(childrens[i]);\n" +
                    "\t\t\t\t} \n" +
                    "\t\t\t\treturn total;\n" +
                    "\t\t\t}\n" +
                    "\t\t\tcountTotalElement(document);"
            ;
        }else {
            temp = "javascript:" +
                    "var player = document.getElementById('"+ ids[currentPosition] + "');\n" +
                    "\t\tvar parent = player.parentNode;\n" +
                    "\t\t\t\t\t\tvar div = document.createElement('div');\n" +
                    "\t\t\t\t\t\tdiv.innerHtml = player.innerHtml;\n" +
                    "\t\t\t\t\t\tdiv.id = player.id;\n" +
                    "div.style.cssText =\"\t\tbackground-repeat: no-repeat; background-position: center; background-size: cover; background-image: url(http://p0dmc0w17.bkt.clouddn.com/ply.png);\"\n"+
                    "\t\tdiv.style.width = '100%';\n" +
                    "\t\tdiv.style.height = '203px';\n" +
                    "\t\tdiv.onclick = function (){\n" +
                    "\t\t\tconsole.log(\"jjskdfsdfdsjfkdsfj\");\n" +
                    "       window.jsHook.goToPlayPage(window.location.href)"+
                    "\t\t} \n" +
                    "\t\tparent.replaceChild(div, player); "
            ;
        }
        return temp;
    }*/

    public String getRealPlaySite() {
        String temp = "javascript:" +
                "\t\tvar player = document.getElementById('post');\n" +
                "\t\tvar input = document.getElementById('v');\n" +
                "\t\tinput.value = '" + currentPlayUrl + "';\n" +
                "\t\t\talert('" + tips + "');\n" +
                "\t\tplayer.onclick();";
        return temp;
    }

    //play_script
//屏蔽播放页面底部广告，用张图片
//    public String pingbiAD() {
//        String temp = "javascript:" +
//                "var div = document.createElement('img');\n" +
//                "\t\t\t\tdiv.src = \""+imgAdUrl+"\";\n" +
//                "\t\t\t\tdiv.setAttribute('style', 'width: 100%!important; height: 30%!important; z-index: 2147483647!important; position: fixed!important; bottom: 0px; left: 0px; ');\n" +
//                "\t\t\t\tdocument.body.appendChild(div);";
//        return temp;
//    }

    //屏蔽底部广告用黑色背景
    public String pingbiAD() {
        String temp = "javascript:" +
                "var div = document.createElement('div');\n" +
                "\t\t\t\tdiv.setAttribute('style', 'width: 100%!important; height: 30%!important; z-index: 2147483647!important; position: fixed!important; bottom: 0px; left: 0px; background: #000000;');\n" +
                "\t\t\t\tdocument.body.appendChild(div);" +
                "var div = document.createElement('div');\n" +
                "\t\t\t\tdiv.setAttribute('style', 'width: 100%!important; height: 30%!important; z-index: 2147483647!important; position: fixed!important; top: 0px; left: 0px; background: #000000;');\n" +
                "\t\t\t\tdocument.body.appendChild(div);";
        return temp;
    }

    private ArrayList<String> hasSelectedlist = new ArrayList();


/*    public void changePlayLine(WebView view) {

        int tempPosition = random.nextInt(10);
        currentApi = apis[tempPosition];
        view.loadUrl("javascript:" +
                "var itta = document.getElementById(\"iframe\");\n" +
                "itta.src = '" +currentApi + currentPlayUrl +"'");
    }*/

    public void changePlayLine(WebView view) {

        currentApiIndex++;
        if (currentApiIndex >= apis.length) {
            currentApiIndex = 0;
        }
        currentApi = apis[currentApiIndex];
        DataHelper.setIntergerSF(APPAplication.instance, "currentapi", currentApiIndex);
        DataHelper.setStringSF(APPAplication.instance, "currentapi_String", currentApi);
        view.loadUrl(VipHelperUtils.getInstance().getCurrentApi() + currentPlayUrl);
        Log.i("changePlayLine", "changePlayLine-------------" + VipHelperUtils.getInstance().getCurrentApi() + currentPlayUrl);
    }

    public void getNewPlayLine() {

        int tempPosition = random.nextInt(10);
        currentApi = apis[tempPosition];
    }

    public void changeTitle(WebView view) {

        String temp = "javascript:" +
                "document.title = 'VIP';";
        view.loadUrl(temp);
    }

    public void changeCurrentSite(int position) {
        currentPosition = position;
    }

    public String[] getApis() {
        return apis;
    }

    public String[] getIds() {
        return ids;
    }

    public String[] getNames() {
        return names;
    }

    public String[] getUrls() {
        return urls;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public String getCurrentApi() {
        return currentApi;
    }

    public int[] getIcons() {
        return icons;
    }

    public String getCurrentPlayUrl() {
        return currentPlayUrl;
    }

    public void setCurrentPlayUrl(String currentPlayUrl) {
        this.currentPlayUrl = currentPlayUrl;
    }

    public void setCurrentApi(String currentApi) {
        this.currentApi = currentApi;
    }

    public boolean isWechatLogin() {
        return isWechatLogin;
    }

    public void setWechatLogin(boolean wechatLogin) {
        isWechatLogin = wechatLogin;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public WeiChatUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(WeiChatUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult;
    }

    public boolean isValidVip() {
        return isValidVip;
    }

    public void setValidVip(boolean validVip) {
        isValidVip = validVip;
    }
}
