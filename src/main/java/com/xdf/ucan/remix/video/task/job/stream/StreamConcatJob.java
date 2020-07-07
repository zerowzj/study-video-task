package com.xdf.ucan.remix.video.task.job.stream;

import com.xdf.ucan.remix.video.task.dao.videostream.VideoStreamDao;
import com.xdf.ucan.remix.video.task.service.stream.StreamService;
import com.xdf.ucan.remix.video.task.support.common.utils.DateUtils;
import com.xdf.ucan.remix.video.task.support.trace.JobTrace;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
//@Component
public class StreamConcatJob {

    @Autowired
    private VideoStreamDao videoStreamDao;
    @Autowired
    private StreamService streamService;

    @JobTrace(jobName = "stream_concat")
    public void doConcat() {
        //获取需要拼接的room id列表
        String startTime = DateUtils.toStdDateTimeStr(DateUtils.plusHours(-24));
        String endTime = DateUtils.toStdDateTimeStr(DateUtils.now());
        List<String> roomIdLt = videoStreamDao.getNeedConcatRoomIdLt(startTime, endTime);
        if (CollectionUtils.isEmpty(roomIdLt)) {
            log.info(">>>>>>>>>> no data to concat between [{}] and [{}]", startTime, endTime);
            return;
        }
        //拼接
        log.info("concat between [{}] and [{}]", startTime, endTime);
        streamService.concat(roomIdLt);
    }
}
