package com.xdf.ucan.remix.video.notify.msg;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class NotifyMsg implements Serializable {

    private Integer event_type;

    private Long t;

    private String sign;

    private String stream_id;

    private String stream_param;
}
