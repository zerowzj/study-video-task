package com.xdf.ucan.remix.video.task.client.event;

import com.google.common.collect.Lists;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import com.xdf.ucan.remix.video.task.client.EventTask;
import com.xdf.ucan.remix.video.task.client.EventType;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfile;
import com.xdf.ucan.remix.video.task.support.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PullEventsV3Client {

    /**
     * 拉取事件
     *
     * @param authProfile 鉴权配置
     * @return List<EventTask> 事件任务列表
     */
    public List<EventTask> pullEvents(AuthProfile authProfile) throws TencentCloudSDKException {
        //*************** 配置 ***************
        Credential cred = new Credential(authProfile.getSecretId(), authProfile.getSecretKey());

        //*************** 参数 ***************
        PullEventsRequest req = new PullEventsRequest();
        req.setSubAppId(authProfile.getSubAppId());
        log.info(">>>>>>>>>> {}", JsonUtils.toJson(req));
        //*************** 请求 ***************
        List<EventTask> eventTaskLt = Lists.newArrayList();
        try {
            VodClient client = new VodClient(cred, authProfile.getRegion());
            PullEventsResponse resp = client.PullEvents(req);
            log.info("<<<<<<<<<< {}", JsonUtils.toJson(resp));

            //*************** 结果 ***************
            String requestId = resp.getRequestId();
            EventContent[] eventSets = resp.getEventSet();
            //*************** 包装 ***************
            for (EventContent content : eventSets) {
                String eventHandle = content.getEventHandle();
                String eventType = content.getEventType();
                if (EventType.PULL_COMPLETE.equalsIgnoreCase(eventType)) { //上传事件
                    PullUploadTask uploadTask = content.getPullCompleteEvent();
                    eventTaskLt.add(new EventTask(eventType, eventHandle, uploadTask));
                } else if (EventType.EDIT_COMPLETE.equalsIgnoreCase(eventType)) { //编辑事件
                    EditMediaTask editMediaTask = content.getEditMediaCompleteEvent();
                    eventTaskLt.add(new EventTask(eventType, eventHandle, editMediaTask));
                }
            }
        } catch (TencentCloudSDKException ex) {
            log.info("<<<<<<<<<< {}", ex.toString());
            throw ex;
        }
        return eventTaskLt;
    }
}
