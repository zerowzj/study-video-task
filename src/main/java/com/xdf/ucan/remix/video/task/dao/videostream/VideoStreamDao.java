package com.xdf.ucan.remix.video.task.dao.videostream;

import com.xdf.ucan.remix.video.task.support.common.db.BaseDao;

import java.util.List;

public interface VideoStreamDao extends BaseDao<Long, VideoStreamEO> {

    List<String> getNeedConcatRoomIdLt(String startTime,
                                       String endTime);

    VideoStreamEO getVideoStreamLogByRoomId(String roomId);

    int updateByRoomId(String roomId);

}
