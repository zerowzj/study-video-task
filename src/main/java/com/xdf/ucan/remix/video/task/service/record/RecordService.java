package com.xdf.ucan.remix.video.task.service.record;

import com.xdf.ucan.remix.video.task.client.EventTask;
import com.xdf.ucan.remix.video.task.dao.videorecord.VideoRecordEO;

import java.util.List;

public interface RecordService {

    /**
     * 请求上传
     */
    void upload(List<VideoRecordEO> vrEOLt);

    /**
     * 处理上传结果
     */
    void handleUploadResult(List<EventTask> eventTaskLt);

    /**
     * 请求拼接
     */
    void concat(List<String> roomIdLt);

    /**
     * 处理拼接结果
     */
    void handleConcatResult(List<EventTask> eventTaskLt);
}
