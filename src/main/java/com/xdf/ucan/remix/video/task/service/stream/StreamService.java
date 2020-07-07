package com.xdf.ucan.remix.video.task.service.stream;

import com.xdf.ucan.remix.video.task.client.EventTask;

import java.util.List;

public interface StreamService {

    /**
     * 请求拼接
     */
    void concat(List<String> streamIdLt);

    /**
     * 处理拼接结果
     */
    void handleConcatResult(List<EventTask> eventTaskLt);
}
