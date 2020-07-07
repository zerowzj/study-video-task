package com.xdf.ucan.remix.video.task.client.media;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.SearchMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.SearchMediaResponse;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfile;
import com.xdf.ucan.remix.video.task.support.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
public class SearchMediaV3Client {

    /**
     * 检索
     *
     * @param streamId
     * @param authProfile 鉴权配置
     * @return SimpleResp
     */
    public SearchMediaResponse searchMedia(String streamId, AuthProfile authProfile) throws Exception {
        //*************** 配置 ***************
        Credential cred = new Credential(authProfile.getSecretId(), authProfile.getSecretKey());

        //*************** 参数 ***************
        SearchMediaRequest req = new SearchMediaRequest();
        req.setStreamId(streamId);
        req.setSubAppId(authProfile.getSubAppId());
        log.info(">>>>>>>>>> {}", JsonUtils.toJson(req));

        //*************** 请求 ***************
        SearchMediaResponse resp;
        try {
            VodClient client = new VodClient(cred, "ap-beijing");
            resp = client.SearchMedia(req);
            log.info("<<<<<<<<<< {}", JsonUtils.toJson(resp));
        } catch (Exception ex) {
            log.info("<<<<<<<<<< {}", ex.toString());
            throw ex;
        }
        return resp;
    }
}
