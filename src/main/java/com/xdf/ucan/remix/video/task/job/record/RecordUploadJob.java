package com.xdf.ucan.remix.video.task.job.record;

import com.xdf.ucan.remix.video.task.dao.videorecord.VideoRecordDao;
import com.xdf.ucan.remix.video.task.dao.videorecord.VideoRecordEO;
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
public class RecordUploadJob {

    @Autowired
    private VideoRecordDao videoRecordDao;
    @Autowired
    private RecordService recordService;

    /**
     * 请求上传
     */
    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
    @JobTrace(jobName = "record_upload")
    public void doUpload() {
        //获取需要上传的记录列表
        Date startTime = DateUtils.plusHours(-24);
        Date endTime = DateUtils.now();
        List<VideoRecordEO> vrEOLt = videoRecordDao.getNeedUploadRecordLt(startTime, endTime);
        if (CollectionUtils.isEmpty(vrEOLt)) {
            log.info(">>>>>>>>>> no data to upload between [{}] and [{}]", startTime, endTime);
            return;
        }
        //上传
        log.info("upload between [{}] and [{}]", startTime, endTime);
        recordService.upload(vrEOLt);
    }
}
