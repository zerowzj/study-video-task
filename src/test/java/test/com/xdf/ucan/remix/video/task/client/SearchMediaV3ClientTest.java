package test.com.xdf.ucan.remix.video.task.client;

import com.xdf.ucan.remix.video.task.client.media.SearchMediaV3Client;
import com.xdf.ucan.remix.video.task.support.SpringBootCfg;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfile;
import com.xdf.ucan.remix.video.task.support.common.auth.AuthProfiles;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringBootCfg.class})
public class SearchMediaV3ClientTest {

    @Autowired
    private SearchMediaV3Client client;

    @Test
    public void test() throws Exception {
        //String streamId = "prod_2_22_VIPGAP200332_54_20200603_TC1797_JN0520575639_1530_1730";
        String streamId = "prod_2_9998_TEST001_7_20200701_TC334_TEST11847677754263969_1550_1650";
        AuthProfile profile = AuthProfiles.getStmVodProfile();

        client.searchMedia(streamId, profile);
    }
}
