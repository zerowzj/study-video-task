package com.xdf.ucan.remix.video.task.client;

import com.tencentcloudapi.vod.v20180717.models.EditMediaTask;
import com.tencentcloudapi.vod.v20180717.models.PullUploadTask;
import lombok.Getter;

@Getter
public class EventTask {

    private String eventType;

    private String eventHandle;

    private PullUploadTask pullUploadTask;

    private EditMediaTask editMediaTask;

    public EventTask(String eventType, String eventHandle, PullUploadTask pullUploadTask) {
        this.eventType = eventType;
        this.eventHandle = eventHandle;
        this.pullUploadTask = pullUploadTask;
    }

    public EventTask(String eventType, String eventHandle, EditMediaTask editMediaTask) {
        this.eventType = eventType;
        this.eventHandle = eventHandle;
        this.editMediaTask = editMediaTask;
    }
}
