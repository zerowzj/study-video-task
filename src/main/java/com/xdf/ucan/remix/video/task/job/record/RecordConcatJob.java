package com.xdf.ucan.remix.video.task.job.record;

import com.xdf.ucan.remix.video.task.dao.videorecord.VideoRecordDao;
import com.xdf.ucan.remix.video.task.service.record.RecordService;
import com.xdf.ucan.remix.video.task.support.common.utils.DateUtils;
import com.xdf.ucan.remix.video.task.support.trace.JobTrace;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class RecordConcatJob {

    @Autowired
    private VideoRecordDao videoRecordDao;
    @Autowired
    private RecordService recordService;

    /**
     * 请求拼接
     */
    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
    @JobTrace(jobName = "record_concat")
    public void doConcat() {
        //获取需要拼接的room id列表
        Date startTime = DateUtils.plusHours(-24);
        Date endTime = DateUtils.now();
        List<String> roomIdLt = videoRecordDao.getNeedConcatRoomIdLt(startTime, endTime);
        if (CollectionUtils.isEmpty(roomIdLt)) {
            log.info(">>>>>>>>>> no data to concat between [{}] and [{}]", startTime, endTime);
            return;
        }
        //拼接
        log.info("concat between [{}] and [{}]", startTime, endTime);
        recordService.concat(roomIdLt);
    }
}
