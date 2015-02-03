package com.tlz.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.text.format.DateUtils;
import android.text.format.Time;

public class TimeUtils {

    public static final long SECOND_IN_MILLIS = 1000;
    public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
    public static final long WEEK_IN_MILLIS = DAY_IN_MILLIS * 7;
    
    public static final long MINUTE_IN_SECOND = 60;
    public static final long HOUR_IN_SECOND = MINUTE_IN_SECOND * 60;
    public static final long DAY_IN_SECOND = HOUR_IN_SECOND * 24;
    public static final long WEEK_IN_SECOND = DAY_IN_SECOND * 7;
	
    public static final SimpleDateFormat DATE_FORMAT_DATE_TIME_FULL						= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_DATE_TIME		 					= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_DATE_TIME_IGNORE_SECOND			= new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_DATE								= new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_TIME								= new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_TIME_IGNORE_SECOND  				= new SimpleDateFormat("HH:mm", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_DATE_TIME_STRING_FULL				= new SimpleDateFormat("yyyyMMddHHmmssS", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_DATE_TIME_STRING 					= new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_DATE_STRING 						= new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_TIME_STRING 						= new SimpleDateFormat("HHmmss", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_YEAR_MONTH_STRING					= new SimpleDateFormat("yyyyMM", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_YEAR_STRING 						= new SimpleDateFormat("yyyy", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_MONTH_STRING 						= new SimpleDateFormat("MM", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_DAY_STRING 						= new SimpleDateFormat("dd", Locale.CHINA);

    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    public static String getCurrentTime(String dateFormat) {
    	return getTime(getCurrentTimeInLong(), new SimpleDateFormat(dateFormat, Locale.CHINA));
    }
    
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }
    
    public static String getCurrentDataTimeFull() {
    	return getTime(getCurrentTimeInLong(), DATE_FORMAT_DATE_TIME_FULL);
    }

    public static String getCurrentDateTime() {
    	return getTime(getCurrentTimeInLong(), DATE_FORMAT_DATE_TIME);
    }
    
    public static String getCurrentDate() {
    	return getTime(getCurrentTimeInLong(), DATE_FORMAT_DATE);
    }
    
    public static String getCurrentDateInString() {
    	return getTime(getCurrentTimeInLong(), DATE_FORMAT_DATE_STRING);
    }
    
    public static String getCurrentTime() {
    	return getTime(getCurrentTimeInLong(), DATE_FORMAT_TIME);
    }
    
    public static String getCurrentDateTimeFullInString() {
    	return getTime(getCurrentTimeInLong(), DATE_FORMAT_DATE_TIME_STRING_FULL);
    }
    
    public static String getCurrentDateTimeInString() {
    	return getTime(getCurrentTimeInLong(), DATE_FORMAT_DATE_TIME_STRING);
    }

    public static String getDateTime(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE_TIME);
    }
    
    public static String getDate(long timeInMillis) {
    	return getTime(timeInMillis, DATE_FORMAT_DATE);
    }
    
    public static String getTime(long timeInMillis) {
    	return getTime(timeInMillis, DATE_FORMAT_TIME);
    }
    
    public static String getDateTimeInString(long timeInMillis) {
    	return getTime(timeInMillis, DATE_FORMAT_DATE_TIME);
    }
    
    public static String getDateInString(long timeInMillis) {
    	return getTime(timeInMillis, DATE_FORMAT_DATE_STRING);
    }
    
    public static String getTimeInString(long timeInMillis) {
    	return getTime(timeInMillis, DATE_FORMAT_TIME);
    }
    
	/**
	 * 判断当前日期是否是当天
	 * @param millisecond
	 * @return
	 */
	public static boolean isToday(long timeInMillis) {
		return DateUtils.isToday(timeInMillis);
	}
	
	/**
	 * 判断当前日期是否是当月
	 * @param millisecond
	 * @return
	 */
	public static boolean isThisMonth(long timeInMillis) {
		Time time = new Time();
		time.set(timeInMillis);
		
		int thenYear = time.year;
		int thenMonth = time.month;
		
		time.set(System.currentTimeMillis());
		return (thenYear == time.year) && (thenMonth == time.month);
	}
	
	/**
	 * 判断当前日期是否是当年
	 * @param millisecond
	 * @return
	 */
	public static boolean isThisYear(long timeInMillis) {
		Time time = new Time();
		time.set(timeInMillis);
		
		int thenYear = time.year;
		
		time.set(System.currentTimeMillis());
		return thenYear == time.year;
	}
	
	
	private TimeUtils() {/*Do not new me*/}
}
