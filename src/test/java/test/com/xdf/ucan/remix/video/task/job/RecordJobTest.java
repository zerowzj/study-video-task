package test.com.xdf.ucan.remix.video.task.job;

import com.xdf.ucan.remix.video.task.job.record.RecordConcatJob;
import com.xdf.ucan.remix.video.task.job.record.RecordUploadJob;
import com.xdf.ucan.remix.video.task.support.SpringBootCfg;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {SpringBootCfg.class})
//@Profile("my_test")
public class RecordJobTest {

//    @Autowired
//    private RecordUploadJob recordUploadJob;
//    @Autowired
//    private RecordConcatJob recordConcatJob;
//
//    @Test
//    public void test() {
//        List<String> strLt = Lists.newArrayList("a", "b");
//        String[] strArr = strLt.toArray(new String[strLt.size()]);
//        System.out.println(strArr);
//    }
//
//    @Test
//    public void doUpload_test() {
//        recordUploadJob.doUpload();
//    }
//
//    @Test
//    public void doConcat_test() {
//        recordConcatJob.doConcat();
//    }

    public static void main(String[] args) {
        Date now = new Date();
        log.info("{}", now.getTime());
        log.info("{}", System.currentTimeMillis());
    }
}
