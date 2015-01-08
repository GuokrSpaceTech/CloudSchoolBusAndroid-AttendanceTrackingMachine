package com.cytx.timecardmobile.service.impl;

import com.cytx.timecardmobile.bean.TimeCardBean;
import com.cytx.timecardmobile.constants.Constants;
import com.cytx.timecardmobile.lbs.BusStopDto;
import com.cytx.timecardmobile.lbs.LocationUpdateMsg;
import com.cytx.timecardmobile.service.WebService;
import com.cytx.timecardmobile.utility.TimeCardClient;
import com.cytx.timecardmobile.utility.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 创建时间：2014年7月30日 下午1:49:06  
 * 项目名称：TimeCard  
 * @author ben
 * @version 1.0
 * 文件名称：WebServiceImpl.java  
 * 类说明：WebService的实现类
 */
public class WebServiceImpl implements WebService {
	
	private static WebServiceImpl wsi;
	private WebServiceImpl(){}
	public static WebServiceImpl getInstance(){
		if (wsi == null) {
			wsi = new WebServiceImpl();
		}
		return wsi;
	}

	/**
	 * 获取所有学生信息
	 */
	@Override 
	public void getAllStudentInfo(String machine,
			AsyncHttpResponseHandler asynchttpresponsehandler) {
		RequestParams params = new RequestParams();
		TimeCardClient.get(Constants.SCHOOL_STUDENT_URL + "/machine/" + machine, params, asynchttpresponsehandler);
	}

	/**
	 * 获取心跳包信息
	 */
	@Override
	public void getHeartPackageInfo(String machine,
			AsyncHttpResponseHandler asynchttpresponsehandler) {
		RequestParams params = new RequestParams();
		TimeCardClient.get(Constants.SCHOOL_CHECK_URL + "/machine/" + machine, params, asynchttpresponsehandler);
	}

	/**
	 * 学生考勤打卡
	 */
	@Override
	public void timeCardInfo(TimeCardBean timeCardBean,
			AsyncHttpResponseHandler asynchttpresponsehandler) {
		
		RequestParams params = new RequestParams();
		params.put("machine", timeCardBean.getMachine());
		params.put("smartid", timeCardBean.getSmartid());
		params.put("createtime", timeCardBean.getCreatetime());
		params.put("fbody", timeCardBean.getFbody());
		params.put("healthstate", timeCardBean.getHealthstate());
		TimeCardClient.post(Constants.SMART_CARD_URL, params, asynchttpresponsehandler);
	}

	/**
	 * 获取广告图片
	 */
	@Override
	public void getAvdPicture(AsyncHttpResponseHandler asynchttpresponsehandler) {
		RequestParams params = new RequestParams();
		TimeCardClient.get(Constants.AVD_PICTURE_URL, params, asynchttpresponsehandler);
	}
	
	/**
	 * 上传位置信息
	 */
	@Override
	public void postLocationUpdate(LocationUpdateMsg locationUpdate,
			AsyncHttpResponseHandler asynchttpresponsehandler) {
		RequestParams params = new RequestParams();
		params.put("machine", locationUpdate.getMachine());
		params.put("createtime", locationUpdate.getCreatetime());
		params.put("latitude", locationUpdate.getLatitude());
		params.put("longitude", locationUpdate.getLongitude());
		TimeCardClient.post(Constants.LOC_UPDATE_URL, params, asynchttpresponsehandler);	
	}
	
	/**
	 * 获取站点列表
	 */
	@Override
	public void getBusStopList(String machineid, 
			AsyncHttpResponseHandler asynchttpresponsehandler) {
		RequestParams params = new RequestParams();
		TimeCardClient.get(Constants.BUS_STOP_LIST_URL+"/mid/"+machineid, params, asynchttpresponsehandler);
	}
	
	/**
	 * 通知服务器到达站点附近
	 */
	@Override
	public void postBusStopArrival(int busStopId, String mid, Long timestamp,
			AsyncHttpResponseHandler asynchttpresponsehandler)
	{
		RequestParams params = new RequestParams();
		params.put("createtime", timestamp);
		params.put("geoid", busStopId);
		params.put("mid", mid);
		TimeCardClient.post(Constants.BUS_STOP_ARRIVAL_NOTICE, params, asynchttpresponsehandler);
	}
}
