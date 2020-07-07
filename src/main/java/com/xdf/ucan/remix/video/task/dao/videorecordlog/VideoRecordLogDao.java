package com.xdf.ucan.remix.video.task.dao.videorecordlog;

import com.xdf.ucan.remix.video.task.support.common.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoRecordLogDao extends BaseDao<Long, VideoRecordLogEO> {

    VideoRecordLogEO getRecordLogByTaskId(String uploadTaskId, String vodTaskId);

    List<VideoRecordLogEO> getRecordLogLtByRoomId(@Param("roomId") String roomId);
}
