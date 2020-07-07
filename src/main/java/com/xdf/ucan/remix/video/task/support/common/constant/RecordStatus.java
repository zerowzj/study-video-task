package com.xdf.ucan.remix.video.task.support.common.constant;

public interface RecordStatus {

    /* 上传完成 */
    int UPLOAD_OK = 1;
    /* 通知拉取 */
    int PULL_NOTIFY = 2;
    /* 拉取完成 */
    int PULL_OK = 3;
    /* 拉取失败 */
    int PULL_ERROR = 3;
    /* 通知拼接 */
    int CONCAT_NOTIFY = 4;
    /* 拼接完成 */
    int CONCAT_OK = 5;
    /* 拼接失败 */
    int CONCAT_ERROR = 5;
    /* 确认完成 */
    int CONFIRM_OK = 6;
}
