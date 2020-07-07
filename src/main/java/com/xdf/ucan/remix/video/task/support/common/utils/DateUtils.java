package com.xdf.ucan.remix.video.task.support.common.utils;

import com.xdf.ucan.remix.video.task.support.common.constant.DateFormat;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class DateUtils {

    public static Date now() {
        return DateTime.now().toDate();
    }

    /**
     * 日期转字符串
     */
    public static String toStdDateStr(Date date) {
        return toStr(date, DateFormat.STD_DATE);
    }

    public static String toStdDateTimeStr(Date date) {
        return toStr(date, DateFormat.STD_DATE_TIME);
    }

    public static String toStdDateTimeMsStr(Date date) {
        return toStr(date, DateFormat.STD_DATE_TIME_MS);
    }

    public static String toStr(Date date, DateFormat format) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(format.getValue());
    }

    /**
     * 字符转串日期
     */
    public static Date toDate(Date date, DateFormat format) {
        return null;
    }

    /**
     * 小时计算
     */
    public static Date plusHours(int hours) {
        return plusHours(DateTime.now().toDate(), hours);
    }

    public static Date plusHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        DateTime dt = dateTime.plusHours(hours);
        return dt.toDate();
    }

    /**
     * 分钟计算
     */
    public static Date plusMinutes(int minutes) {
        return plusMinutes(DateTime.now().toDate(), minutes);
    }

    public static Date plusMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        DateTime dt = dateTime.plusMinutes(minutes);
        return dt.toDate();
    }

    /**
     * 获取UNIX时间戳
     */
    public static Long getUnixTimestampSec() {
        DateTimeZone dtZone = DateTimeZone.forID("GMT-0");
        DateTime dateTime = DateTime.now(dtZone);
        return dateTime.getMillis() / 1000;
    }

    private static Long getUtcTimeSec(){
        TimeZone zone = TimeZone.getTimeZone("GMT-0");
        Calendar calendar = Calendar.getInstance(zone);
        return calendar.getTimeInMillis()/1000;
    }

    public static void main(String[] args) {
        log.info("{}", getUnixTimestampSec());
        log.info("{}", getUtcTimeSec());
        log.info("{}", System.currentTimeMillis());
    }
}
