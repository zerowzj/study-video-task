package com.xdf.ucan.remix.video.task.support.log;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "TRACK_FILE")
public class TrackLogger {

    public static void info(String s, Object... objects) {
        log.info(s, objects);
    }

    public static void error(String s, Object... objects) {
        log.error(s, objects);
    }
}
