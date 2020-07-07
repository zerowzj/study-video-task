package com.xdf.ucan.remix.video.task.job.stream;

import com.xdf.ucan.remix.video.task.client.EventTask;
import com.xdf.ucan.remix.video.task.client.EventType;
import com.xdf.ucan.remix.video.task.client.event.PullEventsV3Client;
import com.xdf.ucan.remix.video.task.service.stream.StreamService;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfile;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfiles;
import com.xdf.ucan.remix.video.task.support.trace.JobTrace;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StreamPullEventJob {

    @Autowired
    private PullEventsV3Client pullEventsV3Client;
    @Autowired
    private StreamService streamService;

    /**
     * 处理事件
     */
    @JobTrace(jobName = "stream_pull_event")
    public void doEvent() {
        AuthProfile authProfile = AuthProfiles.getStmVodProfile();
        //获取事件
        List<EventTask> eventTaskLt;
        try {
            eventTaskLt = pullEventsV3Client.pullEvents(authProfile);
        } catch (Exception ex) {
            log.error("", ex);
            return;
        }
        //分离事件
        List<EventTask> editEventTaskLt = Lists.newArrayList();
        List<EventTask> unknownEventTaskLt = Lists.newArrayList();
        eventTaskLt.forEach(e -> {
            String eventType = e.getEventType();
            if (EventType.EDIT_COMPLETE.equalsIgnoreCase(eventType)) {
                editEventTaskLt.add(e);
            } else {
                unknownEventTaskLt.add(e);
            }
        });
        //处理事件
        if (CollectionUtils.isNotEmpty(editEventTaskLt)) {
            streamService.handleConcatResult(editEventTaskLt);
        }
    }
}
