/*
 * 创建时间：
 * 
 * @author 
 * @version 1.0 文件名称：.java 类说明：BusStopList（访问服务器）
 */
package com.cytx.timecardmobile.lbs;

import java.util.List;

public class BusStopListDto {
	private List<BusStopDto> geofence;
	
	public List<BusStopDto> getGeofence() {
		return geofence;
	}
	
	public void setGeofence(List<BusStopDto> busStopList) {
		this.geofence = busStopList;
	}
}