package com.xdf.ucan.remix.video.task.dao.videorecord;

import com.xdf.ucan.remix.video.task.support.common.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface VideoRecordDao extends BaseDao<Long, VideoRecordEO> {

    List<VideoRecordEO> getNeedUploadRecordLt(@Param("startTime") Date startTime,
                                              @Param("endTime") Date endTime);

    List<String> getNeedConcatRoomIdLt(@Param("startTime") Date startTime,
                                       @Param("endTime") Date endTime);

    List<VideoRecordEO> getVideoRecordLtByRoomId(@Param("roomId") String roomId);
}
