package com.zm.provider.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具类
 * @author yp-tc-m-7129
 *
 */
public class LocalDateUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalDateUtils.class);


    public static final String YYYYMMDD = "yyyyMMdd";

    public static final String YYMMDD = "yyMMdd";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYYMMDDHHmmss = "yyyyMMddHHmmss";

    public static final String yyyyMMddHHmmssSSS = "yyyyMMddHHmmss";

    public static final String YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public static final String HH_mm_ss = "HH:mm:ss";

    public static final String HHMMSS = "HHmmss";


    /**
     * 日期转为字符串 (默认转为yyyy-MM-dd)
     * @param localDate
     * @return
     */
    public static String formatDate(LocalDate localDate){
        CheckUtils.notEmpty(new Object[]{localDate},"localDate");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        return localDate.format(dtf);
    }

    /**
     * 日期转为字符串 (指定格式)
     * @param localDate
     * @param pattern
     * @return
     */
    public static String formatDate(LocalDate localDate,String pattern){
        CheckUtils.notEmpty(new Object[]{localDate,pattern},"localDate","pattern");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(dtf);
    }

    /**
     * 字符串转化为日期 (默认转为yyyy-MM-dd)
     * @param localDateStr
     * @return
     */
    public static LocalDate parseDate(String localDateStr){
        CheckUtils.notEmpty(new Object[]{localDateStr},"localDateStr");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        return LocalDate.parse(localDateStr,dtf);
    }

    /**
     * 字符串转化为日期 (指定格式)
     * @param localDateStr
     * @param pattern
     * @return
     */
    public static LocalDate parseDate(String localDateStr,String pattern){
        CheckUtils.notEmpty(new Object[]{localDateStr,pattern},"localDateStr","pattern");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(localDateStr,dtf);
    }

    /**
     * 时间转为字符串 (默认格式：yyyy-MM-dd HH:mm:ss)
     * @param localDateTime
     * @return
     */
    public static String formatDateTime(LocalDateTime localDateTime){
        CheckUtils.notEmpty(new Object[]{localDateTime},"localDateTime");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_mm_ss);
        return localDateTime.format(dtf);
    }

    /**
     * 时间转为字符串 (指定格式)
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String formatDateTime(LocalDateTime localDateTime,String pattern){
        CheckUtils.notEmpty(new Object[]{localDateTime,pattern},"localDateTime","pattern");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dtf);
    }

    /**
     * 字符串转化为时间 (默认格式：yyyy-MM-dd HH:mm:ss)
     * @param localDateTimeStr
     * @return
     */
    public static LocalDateTime parseDateTime(String localDateTimeStr){
        CheckUtils.notEmpty(new Object[]{localDateTimeStr},"localDateTimeStr");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_mm_ss);
        return LocalDateTime.parse(localDateTimeStr,dtf);
    }

    /**
     * 字符串转化为时间 (指定格式)
     * @param localDateTimeStr
     * @param pattern
     * @return
     */
    public static LocalDateTime parseDateTime(String localDateTimeStr, String pattern){
        CheckUtils.notEmpty(new Object[]{localDateTimeStr,pattern},"localDateTimeStr","pattern");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(localDateTimeStr,dtf);
    }


    /**
     * 时间转为字符串 (默认格式：HH:mm:ss)
     * @param localTime
     * @return
     */
    public static String formatTime(LocalTime localTime){
        CheckUtils.notEmpty(new Object[]{localTime},"localTime");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(HH_mm_ss);
        return localTime.format(dtf);
    }

    /**
     * 时间转为字符串 (指定格式)
     * @param localTime
     * @param pattern
     * @return
     */
    public static String formatTime(LocalDateTime localTime,String pattern){
        CheckUtils.notEmpty(new Object[]{localTime,pattern},"localTime","pattern");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return localTime.format(dtf);
    }

    /**
     * 字符串转化为时间 (默认格式：HH:mm:ss)
     * @param localTimeStr
     * @return
     */
    public static LocalTime parseTime(String localTimeStr){
        CheckUtils.notEmpty(new Object[]{localTimeStr},"localTimeStr");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(HH_mm_ss);
        return LocalTime.parse(localTimeStr,dtf);
    }

    /**
     * 字符串转化为时间 (指定格式)
     * @param localTimeStr
     * @param pattern
     * @return
     */
    public static LocalTime parseTime(String localTimeStr, String pattern){
        CheckUtils.notEmpty(new Object[]{localTimeStr,pattern},"localTimeStr","pattern");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return LocalTime.parse(localTimeStr,dtf);
    }

    /***
     * D+1获取下一个对账日期
     * @param channelNo 通道编号
     * @param cutTime 切日时间
     * @return
     */
    public static LocalDate getNextDate(String channelNo,LocalDateTime currentDateTime,LocalTime cutTime){
        LOGGER.info("准备获取下一个对账日期 channelNo={},currentDateTime={}, cutTime={}",
                channelNo,currentDateTime, cutTime);
        CheckUtils.notEmpty(currentDateTime, "当前日期");
        LocalDateTime nextDateTime = currentDateTime.plusDays(1);
        LocalTime currentTime = nextDateTime.toLocalTime();
        //切日时间不为空，当前时间超过切日时间，则取当前日期向后移2天（即： 下一日期的下一日期）
        if (cutTime != null && !"00:00:00".equals(formatTime(cutTime)) && currentTime.isAfter(cutTime)) {
            nextDateTime = nextDateTime.plusDays(1);
        }
        return  nextDateTime.toLocalDate();
    }

    /***
     *  是否为通道对账日期
     * @param channelNo 通道编号
     * @return
     */
    public static boolean isCheckDate(String channelNo,LocalDate checkDate){
        LOGGER.info("是否为通道对账日期 channelNo={},currentDateTime={}",
                channelNo,checkDate);
        CheckUtils.notEmpty(checkDate, "当前日期");
        return true;
    }

    /**
	 * 获取指定日期的后一天日期前一天日期--前几天需要负数
	 * @param start
	 * @param day
	 * @return
	 */
	public static LocalDate getBeforeAfterNDaysDate(LocalDate start, Integer day) {
		return start.plusDays(day);
	}

	/**
	 * 获取开始和截止日期之间的日期list，包含开始和截止
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<LocalDate> getBetweenDatesContainStartAndEnd(LocalDate start, LocalDate end) {
		 List<LocalDate> result = new ArrayList<LocalDate>();
		 end = end.plusDays(1);
	     while (start.isBefore(end)) {
	        result.add(start);
	        start = start.plusDays(1);
	     }
	     return result;
	}

	/**
	 * 获取对应时间延迟时间(分钟)
	 * @param date
	 * @param dateDelayMinutes
	 * @return
	 */
	public static LocalDateTime getFetchDelayDate(LocalDateTime date, Integer dateDelayMinutes) {
		return date.minusMinutes(dateDelayMinutes);
	}

    /**
     * 获取对应时间延迟时间(秒)
     * @param date
     * @param dateDelaySeconds
     * @return
     */
    public static LocalDateTime minusSeconds(LocalDateTime date, Integer dateDelaySeconds) {
        return date.minusSeconds(dateDelaySeconds);
    }

    /**
     * 时间戳转换为yyyyMMddHHmmss
     * @param timeTamp
     * @return
     */
    public static Long timeTampParse(Long timeTamp) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timeTamp), ZoneOffset.ofHours(8));
        return Long.valueOf(LocalDateUtils.formatDateTime(localDateTime,YYYYMMDDHHmmss));
    }
}
