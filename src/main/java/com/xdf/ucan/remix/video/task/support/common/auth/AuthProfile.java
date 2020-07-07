package com.xdf.ucan.remix.video.task.support.common.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthProfile {

    private Long subAppId;

    private String secretId;

    private String secretKey;

    private String region;
}
