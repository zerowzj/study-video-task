package com.xdf.ucan.remix.video.notify.msg;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StreamBeginNotifyMsg extends NotifyMsg {

    private Integer errcode;

    private String errmsg;
}
