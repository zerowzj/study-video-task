package com.xdf.ucan.remix.video.task.dao.videorecordlog;

import com.xdf.ucan.remix.video.task.support.common.db.BaseEO;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Setter
@Getter
@Alias("VideoRecordLogEO")
public class VideoRecordLogEO extends BaseEO {

    private Long vrlId;

    private Long vrlVrId;

    private String vrlRoomId;

    private String vrlUploadRequestId;

    private String vrlUploadTaskId;

    private String vrlUploadFileId;

    private String vrlUploadFileUrl;

    private String vrlUploadResultMsg;

    private Date vrlUploadBeginTime;

    private Date vrlUploadEndTime;

    private String vrlVodRequestId;

    private String vrlVodTaskId;
    
    private String vrlVodFileId;

    private String vrlVodFileUrl;

    private String vrlVodResultMsg;

    private Date vrlVodBeginTime;

    private Date vrlVodEndTime;
}
