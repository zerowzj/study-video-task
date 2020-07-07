package com.xdf.ucan.remix.video.task.service.record;

import com.tencentcloudapi.vod.v20180717.models.ConfirmEventsResponse;
import com.tencentcloudapi.vod.v20180717.models.EditMediaResponse;
import com.tencentcloudapi.vod.v20180717.models.EditMediaTask;
import com.tencentcloudapi.vod.v20180717.models.EditMediaTaskOutput;
import com.tencentcloudapi.vod.v20180717.models.PullUploadResponse;
import com.tencentcloudapi.vod.v20180717.models.PullUploadTask;
import com.xdf.ucan.remix.video.task.client.EventTask;
import com.xdf.ucan.remix.video.task.client.EventType;
import com.xdf.ucan.remix.video.task.client.event.ConfirmEventsV3Client;
import com.xdf.ucan.remix.video.task.client.media.EditMediaV3Client;
import com.xdf.ucan.remix.video.task.client.upload.PullUploadV3Client;
import com.xdf.ucan.remix.video.task.dao.videorecord.VideoRecordDao;
import com.xdf.ucan.remix.video.task.dao.videorecord.VideoRecordEO;
import com.xdf.ucan.remix.video.task.dao.videorecordlog.VideoRecordLogDao;
import com.xdf.ucan.remix.video.task.dao.videorecordlog.VideoRecordLogEO;
import com.xdf.ucan.remix.video.task.dao.videourl.VideoUrlDao;
import com.xdf.ucan.remix.video.task.dao.videourl.VideoUrlEO;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfile;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfiles;
import com.xdf.ucan.remix.video.task.support.common.constant.RecordStatus;
import com.xdf.ucan.remix.video.task.support.common.utils.DateUtils;
import com.xdf.ucan.remix.video.task.support.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service("recordService")
public class RecordServiceImpl implements RecordService {

    @Autowired
    private VideoRecordDao videoRecordDao;
    @Autowired
    private VideoRecordLogDao videoRecordLogDao;
    @Autowired
    private VideoUrlDao videoUrlDao;
    @Autowired
    private PullUploadV3Client pullUploadV3Client;
    @Autowired
    private EditMediaV3Client editMediaV3Client;
    @Autowired
    private ConfirmEventsV3Client confirmEventsV3Client;

    @Override
    public void upload(List<VideoRecordEO> vrEOLt) {
        AuthProfile authProfile = AuthProfiles.getRecVodProfile();
        for (VideoRecordEO vrEO : vrEOLt) {
            //*************** 通知拉取 ***************
            String mediaUrl = vrEO.getVrStorePath();
            PullUploadResponse resp;
            try {
                resp = pullUploadV3Client.pullUpload(mediaUrl, authProfile);
            } catch (Exception ex) {
                log.error("", ex);
                continue;
            }
            String requestId = resp.getRequestId();
            String taskId = resp.getTaskId();

            //*************** 数据操作 ***************
            //生成Video Record Log
            VideoRecordLogEO vrlEO = new VideoRecordLogEO();
            vrlEO.setVrlVrId(vrEO.getVrId());
            vrlEO.setVrlRoomId(vrEO.getVrRoomId());
            vrlEO.setVrlUploadRequestId(requestId);
            vrlEO.setVrlUploadTaskId(taskId);
            vrlEO.setVrlUploadBeginTime(DateUtils.now()); //拉取开始时间
            videoRecordLogDao.insert(vrlEO);
            //更新Video Record
            vrEO.setVrStatus(RecordStatus.PULL_NOTIFY); //通知拉取
            vrEO.setVrEndTime(DateUtils.now());
            videoRecordDao.update(vrEO);
        }
    }

    @Override
    public void handleUploadResult(List<EventTask> eventTaskLt) {
        AuthProfile authProfile = AuthProfiles.getRecVodProfile();
        List<String> eventHandleLt = Lists.newArrayList();
        for (EventTask eventTask : eventTaskLt) {
            String eventType = eventTask.getEventType();
            if (!EventType.PULL_COMPLETE.equalsIgnoreCase(eventType)) {
                log.warn("event type error");
                continue;
            }
            //事件确认列表
            String eventHandle = eventTask.getEventHandle();
            eventHandleLt.add(eventHandle);

            //*************** 任务解析 ***************
            PullUploadTask uploadTask = eventTask.getPullUploadTask();
            String taskId = uploadTask.getTaskId();
            VideoRecordLogEO vrlEO = videoRecordLogDao.getRecordLogByTaskId(taskId, null);
            if (vrlEO == null) {
                log.info("no record of upload_task_id=[{}] was got", taskId);
                continue;
            }
            Long errorCode = uploadTask.getErrCode();
            String message = uploadTask.getMessage();
            String status = uploadTask.getStatus();
            if (errorCode != 0) {
                log.info("error_code={}, message={}, status={}", errorCode, message, status);
                vrlEO.setVrlUploadResultMsg(JsonUtils.toJson(eventTask, true));
                vrlEO.setVrlUploadEndTime(DateUtils.now()); //拉取结束时间
                videoRecordLogDao.update(vrlEO);
                continue;
            }
            String fileId = uploadTask.getFileId();
            String fileUrl = uploadTask.getFileUrl();

            //*************** 数据操作 ***************
            //更新处理信息
            vrlEO.setVrlUploadFileId(fileId);
            vrlEO.setVrlUploadFileUrl(fileUrl);
            vrlEO.setVrlUploadResultMsg(JsonUtils.toJson(eventTask, true));
            vrlEO.setVrlUploadEndTime(DateUtils.now()); //拉取结束时间
            videoRecordLogDao.update(vrlEO);
            //更新视频记录
            VideoRecordEO vrEO = videoRecordDao.get(vrlEO.getVrlVrId());
            vrEO.setVrStatus(RecordStatus.PULL_OK); //拉取完成
            vrEO.setVrEndTime(DateUtils.now());
            videoRecordDao.update(vrEO);
        }
        //*************** 队列确认 ***************
        try {
            ConfirmEventsResponse resp = confirmEventsV3Client.confirmEvents(eventHandleLt, authProfile);
        } catch (Exception ex) {
            log.error("", ex);
        }
    }

    @Override
    public void concat(List<String> roomIdLt) {
        for (String roomId : roomIdLt) {
            //获取处理信息
            List<VideoRecordLogEO> vrlEOLt = videoRecordLogDao.getRecordLogLtByRoomId(roomId);
            if (CollectionUtils.isEmpty(vrlEOLt)) {
                log.warn("room_id={}, 处理列表为空", roomId);
                continue;
            }
            //生成file id列表
            List<String> fileIdLt = Lists.newArrayList();
            vrlEOLt.forEach(e -> {
                String fileId = e.getVrlUploadFileId();
                if (Strings.isNullOrEmpty(fileId)) {
                    log.warn("room_id={} file id is empty", roomId);
                    //在一个方法的lambda表达式中使用return时，这个方法是不会返回的，而只是执行下一次遍历
                    return;
                }
                fileIdLt.add(fileId);
            });
            //*************** 请求拼接 ***************
            EditMediaResponse resp;
            try {
                resp = editMediaV3Client.editMedia(fileIdLt, AuthProfiles.getRecVodProfile());
            } catch (Exception ex) {
                log.error("", ex);
                continue;
            }
            //获取结果
            String requestId = resp.getRequestId();
            String taskId = resp.getTaskId();
            //更新拼接信息
            for (VideoRecordLogEO vrlEO : vrlEOLt) {
                vrlEO.setVrlVodRequestId(requestId);
                vrlEO.setVrlVodTaskId(taskId);
                vrlEO.setVrlVodBeginTime(DateUtils.now()); //拼接开始时间
                videoRecordLogDao.update(vrlEO);
            }

            List<VideoRecordEO> vrEOLt = videoRecordDao.getVideoRecordLtByRoomId(roomId);
            for (VideoRecordEO vrEO : vrEOLt) {
                vrEO.setVrStatus(RecordStatus.CONCAT_NOTIFY); //通知拼接
                vrEO.setVrEndTime(DateUtils.now());
                videoRecordDao.update(vrEO);
            }
        }
    }

    @Override
    public void handleConcatResult(List<EventTask> eventTaskLt) {
        AuthProfile authProfile = AuthProfiles.getRecVodProfile();
        List<String> eventHandleLt = Lists.newArrayList();
        for (EventTask eventTask : eventTaskLt) {
            //事件类型
            String eventType = eventTask.getEventType();
            if (!EventType.EDIT_COMPLETE.equalsIgnoreCase(eventType)) {
                log.info("no EditMediaComplete event");
                continue;
            }
            //事件确认
            String eventHandle = eventTask.getEventHandle();
            eventHandleLt.add(eventHandle);

            //*************** 任务解析 ***************
            //拼接任务
            EditMediaTask editMediaTask = eventTask.getEditMediaTask();
            String taskId = editMediaTask.getTaskId();
            VideoRecordLogEO vrlEO = videoRecordLogDao.getRecordLogByTaskId(null, taskId);
            if (vrlEO == null) {
                log.error("not found task_id={} video record log", taskId);
                continue;
            }
            //错误码
            Long errorCode = editMediaTask.getErrCode();
            String message = editMediaTask.getMessage();
            String status = editMediaTask.getStatus();
            if (errorCode != 0) {
                log.error("error_code={}, message={}, status={}", errorCode, message, status);
                vrlEO.setVrlVodResultMsg(JsonUtils.toJson(eventTask, true));
                vrlEO.setVrlVodEndTime(DateUtils.now());
                videoRecordLogDao.update(vrlEO);
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
            vrlEO.setVrlVodFileId(fileId);
            vrlEO.setVrlVodFileUrl(fileUrl);
            vrlEO.setVrlVodResultMsg(JsonUtils.toJson(eventTask, true));
            vrlEO.setVrlVodEndTime(DateUtils.now());
            videoRecordLogDao.update(vrlEO);
            //更新记录信息
            String roomId = vrlEO.getVrlRoomId();
            List<VideoRecordEO> vrEOLt = videoRecordDao.getVideoRecordLtByRoomId(roomId);
            for (VideoRecordEO vrEO : vrEOLt) {
                vrEO.setVrStatus(RecordStatus.CONCAT_OK); //拼接完成
                vrEO.setVrEndTime(new Date());
                videoRecordDao.update(vrEO);
            }
            //新增链接信息
            VideoUrlEO vuEO = new VideoUrlEO();
            vuEO.setVuRoomId(roomId);
            vuEO.setVuType(1);
            vuEO.setVuUrl(fileUrl);
            vuEO.setVuBeginTime(DateUtils.now());
            videoUrlDao.insert(vuEO);
        }

        //*************** 队列确认 ***************
        try {
            ConfirmEventsResponse resp = confirmEventsV3Client.confirmEvents(eventHandleLt, authProfile);
        } catch (Exception ex) {
            log.error("", ex);
        }
    }
}
