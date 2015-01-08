package com.cytx.timecardmobile.service;

import com.cytx.timecardmobile.bean.TimeCardBean;
import com.cytx.timecardmobile.lbs.BusStopDto;
import com.cytx.timecardmobile.lbs.LocationUpdateMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 创建时间：2014年7月30日 下午1:48:29  
 * 项目名称：TimeCard  
 * @author ben
 * @version 1.0
 * 文件名称：WebService.java  
 * 类说明：与服务器交互的接口
 */
public interface WebService {
	
	// 获取所有学生信息
	void getAllStudentInfo(String machine, AsyncHttpResponseHandler asynchttpresponsehandler);
	
	// 获取心跳包信息
	void getHeartPackageInfo(String machine, AsyncHttpResponseHandler asynchttpresponsehandler);
	
	// 获取心跳包信息
	void postLocationUpdate(LocationUpdateMsg locationUpdate, AsyncHttpResponseHandler asynchttpresponsehandler);
	
	// 学生考勤打卡	
	void timeCardInfo(TimeCardBean timeCardBean, AsyncHttpResponseHandler asynchttpresponsehandler);
	
	// 获取广告图片
	void getAvdPicture(AsyncHttpResponseHandler asynchttpresponsehandler);

	//获取站点列表
	public void getBusStopList(String machineid, AsyncHttpResponseHandler asynchttpresponsehandler);

	//通知到站信息
	void postBusStopArrival(int busStopId, String mid, Long timestamp, AsyncHttpResponseHandler asynchttpresponsehandler);
}