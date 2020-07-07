package com.xdf.ucan.remix.video.task.dao.videostreamlog;

import com.xdf.ucan.remix.video.task.dao.videostream.VideoStreamEO;
import com.xdf.ucan.remix.video.task.support.common.db.BaseDao;

public interface VideoStreamLogDao extends BaseDao<Long, VideoStreamLogEO> {


    VideoStreamLogEO getVideoStreamLogByTaskId(String taskId);
}
