package org.cocos2dx.lua.service;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 功能
 * Created by Jiang on 2017/11/6.
 */

public class StringResponseBodyConverter implements Converter<ResponseBody, String> {
    @Override
    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }
}
