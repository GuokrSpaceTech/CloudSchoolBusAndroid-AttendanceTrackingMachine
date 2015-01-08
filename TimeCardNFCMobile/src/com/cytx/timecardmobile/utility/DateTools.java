package com.cytx.timecardmobile.utility;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

public class DateTools {
	
	
	/**
	 * 
	 * @param form 显示时间的格式，如："yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateForm(String form, long time){
		SimpleDateFormat df = new SimpleDateFormat(form);
		Timestamp now = new Timestamp(time);
		String str = df.format(now);

		return str;
	}
	
	/**
	 * 获得当前日期
	 * 
	 * @return 
	 */
	public static String getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		String MONTH = isHaseZero(String.valueOf((cal.get(Calendar.MONTH) + 1)));
		int YEAR = cal.get(Calendar.YEAR);
		String DAY = isHaseZero(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		return YEAR + "-" + MONTH + "-" + DAY;
	}
	
	/**
	 * 得到月份
	 * @return
	 */
	public static String getMonth(){
		Calendar cal = Calendar.getInstance();
		String MONTH = isHaseZero(String.valueOf((cal.get(Calendar.MONTH) + 1)));
		return MONTH;
	}
	
	/**
	 * 得到天
	 * @return
	 */
	public static String getDay(){
		Calendar cal = Calendar.getInstance();
		String DAY = isHaseZero(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		return DAY;
	}
	
	/**
	 * 获取档期时间
	 * @return
	 */
	public static String getCurrentTime(){
		Calendar c = Calendar.getInstance();
		String hour = isHaseZero(String.valueOf(c.get(Calendar.HOUR_OF_DAY))); 
		String minute = isHaseZero(String.valueOf(c.get(Calendar.MINUTE))); 
		return hour + ":" + minute;
	}
	
	/**
	 * 判断数字前面是否有0
	 * 
	 * @param number
	 * @return
	 */
	public static String isHaseZero(String number) {
		if (number.length() == 1 && Integer.parseInt(number) < 10) {
			number = "0" + number;
		}
		return number;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}
	
	/**
	 *  通过日期获取星期
	 * @param year_month_day
	 * @return
	 */
	public static String getWeekFromDate(String year_month_day, String dayNames[]) {
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");

		Calendar calendar = Calendar.getInstance();
		Date date = new Date();

		try {
			date = sdfInput.parse(year_month_day);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0)
			dayOfWeek = 0;

		return dayNames[dayOfWeek];
	}
	
	/**
	 * 得到两日期之间相差的天数
	 * @param startDate:yyyy-MM-dd
	 * @param endDate:yyyy-MM-dd
	 * @return
	 */
	public static long getDaysBetween2Date(String startDate, String endDate){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    Date start = null, end = null;
	    try {
	      start = format.parse(startDate);
	      end = format.parse(endDate);
	    } catch (ParseException e) {
	      e.printStackTrace();
	    }

	    long diff = end.getTime() - start.getTime();
	    long days = diff / (24 * 60 * 60 * 1000);
	    return days;
	}
	
	
}
