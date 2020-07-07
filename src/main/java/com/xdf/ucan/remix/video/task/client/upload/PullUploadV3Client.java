package com.xdf.ucan.remix.video.task.client.upload;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.PullUploadRequest;
import com.tencentcloudapi.vod.v20180717.models.PullUploadResponse;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfile;
import com.xdf.ucan.remix.video.task.support.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
public class PullUploadV3Client {

    /**
     * 拉取上传
     *
     * @param mediaUrl    媒体地址
     * @param authProfile 鉴权配置
     * @return PullUploadResponse
     */
    public PullUploadResponse pullUpload(String mediaUrl, AuthProfile authProfile) throws TencentCloudSDKException {
        //*************** 配置 ***************
        Credential credential = new Credential(authProfile.getSecretId(), authProfile.getSecretKey());
        //http
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReadTimeout(30);
        httpProfile.setReqMethod(HttpProfile.REQ_POST);
        //client
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        //*************** 参数 ***************
        PullUploadRequest req = new PullUploadRequest();
        req.setSubAppId(authProfile.getSubAppId());
        req.setMediaUrl(mediaUrl);
        log.info(">>>>>>>>>> {}", JsonUtils.toJson(req));

        //*************** 请求 ***************
        PullUploadResponse resp;
        try {
            VodClient client = new VodClient(credential, authProfile.getRegion(), clientProfile);
            resp = client.PullUpload(req);
            log.info("<<<<<<<<<< {}", JsonUtils.toJson(resp));
        } catch (TencentCloudSDKException ex) {
            log.info("<<<<<<<<<< {}", ex.toString());
            throw ex;
        }
        return resp;
    }
}
