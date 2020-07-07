package com.xdf.ucan.remix.video.notify.msg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordNotifyMsg extends NotifyMsg {

    private String file_id;

    private String file_format;

    private String video_url;

    private Long file_size;

    private Long duration;
}
