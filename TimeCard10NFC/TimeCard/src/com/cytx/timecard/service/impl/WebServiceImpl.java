package com.cytx.timecard.service.impl;

import com.cytx.timecard.bean.AttendanceStateBean;
import com.cytx.timecard.bean.TimeCardBean;
import com.cytx.timecard.constants.Constants;
import com.cytx.timecard.service.WebService;
import com.cytx.timecard.utility.TimeCardClient;
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
		params.put("healthstate", timeCardBean.getCreatetime());
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
	 * Upload Reminders
	 * && HealthState
	 */
	@Override
	public void postReminderHealthState(AttendanceStateBean attStateBean, 
			                    AsyncHttpResponseHandler asynchttpresponsehandler) {
		RequestParams params = new RequestParams();
		params.put("machine", attStateBean.getMid());
		params.put("smartid", attStateBean.getCardid());
		params.put("reminder", attStateBean.getReminder());
		params.put("isnormal", attStateBean.getHealthState());
		params.put("createtime", attStateBean.getCreatetime());
		TimeCardClient.post(Constants.HEALTH_STATE_REMINDER_URL, params, asynchttpresponsehandler);
	}

}
