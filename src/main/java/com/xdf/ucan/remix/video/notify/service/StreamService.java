package com.xdf.ucan.remix.video.notify.service;

import com.xdf.ucan.remix.video.notify.StreamId;
import com.xdf.ucan.remix.video.notify.StreamParam;

public interface StreamService {

    void checkInStream(StreamId streamId, StreamParam streamParam);

    void checkInRecord(StreamId streamId, StreamParam streamParam);
}
