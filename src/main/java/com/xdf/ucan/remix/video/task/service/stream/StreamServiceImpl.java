package com.xdf.ucan.remix.video.task.service.stream;

import com.google.common.collect.Maps;
import com.tencentcloudapi.vod.v20180717.models.*;
import com.xdf.ucan.remix.video.task.client.EventTask;
import com.xdf.ucan.remix.video.task.client.EventType;
import com.xdf.ucan.remix.video.task.client.media.EditMediaV3Client;
import com.xdf.ucan.remix.video.task.client.media.SearchMediaV3Client;
import com.xdf.ucan.remix.video.task.dao.videostream.VideoStreamDao;
import com.xdf.ucan.remix.video.task.dao.videostream.VideoStreamEO;
import com.xdf.ucan.remix.video.task.dao.videostreamlog.VideoStreamLogDao;
import com.xdf.ucan.remix.video.task.dao.videostreamlog.VideoStreamLogEO;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfile;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfiles;
import com.xdf.ucan.remix.video.task.support.common.constant.StreamStatus;
import com.xdf.ucan.remix.video.task.support.common.utils.DateUtils;
import com.xdf.ucan.remix.video.task.support.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("streamService")
public class StreamServiceImpl implements StreamService {

    @Autowired
    private SearchMediaV3Client searchMediaV3Client;
    @Autowired
    private EditMediaV3Client editMediaV3Client;
    @Autowired
    private VideoStreamLogDao videoStreamLogDao;
    @Autowired
    private VideoStreamDao videoStreamDao;

    @Override
    public void concat(List<String> streamIdLt) {
        AuthProfile authProfile = AuthProfiles.getStmVodProfile();
        for (String streamId : streamIdLt) {
            //*************** 检索 ***************
            SearchMediaResponse searchMediaResp;
            try {
                searchMediaResp = searchMediaV3Client.searchMedia(streamId, authProfile);
            } catch (Exception ex) {
                log.error("", ex);
                continue;
            }
            String requestId = searchMediaResp.getRequestId();
            Long totalCount = searchMediaResp.getTotalCount();

            List<String> fileIdLt = Lists.newArrayList();
            List<Map<String, Object>> dataLt = Lists.newArrayList();
            Map<String, Object> data;
            MediaInfo[] mediaInfos = searchMediaResp.getMediaInfoSet();
            for (MediaInfo mediaInfo : mediaInfos) {
                String fileId = mediaInfo.getFileId();
                fileIdLt.add(fileId);

                data = Maps.newHashMap();
                MediaBasicInfo basicInfo = mediaInfo.getBasicInfo();
                data.put("file_id", fileId);
                data.put("media_url", basicInfo.getMediaUrl());
                data.put("name", basicInfo.getName());
                dataLt.add(data);
            }
            //保存检索信息
            VideoStreamLogEO vslEO = new VideoStreamLogEO();
            vslEO.setVslFileRequestId(requestId);
            vslEO.setVslFileTotalCount(totalCount);
            vslEO.setVslFileMediaInfo(JsonUtils.toJson(dataLt));
            vslEO.setVslFileBeginTime(DateUtils.now());

            //*************** 拼接 ***************
            EditMediaResponse editMediaResp;
            try {
                editMediaResp = editMediaV3Client.editMedia(fileIdLt, authProfile);
            } catch (Exception ex) {
                log.error("", ex);
                continue;
            }
            String editRequestId = editMediaResp.getRequestId();
            String editTaskId = editMediaResp.getTaskId();
            //保存拼接信息
            vslEO.setVslVodRequestId(editRequestId);
            vslEO.setVslVodTaskId(editTaskId);
            vslEO.setVslVodBeginTime(DateUtils.now());
            videoStreamLogDao.insert(vslEO);

            //更新
            VideoStreamEO vsEO = videoStreamDao.getVideoStreamLogByRoomId(streamId);
            vsEO.setVsStatus(StreamStatus.CONCAT_NOTIFY); //通知拼接
            vsEO.setVsEndTime(DateUtils.now());
            videoStreamDao.update(vsEO);
        }
    }

    @Override
    public void handleConcatResult(List<EventTask> eventTaskLt) {
        List<String> eventHandleLt = Lists.newArrayList();
        for (EventTask eventTask : eventTaskLt) {
            String eventType = eventTask.getEventType();
            if (!EventType.EDIT_COMPLETE.equalsIgnoreCase(eventType)) {
                log.info("no EditMediaComplete event");
                continue;
            }
            //事件确认
            String eventHandle = eventTask.getEventHandle();
            eventHandleLt.add(eventHandle);

            //*************** 任务解析 ***************
            EditMediaTask editMediaTask = eventTask.getEditMediaTask();
            String taskId = editMediaTask.getTaskId();
            VideoStreamLogEO vslEO = videoStreamLogDao.getVideoStreamLogByTaskId(taskId);
            if (vslEO == null) {
                log.error("not found task_id={} video record log", taskId);
                continue;
            }
            //错误码
            Long errorCode = editMediaTask.getErrCode();
            String message = editMediaTask.getMessage();
            String status = editMediaTask.getStatus();
            if (errorCode != 0) {
                log.error("error_code={}, message={}, status={}", errorCode, message, status);
                vslEO.setVslVodResultMsg(JsonUtils.toJson(eventTask, true));
                vslEO.setVslVodEndTime(DateUtils.now());
                videoStreamLogDao.update(vslEO);
                continue;
            }
            //输出
            EditMediaTaskOutput output = editMediaTask.getOutput();
            String fileType = output.getFileType();
            if (!"mp4".equalsIgnoreCase(fileType)) {
                log.info(">>>>>>>>>> illegal file type[{}]", fileType);
                continue;
            }
            String fileId = output.getFileId();
            String fileUrl = output.getFileUrl();

            //*************** 数据操作 ***************
            //更新拼接信息
            vslEO.setVslVodResultMsg(JsonUtils.toJson(eventTask, true));
            vslEO.setVslVodFileId(fileId);
            vslEO.setVslVodFileUrl(fileUrl);
            vslEO.setVslVodEndTime(DateUtils.now());
            videoStreamLogDao.update(vslEO);
            //更新推流信息
            String roomId = vslEO.getVslRoomId();
            VideoStreamEO vsEO = videoStreamDao.getVideoStreamLogByRoomId(roomId);
            vsEO.setVsStatus(1);
            vsEO.setVsEndTime(DateUtils.now());
            videoStreamDao.update(vsEO);
            //新增链接信息
        }

    }
}
