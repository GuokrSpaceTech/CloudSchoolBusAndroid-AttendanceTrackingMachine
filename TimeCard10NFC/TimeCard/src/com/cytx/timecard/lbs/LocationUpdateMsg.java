package com.cytx.timecard.lbs;


/**
 * 创建时间：
 * 
 * @author 
 * @version 1.0 文件名称：.java 类说明：考勤卡信息（访问服务器）
 */
public class LocationUpdateMsg {

	private String machine;
	private double latitude;
	private double longitude;
	private long   createtime;


	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long timestamp) {
		this.createtime = timestamp;
	}
}
