package com.xdf.ucan.remix.video.task.job.record;

import com.xdf.ucan.remix.video.task.client.EventTask;
import com.xdf.ucan.remix.video.task.client.EventType;
import com.xdf.ucan.remix.video.task.client.event.PullEventsV3Client;
import com.xdf.ucan.remix.video.task.service.record.RecordService;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfiles;
import com.xdf.ucan.remix.video.task.support.trace.JobTrace;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RecordPullEventJob {

    @Autowired
    private PullEventsV3Client pullEventsV3Client;
    @Autowired
    private RecordService recordService;

    /**
     * 处理事件
     */
    @Scheduled(initialDelay = 2000, fixedDelay = 10000)
    @JobTrace(jobName = "record_pull_event")
    public void doEvent() {
        //获取事件
        List<EventTask> eventTaskLt;
        try {
            eventTaskLt = pullEventsV3Client.pullEvents(AuthProfiles.getRecVodProfile());
        } catch (Exception ex) {
            log.info("{}",  ex.toString());
            return;
        }
        //分离事件
        List<EventTask> uploadEventTaskLt = Lists.newArrayList();
        List<EventTask> editEventTaskLt = Lists.newArrayList();
        List<EventTask> unknownEventTaskLt = Lists.newArrayList();
        eventTaskLt.forEach(e -> {
            String eventType = e.getEventType();
            if (EventType.PULL_COMPLETE.equalsIgnoreCase(eventType)) {
                uploadEventTaskLt.add(e);
            } else if (EventType.EDIT_COMPLETE.equalsIgnoreCase(eventType)) {
                editEventTaskLt.add(e);
            } else {
                unknownEventTaskLt.add(e);
            }
        });
        //处理事件
        if (CollectionUtils.isNotEmpty(uploadEventTaskLt)) {
            recordService.handleUploadResult(uploadEventTaskLt);
        }
        if (CollectionUtils.isNotEmpty(editEventTaskLt)) {
            recordService.handleConcatResult(editEventTaskLt);
        }
    }
}
