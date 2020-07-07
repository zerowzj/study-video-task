package com.xdf.ucan.remix.video.task.support.common.constant;

public interface StreamStatus {

    /* 推流开始 */
    int STREAMING = 1;
    /* 推流完成 */
    int STREAM_OK = 2;
    /* 通知拼接 */
    int CONCAT_NOTIFY = 3;
    /* 拼接完成 */
    int CONCAT_OK = 4;
}
