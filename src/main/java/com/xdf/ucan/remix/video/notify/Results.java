package com.xdf.ucan.remix.video.notify;

import com.google.common.collect.Maps;

import java.util.Map;

public class  Results {

    public static Map<String, Object> ok(){
        Map<String, Object> data = Maps.newHashMap();
        data.put("code",0);
        return data;
    }
}
