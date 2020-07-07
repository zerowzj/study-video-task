package com.xdf.ucan.remix.video.notify.service;

import com.xdf.ucan.remix.video.notify.StreamId;
import com.xdf.ucan.remix.video.notify.StreamParam;
import com.xdf.ucan.remix.video.task.dao.videostream.VideoStreamDao;
import com.xdf.ucan.remix.video.task.dao.videostream.VideoStreamEO;
import com.xdf.ucan.remix.video.task.support.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class StreamServiceImpl implements StreamService {

    @Autowired
    private VideoStreamDao videoStreamDao;

    @Override
    public void checkInStream(StreamId streamId, StreamParam streamParam) {
        String roomId = streamId.getRoomId();
        VideoStreamEO vsEO = videoStreamDao.getVideoStreamLogByRoomId(roomId);
        if (vsEO == null) {
            vsEO = new VideoStreamEO();
            vsEO.setVsRoomId(roomId);
            vsEO.setVsStreamId(streamId.getSrc());
            vsEO.setVsStatus(1);
            vsEO.setVsBeginTime(DateUtils.now());
            videoStreamDao.insert(vsEO);
        } else {
            vsEO.setVsEndTime(DateUtils.now());
            videoStreamDao.update(vsEO);
        }
    }

    @Override
    public void checkInRecord(StreamId streamId, StreamParam streamParam) {

    }
}
