package com.xdf.ucan.remix.video.notify;

import com.google.common.base.Splitter;
import lombok.Getter;

import java.util.Map;

@Getter
public class StreamParam {

    private String src;

    private String txSecret;

    private String txTime;

    private String vodSubAppId;

    private String form;

    /**
     * eg.
     * txSecret=2c83c1d6fdadc2a5a77e06ad2084c6fc&txTime=5EC4A045&vod_sub_app_id=1400317442&from=1
     * txSecret=add5fd659dbb416f6d34ce99f66bc70b&txTime=5EC472B6&vod_sub_app_id=1400317442&from=1
     */
    public StreamParam(String src) {
        this.src = src;
        Map<String, String> data = Splitter.on("&")
                .withKeyValueSeparator("=")
                .split(src);
        this.txSecret = data.get("txSecret");
        this.txTime = data.get("txTime");
        this.vodSubAppId = data.get(" vod_sub_app_id");
        this.form = data.get("form");
    }
}
