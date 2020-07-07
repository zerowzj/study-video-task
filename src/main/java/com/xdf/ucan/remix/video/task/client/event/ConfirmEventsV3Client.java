package com.xdf.ucan.remix.video.task.client.event;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.ConfirmEventsRequest;
import com.tencentcloudapi.vod.v20180717.models.ConfirmEventsResponse;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfile;
import com.xdf.ucan.remix.video.task.support.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ConfirmEventsV3Client {

    /**
     * 确认事件
     *
     * @param eventHandleLt 事件处理列表
     * @param authProfile   鉴权配置
     * @return String RequestId
     */
    public ConfirmEventsResponse confirmEvents(List<String> eventHandleLt, AuthProfile authProfile) throws TencentCloudSDKException {
        //*************** 配置 ***************
        Credential cred = new Credential(authProfile.getSecretId(), authProfile.getSecretKey());

        //*************** 参数 ***************
        ConfirmEventsRequest req = new ConfirmEventsRequest();
        req.setSubAppId(authProfile.getSubAppId());
        req.setEventHandles(eventHandleLt.toArray(new String[eventHandleLt.size()]));
        log.info(">>>>>>>>>> {}", JsonUtils.toJson(req));

        //*************** 请求 ***************
        ConfirmEventsResponse resp;
        try {
            //请求
            VodClient client = new VodClient(cred, "ap-beijing");
             resp = client.ConfirmEvents(req);
            log.info("<<<<<<<<<< {}", JsonUtils.toJson(resp));
        } catch (TencentCloudSDKException ex) {
            log.info("<<<<<<<<<< {}", ex.toString());
            throw ex;
        }
        return resp;
    }
}
