package com.cytx.timecard.utility;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.conn.ConnectTimeoutException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import com.cytx.timecard.R;
import com.cytx.timecard.constants.Constants;

/**
 * 创建时间：2014年8月2日 下午11:18:15  
 * 项目名称：TimeCard  
 * @author ben
 * @version 1.0
 * 文件名称：UIUtils.java  
 * 类说明：UI公用方法
 */
public class UIUtils {
	
	/**
	 * Toast
	 * @param context
	 * @param contentZn
	 * @param contentEn
	 */
	public static void showToast(Context context, String contentZn, String contentEn){
		Toast.makeText(context, contentZn + "\n" + contentEn, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Toast
	 * @param context
	 * @param idZn
	 * @param idEn
	 */
	public static void showToastId(Context context, int idZn, int idEn){
		Toast.makeText(context, getStringFromR(context, idZn) + "\n" + getStringFromR(context, idEn), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 获取String值
	 * @param context
	 * @param id
	 * @return
	 */
	public static String getStringFromR(Context context, int id){
		return context.getResources().getString(id);
	}
	
	/**
	 * Toast错误提示
	 */
	public static void showToastError(String message, Context context){
		if(Constants.HOST_ERROR.equals(message)){
			showToastId(context, R.string.connect_failed, R.string.connect_failed_en);
		
		}else if(Constants.SOCKET_TIME_OUT.equals(message)){
			showToastId(context, R.string.connect_timeout, R.string.connect_timeout_en);
			
		}else{
			showToastId(context, R.string.server_error, R.string.server_error_en);
			
		}
	}
	
	/**
	 * 访问服务器失败，返回的错误
	 * @param message
	 * @param context
	 */
	public static void showToastSererError(Throwable throwable, Context context){
		// 超时
		if(throwable instanceof SocketTimeoutException){
			showToastId(context, R.string.connect_timeout, R.string.connect_timeout_en);
			return ;
		
		}
		// 链接失败
		if(throwable instanceof SocketException || throwable instanceof ConnectTimeoutException){
			
			showToastId(context, R.string.connect_failed, R.string.connect_failed_en);
			return ;
		}

		// 服务器未知错误
		if(throwable instanceof UnknownHostException){
			showToastId(context, R.string.server_error, R.string.server_error_en);
			return ;
		}
	}
	
	/**
	 * 获取所有学生信息时，判断返回code的值
	 * @param code
	 * @param context
	 */
	public static void showToastGetStudentsInfoCode(String code, Context context){
		if ("1".equals(code)) {
			showToast(context, "数据下载完成", "Data is downloaded");
		}
		
		if ("-1".equals(code)) {
			showToast(context, "必须传入设备号", "Must pass in the device number");
			return ;
		}
		
		if ("-2".equals(code)) {
			showToast(context, "没有设备", "No device");
			return ;
		}
	}
	
	/**
	 * 获取心跳包，判断返回的code值
	 * @param code
	 * @param context
	 */
	public static void showToastGetHeartPackageCode(String code, Context context){
		if ("1".equals(code)) {
			showToast(context, "心跳包已更新", "Heartbeat is updated");
		}
		
		if ("-1".equals(code)) {
			showToast(context, "必须传入设备号", "Must pass in the device number");
			return ;
		}
		
		if ("-2".equals(code)) {
			showToast(context, "没有设备", "No device");
			return ;
		}
		if ("-3".equals(code)) {
			showToast(context, "必须先调用schoolstudent接口", "Must call schoolstudent interface");
			return ;
		}
	}
	
	/**
	 * 考勤打卡，判断返回的code值
	 * @param code
	 * @param context
	 */
	public static void showToastPunchCardCode(String code, Context context){
		
		if ("1".equals(code)) {
			showToast(context, "打卡成功", "Check in success");
			return ;
		}
		
		if ("-1".equals(code)) {
			showToast(context, "必须输入卡号", "Must input smartid");
			return ;
		}
		
		if ("-2".equals(code)) {
			showToast(context, "时间格式不对", "Time format is wrong");
			return ;
		}
		
		if ("-3".equals(code)) {
			showToast(context, "必须输入打卡时间", "Must input time");
			return ;
		}
		
		if ("-4".equals(code)) {
			showToast(context, "接送人头像不能为空", "Recievers' portrait can't be empty");
			return ;
		}
		if ("-5".equals(code)) {
			showToast(context, "该卡不存在", "Card isn't exist");
			return ;
		}
		if ("-6".equals(code)) {
			showToast(context, "文件写入失败", "File is written to failure");
			return;
		}
		if ("-7".equals(code)) {
			showToast(context, "考勤机和学校不匹配", "Attendance machine does not match the school");
			return;
		}
		if ("-8".equals(code)) {
			showToast(context, "学校没有该设备", "School don't have the equipment");
			return;
		}
		
	}
	
	/**
	 * 获取ProgressDialog
	 * @param context
	 * @return
	 */
	public static ProgressDialog getProgressDialog(Context context){
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage("正在上传打卡信息，请稍等...\nUploading attendance infomation...");
		return dialog;
	}
	
	/**
	 * 自定义的错误提示对话框
	 */
	public static void showCustomDialog(Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}
	
	private void hideSystemUI(View mView) {
		// Set the IMMERSIVE flag.
		// Set the content to appear under the system bars so that the content
		// doesn't resize when the system bars hide and show.
		mView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
		        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
		        | View.SYSTEM_UI_FLAG_IMMERSIVE);
	}
}
