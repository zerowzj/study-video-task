package com.xdf.ucan.remix.video.task.client.media;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.EditMediaFileInfo;
import com.tencentcloudapi.vod.v20180717.models.EditMediaOutputConfig;
import com.tencentcloudapi.vod.v20180717.models.EditMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.EditMediaResponse;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfile;
import com.xdf.ucan.remix.video.task.support.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class EditMediaV3Client {

    private static final String INPUT_TYPE_FILE = "File";

    private static final String OUTPUT_TYPE_MP4 = "mp4";

    /**
     * 拼接文件
     *
     * @param fileIdLt    文件id列表
     * @param authProfile 鉴权配置
     * @return EditMediaResponse
     */
    public EditMediaResponse editMedia(List<String> fileIdLt, AuthProfile authProfile) throws TencentCloudSDKException {
        //*************** 配置 ***************
        Credential cred = new Credential(authProfile.getSecretId(), authProfile.getSecretKey());

        //*************** 参数 ***************
        EditMediaRequest req = new EditMediaRequest();
        req.setSubAppId(authProfile.getSubAppId());
        //输入类型
        req.setInputType(INPUT_TYPE_FILE);
        //输出配置
        EditMediaOutputConfig outputCfg = new EditMediaOutputConfig();
        outputCfg.setType(OUTPUT_TYPE_MP4);
        req.setOutputConfig(outputCfg);
        //文件信息
        List<EditMediaFileInfo> fileInfoLt = Lists.newArrayList();
        EditMediaFileInfo fileInfo;
        for (String fileId : fileIdLt) {
            fileInfo = new EditMediaFileInfo();
            fileInfo.setFileId(fileId);
            fileInfoLt.add(fileInfo);
        }
        req.setFileInfos(fileInfoLt.toArray(new EditMediaFileInfo[fileInfoLt.size()]));
        log.info(">>>>>> {}", JsonUtils.toJson(req));

        //*************** 请求 ***************
        EditMediaResponse resp;
        try {
            VodClient client = new VodClient(cred, "ap-beijing");
            resp = client.EditMedia(req);
            log.info("<<<<<< {}", JsonUtils.toJson(resp));
        } catch (TencentCloudSDKException ex) {
            log.info("<<<<<< {}", ex.toString());
            throw ex;
        }
        return resp;
    }
}
