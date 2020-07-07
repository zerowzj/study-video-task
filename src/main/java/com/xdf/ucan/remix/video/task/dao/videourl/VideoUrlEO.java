package com.xdf.ucan.remix.video.task.dao.videourl;

import com.xdf.ucan.remix.video.task.support.common.db.BaseEO;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Setter
@Getter
@Alias("VideoUrlEO")
public class VideoUrlEO extends BaseEO {

    private Long vuId;

    private String vuRoomId;

    private String vuUrl;

    private Integer vuType;

    private Date vuBeginTime;

    private Date vuEndTime;
}
