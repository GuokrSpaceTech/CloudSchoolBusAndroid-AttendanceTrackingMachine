package com.cytx.timecard.dto;
/**
 * 创建时间：2014年8月8日 下午8:56:40  
 * 项目名称：TimeCard  
 * @author ben
 * @version 1.0
 * 文件名称：HealthStateDto.java  
 * 类说明：  
 */
public class HealthStateDto {
	private String id;
	private String reminder;
    public boolean isSelected = false;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReminder() {
		return reminder;
	}
	public void setReminder(String reminder) {
		this.reminder = reminder;
	}

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
