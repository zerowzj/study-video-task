package com.xdf.ucan.remix.video.task.dao.videorecord;

import com.xdf.ucan.remix.video.task.support.common.db.BaseEO;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Setter
@Getter
@Alias("VideoRecordEO")
public class VideoRecordEO extends BaseEO {

    private Long vrId;

    private String vrRoomId;

    private String vrName;

    private Long vrSize;

    private Long vrDuration;

    private String vrStorePath;

    private Integer vrStoreType;

    private Integer vrIsLast;

    private Integer vrStatus;

    private Date vrBeginTime;

    private Date vrEndTime;
}
