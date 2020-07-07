package test.com.xdf.ucan.remix.video.task.dao;


import com.xdf.ucan.remix.video.task.dao.videorecordlog.VideoRecordLogDao;
import com.xdf.ucan.remix.video.task.dao.videorecordlog.VideoRecordLogEO;
import com.xdf.ucan.remix.video.task.support.SpringBootCfg;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringBootCfg.class})
public class VideoConcatDaoTest {

    @Autowired
    private VideoRecordLogDao videoConcatDao;

    @Test
    public void insert_test() {
        VideoRecordLogEO vrlEO = new VideoRecordLogEO();
        vrlEO.setVrlVrId(1L);
        vrlEO.setVrlVrId(1L);
        vrlEO.setVrlRoomId("123123");
        vrlEO.setVrlUploadRequestId("123");
        vrlEO.setVrlUploadTaskId("123");
        vrlEO.setVrlUploadBeginTime(new Date());
        videoConcatDao.insert(vrlEO);
    }
}
