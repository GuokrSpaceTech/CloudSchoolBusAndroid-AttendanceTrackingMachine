package com.cytx.timecard.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.cytx.timecard.TimeCardApplicatoin;
import com.cytx.timecard.bean.TimeCardBean;
import com.cytx.timecard.constants.Constants;
import com.cytx.timecard.database.DaoSession;
import com.cytx.timecard.database.TimeRecordEntity;
import com.cytx.timecard.database.TimeRecordEntityDao;
import com.cytx.timecard.service.impl.WebServiceImpl;
import com.cytx.timecard.utility.DataCacheTools;
import com.cytx.timecard.utility.DateTools;
import com.cytx.timecard.utility.FileTools;
import com.cytx.timecard.utility.JsonHelp;
import com.cytx.timecard.utility.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 创建时间：2014年8月18日 下午3:53
 * 项目名称：TimeCard  
 * @author ben
 * @version 1.0
 * 文件名称：TimeCardService.java  
 * 类说明：  
 */
public class TimeCardService extends Service {
	
	// 每12小时检测一次是否需要清除数据
	private final int TIME_12_H = 12 * 60 * 60 * 1000;
	private int count = 0;// 计时器
	private final int TIME_CLEAR_EVERY = 60*1000;// 按分钟累积计时
	private File noUploadCardFile [] ;// 没有上传打卡信息的文件
	private final int TIME_NETWORK_EVERY = 10 * 1000;// 10秒钟检测网络状态
	private final int TIME_PUNCH_CARD_EVERY = 10 * 1000;// 10秒上传一条打卡数据

	private final static int MSG_UPLOAD_TIMERECORD = 2;
	private final static int MSG_CLEAR_LOCALDATA = 1;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 检测是否干掉已上传的打卡信息
			case 1:
				if (count < TIME_12_H) {
					count += 60 * 1000;
					handler.sendEmptyMessageDelayed(MSG_CLEAR_LOCALDATA, TIME_CLEAR_EVERY);
				} else {
					try {
						// Todo 清除Old数据
					} catch (Exception e) {

					}
					// 将计时器复原
					count = 0;
					handler.sendEmptyMessage(MSG_CLEAR_LOCALDATA);
				}
				
				break;

			// 检测在有网络的情况下，上传打卡数据
			case 2:
				// 如果有网络，那么上传打卡信息
				if (Utils.checkNetworkInfo(TimeCardService.this)) {
					DaoSession dbSession = TimeCardApplicatoin.getInstance().mDaoSession;
					List<TimeRecordEntity> timeRecordEntities = dbSession.getTimeRecordEntityDao().loadAll();
					for(TimeRecordEntity timeRecord:timeRecordEntities)
					{
						TimeCardBean bean = new TimeCardBean();
						bean.setMachine(timeRecord.getMachine());
						bean.setHealthstate(timeRecord.getHealthstate());
						bean.setFext(timeRecord.getFext());
						bean.setCreatetime(timeRecord.getCreatetime());
						bean.setFbody(timeRecord.getFbody());
						bean.setSmartid(timeRecord.getCardid());
						bean.setTemperature(timeRecord.getTemperature());
						punchCard(bean);
					}

					handler.sendEmptyMessageDelayed(MSG_UPLOAD_TIMERECORD, TIME_NETWORK_EVERY);
				}
				// 如果没有网络，那么隔继续检测
				else {
					handler.sendEmptyMessageDelayed(MSG_UPLOAD_TIMERECORD, TIME_NETWORK_EVERY);
				}
				break;
			}
			
		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		// 检测是否需要清除已上传打卡数据
		handler.sendEmptyMessage(MSG_CLEAR_LOCALDATA);
		// 检测有网时，就上传打卡信息
		handler.sendEmptyMessage(MSG_UPLOAD_TIMERECORD);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	private String getCardData(File file) {

		StringBuffer sBuffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				sBuffer.append(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sBuffer.toString();
	}
	
	/**
	 * 获取TimeCardBean
	 * @return
	 */
	private TimeCardBean getTimeCardBean(String cardData){
		return JsonHelp.getObject(cardData, TimeCardBean.class);
	}
	
	// 开始打卡
	private void punchCard(final TimeCardBean timeCardBean) {

		WebService webService = WebServiceImpl.getInstance();
		webService.timeCardInfo(timeCardBean, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, org.apache.http.Header[] arg1,
								  byte[] arg2, Throwable arg3) {
				// 重新检测是否有网络
				handler.sendEmptyMessageDelayed(2, TIME_PUNCH_CARD_EVERY);
			}

			@Override
			public void onSuccess(int arg0, org.apache.http.Header[] arg1,
								  byte[] arg2) {
				Map<String, String> maps = new HashMap<String, String>();

				if (arg1 != null && arg1.length != 0) {

					for (int i = 0; i < arg1.length; i++) {
						maps.put(arg1[i].getName(), arg1[i].getValue());
					}
				}

				// 获取header中参数Code的值
				String code = maps.get("Code");
				//System.out.println(code);
				// 说明上传打卡信息成功：code=1
				if (code != null && "1".equals(code)) {
					// 间隔1秒再上传下条打卡数据
					DaoSession dbSession = TimeCardApplicatoin.getInstance().mDaoSession;
					List<TimeRecordEntity> timeRecordEntities =
							dbSession.getTimeRecordEntityDao().queryBuilder()
									.where(TimeRecordEntityDao.Properties.Createtime.eq(timeCardBean.getCreatetime()))
									.limit(1).list();
					if (timeRecordEntities.size() == 1) {
						dbSession.getTimeRecordEntityDao().delete(timeRecordEntities.get(0));
					}

					handler.sendEmptyMessageDelayed(MSG_UPLOAD_TIMERECORD, 1000);
				} else {
					// 重新检测是否有网络
					handler.sendEmptyMessage(MSG_UPLOAD_TIMERECORD);
				}
			}
		});
	}
}
