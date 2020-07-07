package com.xdf.ucan.remix.video.notify.controller;

import com.xdf.ucan.remix.video.notify.Results;
import com.xdf.ucan.remix.video.notify.StreamId;
import com.xdf.ucan.remix.video.notify.StreamParam;
import com.xdf.ucan.remix.video.notify.msg.StreamBeginNotifyMsg;
import com.xdf.ucan.remix.video.notify.service.StreamService;
import com.xdf.ucan.remix.video.task.support.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public class StreamBeginNotifyController {

    @Autowired
    private StreamService streamService;

    public Map<String, Object> streamBeginNotify(StreamBeginNotifyMsg msg) {
        log.info("stream begin notify msg: {}", JsonUtils.toJson(msg, true));
        //事件类型
        Integer eventType = msg.getEvent_type();
        if (eventType != 1) {
            log.info("event_type error", eventType);
            return Results.ok();
        }
        //过期
        Long t = msg.getT();
        if (t == 1) {
            log.info("notify expire");
            return Results.ok();
        }
        //签名
        String sign = msg.getSign();
        if ("".equalsIgnoreCase(sign)) {
            log.info("sign error");
            return Results.ok();
        }
        //推流id
        String streamIdText = msg.getStream_id();
        StreamId streamId = new StreamId(streamIdText);
        //自定义参数
        String streamParamText = msg.getStream_param();
        StreamParam streamParam = new StreamParam(streamParamText);
        //登记推流
        try {
            streamService.checkInStream(streamId, streamParam);
        } catch (Exception ex) {
            log.error("", ex);
        }
        return Results.ok();
    }
}
