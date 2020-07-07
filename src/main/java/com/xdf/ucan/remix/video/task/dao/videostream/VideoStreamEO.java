package com.xdf.ucan.remix.video.task.dao.videostream;

import com.xdf.ucan.remix.video.task.support.common.db.BaseEO;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Setter
@Getter
@Alias("VideoStreamEO")
public class VideoStreamEO extends BaseEO {

    private Long vsId;

    private String vsRoomId;

    private String vsStreamId;

    private Integer vsStatus;

    private Date vsBeginTime;

    private Date vsEndTime;
}
