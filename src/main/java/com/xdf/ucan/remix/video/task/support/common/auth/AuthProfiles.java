package com.xdf.ucan.remix.video.task.support.common.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class AuthProfiles {

    @Bean(name = "recordAuthProfile")
    @ConfigurationProperties(prefix = "record.vod")
    AuthProfile recordAuthProfile() {
        return new AuthProfile();
    }

    @Bean(name = "streamAuthProfile")
    @ConfigurationProperties(prefix = "stream.vod")
    AuthProfile streamAuthProfile() {
        return new AuthProfile();
    }

    @Autowired
    private AuthProfile recordAuthProfile;
    @Autowired
    private AuthProfile streamAuthProfile;

    private static AuthProfile REC_VOD_PROFILE;

    private static AuthProfile STM_VOD_PROFILE;

    @PostConstruct
    public void init() {
        REC_VOD_PROFILE = recordAuthProfile;
        STM_VOD_PROFILE = streamAuthProfile;
    }

    public static AuthProfile getRecVodProfile() {
        return REC_VOD_PROFILE;
    }

    public static AuthProfile getStmVodProfile() {
        return STM_VOD_PROFILE;
    }
}
