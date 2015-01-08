package com.cytx.timecardmobile.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.cytx.timecardmobile.bean.TimeCardBean;
import com.cytx.timecardmobile.constants.Constants;
import com.cytx.timecardmobile.service.impl.WebServiceImpl;
import com.cytx.timecardmobile.utility.DataCacheTools;
import com.cytx.timecardmobile.utility.DateTools;
import com.cytx.timecardmobile.utility.FileTools;
import com.cytx.timecardmobile.utility.JsonHelp;
import com.cytx.timecardmobile.utility.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 创建时间：2014年8月18日 下午3:53:07  
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
	private File currentFile;// 当前上传成功的文件
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 检测是否干掉已上传的打卡信息
			case 1:
				if (count < TIME_12_H) {
					count += 60 * 1000;
					handler.sendEmptyMessageDelayed(1, TIME_CLEAR_EVERY);
				} else {
					try {
						// 清除数据
						DataCacheTools.clearCacheDatas();
					} catch (Exception e) {

					}
					// 将计时器复原
					count = 0;
					handler.sendEmptyMessage(1);
				}
				
				break;

			// 检测在有网络的情况下，上传打卡数据
			case 2:
				// 如果有网络，那么上传打卡信息
				if (Utils.checkNetworkInfo(TimeCardService.this)) {
					File fileDir = new File(Constants.CARD_INFO_DIR_NO);
					// 存在目录文件
					if (fileDir != null && fileDir.exists()) {
						noUploadCardFile = fileDir.listFiles();
						// 文件夹为空
						if (noUploadCardFile == null || noUploadCardFile.length == 0) {
							// 继续检测
							handler.sendEmptyMessageDelayed(2, TIME_NETWORK_EVERY);						
						}
						// 文件夹不为空，开始上传
						else {
							currentFile = noUploadCardFile[0];
							// 读取打卡信息
							String cardData = getCardData(currentFile);
							// 转换为TimeCardBean对象
							TimeCardBean timeCardBean = getTimeCardBean(cardData);
							// 开始打卡信息
							punchCard(timeCardBean);
						}
					} else {
						// 若还没有未上传的文件，那么继续检测
						handler.sendEmptyMessageDelayed(2, TIME_NETWORK_EVERY);
					}
				}
				// 如果没有网络，那么隔
				else {
					handler.sendEmptyMessageDelayed(2, TIME_NETWORK_EVERY);
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
		handler.sendEmptyMessage(1);
		// 检测有网时，就上传打卡信息
		handler.sendEmptyMessage(2);
		
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
	 * @param jsonString
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
				handler.sendEmptyMessage(2);		
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
				//if (arg0 == 200) {
					// 上传成功，将currentFile从no文件夹里面转移到yes文件夹里面
					removeFileUploaded(timeCardBean);
					// 间隔1秒再上传下条打卡数据
					handler.sendEmptyMessageDelayed(2, 1000);
				} else {
					// 重新检测是否有网络
					moveFile2Fail(timeCardBean);
					handler.sendEmptyMessage(2);
				}				
			}
		});
	}
	
	/**
	 * 移动文件：打卡信息和拍下打卡家长的照片
	 */
	private void removeFileUploaded(TimeCardBean timeCardBean){
		
		// 新的打卡文件目录
//		File newFileDir = new File(Constants.CARD_INFO_DIR_YES);
//		if (!newFileDir.exists()) {
//			newFileDir.mkdirs();
//		}
//		String newFileName = currentFile.getName();
		currentFile.delete();
		
		// 移动打卡信息文件
//		currentFile.renameTo(new File(newFileDir, newFileName));
//		
//		// 新的拍照文件目录
//		File newFilePicDirFile = new File(Constants.CAMERA_PICTURES_DIR_YES);
//		if (!newFilePicDirFile.exists()) {
//			newFilePicDirFile.mkdirs();
//		}
//		String fileName = timeCardBean.getCaptureImgPath();
//        File oldFile = new File(Constants.CAMERA_PICTURES_DIR_NO + "/" + fileName);
//        oldFile.delete();
        
//		if (oldFile != null && oldFile.exists()) {
//			// 移动照片
//			oldFile.renameTo(new File(newFilePicDirFile, fileName));
//		}
	}
	
	/**
	 * 移动文件：打卡信息和拍下打卡家长的照片
	 */
	private void moveFile2Fail(TimeCardBean timeCardBean){
		// 新的打卡文件目录
		File newFileDir = new File(Constants.CARD_INFO_DIR_FAIL);
		if (!newFileDir.exists()) {
			newFileDir.mkdirs();
		}
		String newFileName = currentFile.getName();
		// 移动打卡信息文件
		currentFile.renameTo(new File(newFileDir, newFileName));
		
//		// 新的拍照文件目录
//		File newFilePicDirFile = new File(Constants.CAMERA_PICTURES_DIR_FAIL);
//		if (!newFilePicDirFile.exists()) {
//			newFilePicDirFile.mkdirs();
//		}
//		String fileName = timeCardBean.getCaptureImgPath();
//		File oldFile = new File(Constants.CAMERA_PICTURES_DIR_FAIL + "/" + fileName);
//		if (oldFile != null && oldFile.exists()) {
//			// 移动照片
//			oldFile.renameTo(new File(newFilePicDirFile, fileName));
//		}
	}

}
