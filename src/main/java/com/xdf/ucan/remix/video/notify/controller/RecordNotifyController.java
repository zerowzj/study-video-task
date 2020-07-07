package com.xdf.ucan.remix.video.notify.controller;

import com.xdf.ucan.remix.video.notify.Results;
import com.xdf.ucan.remix.video.notify.msg.RecordNotifyMsg;
import com.xdf.ucan.remix.video.notify.service.StreamService;
import com.xdf.ucan.remix.video.task.support.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public class RecordNotifyController {

    @Autowired
    private StreamService streamService;

    public Map<String, Object> recordNotify(RecordNotifyMsg msg) {
        log.info("record notify msg: {}", JsonUtils.toJson(msg, true));
        //事件类型
        Integer eventType = msg.getEvent_type();
        if (eventType != 1) {
            log.info("event_type error", eventType);
            return Results.ok();
        }
        //过期
        Long t = msg.getT();
        if (t == 1) {
            log.info("msg expire");
            return Results.ok();
        }
        //签名
        String sign = msg.getSign();
        if ("".equalsIgnoreCase(sign)) {
            log.info("sign error");
            return Results.ok();
        }
        //登记
        try {
            streamService.checkInRecord(null, null);
        } catch (Exception ex) {
            log.error("", ex);
        }
        return Results.ok();
    }
}
