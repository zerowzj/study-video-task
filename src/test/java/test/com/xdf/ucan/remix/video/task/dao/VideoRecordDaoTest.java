package test.com.xdf.ucan.remix.video.task.dao;


import com.xdf.ucan.remix.video.task.dao.videorecord.VideoRecordDao;
import com.xdf.ucan.remix.video.task.dao.videorecord.VideoRecordEO;
import com.xdf.ucan.remix.video.task.support.SpringBootCfg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringBootCfg.class})
public class VideoRecordDaoTest {

    @Autowired
    private VideoRecordDao videoRecordDao;

    private List<String> OSS_URL_LT = Lists.newArrayList("https://ucancdn.xdf.cn/1v1/onlineflv/20200526-eLs5Z/20200526-eLs5Z@2020_05_26_09_00_57.flv",
            "https://ucancdn.xdf.cn/1v1/onlineflv/20200526-gKAt6/20200526-gKAt6@2020_05_26_09_01_22.flv",
            "https://ucancdn.xdf.cn/1v1/onlineflv/20200526-vokhU/20200526-vokhU@2020_05_26_09_01_40.flv");

    @Test
    public void insert2_test() {
        OSS_URL_LT.forEach(e -> {
            VideoRecordEO vrEO = new VideoRecordEO();
            vrEO.setVrRoomId("666666");
            vrEO.setVrName(StringUtils.substringAfterLast(e, "/"));
            vrEO.setVrStorePath(e);
            vrEO.setVrStoreType(1);
            vrEO.setVrStatus(1);
            vrEO.setVrBeginTime(new Date());
            videoRecordDao.insert(vrEO);
        });
    }

    @Test
    public void insert_test() {
        VideoRecordEO vrEO = new VideoRecordEO();
        vrEO.setVrRoomId("111");
        vrEO.setVrName("文件名");
        vrEO.setVrStorePath("/data");
        vrEO.setVrStoreType(1);
        vrEO.setVrStatus(1);
        vrEO.setVrBeginTime(new Date());
        videoRecordDao.insert(vrEO);
    }
}
