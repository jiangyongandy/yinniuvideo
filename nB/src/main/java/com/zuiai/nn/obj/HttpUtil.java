package com.zuiai.nn.obj;

import android.content.SharedPreferences;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * get data from api
 * Created by pengyi on 2015/7/27.
 */
public class HttpUtil {

    //cookie处理
//    public static String sCookie = "";
    public static String webCookie = "";

    public static String urlConnection(String url, String pa, SharedPreferences cache, String webCookie) {
        String response = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            // Send data
            PrintWriter pw = new PrintWriter(conn.getOutputStream());
            // pa为请求的参数
            pw.print(pa);
            pw.flush();
            pw.close();
            // Get the response!
            int httpResponseCode = conn.getResponseCode();
            if (httpResponseCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("HTTP response code: " + httpResponseCode +
                        "\nurl:" + url);
            }

            InputStream inputStream = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String readLine;
            while (null != (readLine = br.readLine())) {
                builder.append(readLine);
            }
            inputStream.close();
            response = builder.toString();
            if (null != cache) {

                //保存cookie
                Map<String, List<String>> map = conn.getHeaderFields();
                List<String> cookies = map.get("set-cookie");
                String s = "";
                for (String cookie : cookies) {
                    if (s.isEmpty()) {
                        s = cookie;
                    } else {
                        s += ";" + cookie;
                    }
                }
                if (s.length() > 0) {
                    cache.edit().putString("sCookie", s.replace("path=/;", "")).apply();
                }

            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
