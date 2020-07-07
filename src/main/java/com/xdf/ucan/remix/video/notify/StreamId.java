package com.xdf.ucan.remix.video.notify;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class StreamId {

    private String src;

    private String env;

    private String type;

    private String roomId;

    /**
     * dev_1_20200519_XXLi7
     * prod_2_20200519_XXLi7
     */
    public StreamId(String src) {
       this.src = src;
        //环境
        env = StringUtils.substringBefore("_", src);
        //课程类型
        String temp = StringUtils.substringAfter("_", src);
        type = StringUtils.substringBefore("_", temp);
        //room id
        String myRoomId = StringUtils.substringAfter("_", temp);
        if ("1".equals(type)) {
            roomId = StringUtils.replace(myRoomId, "_", "|");
        } else {
            roomId = StringUtils.replace(myRoomId, "_", "-");
        }
    }
}
