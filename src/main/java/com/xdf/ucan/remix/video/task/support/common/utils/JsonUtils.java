package com.xdf.ucan.remix.video.task.support.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtils {

    public static String toJson(Object obj, boolean isWriteNull) {
        String text;
        if (isWriteNull) {
            text = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        } else {
            text = JSON.toJSONString(obj);
        }
        return text;
    }

    public static String toJson(Object obj) {
        return toJson(obj, false);
    }

    public static <T> T fromJson(String text, Class<T> clazz) {
        T t = JSON.parseObject(text, clazz);
        return t;
    }
}
