package com.cytx.timecard.dto;
/**
 * 创建时间：2014年8月8日 下午8:56:40  
 * 项目名称：TimeCard  
 * @author ben
 * @version 1.0
 * 文件名称：HealthStateDto.java  
 * 类说明：  
 */
public class ReminderDto {
	private int id;
	private String reminder;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReminder() {
		return reminder;
	}
	public void setReminder(String reminder) {
		this.reminder = reminder;
	}

}
