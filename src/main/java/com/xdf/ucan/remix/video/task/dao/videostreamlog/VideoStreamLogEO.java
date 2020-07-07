package com.xdf.ucan.remix.video.task.dao.videostreamlog;

import com.xdf.ucan.remix.video.task.support.common.db.BaseEO;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Setter
@Getter
@Alias("VideoStreamLogEO")
public class VideoStreamLogEO extends BaseEO {

    private Long vslId;

    private Long vslVsId;

    private String vslRoomId;

    private String vslFileRequestId;

    private Long vslFileTotalCount;

    private String vslFileMediaInfo;

    private Date vslFileBeginTime;

    private Date vslFileEndTime;

    private String vslVodRequestId;

    private String vslVodTaskId;

    private String vslVodResultMsg;

    private String vslVodFileId;

    private String vslVodFileUrl;

    private Date vslVodBeginTime;

    private Date vslVodEndTime;
}
