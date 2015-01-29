package com.cytx.timecard.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cytx.timecard.R;
import com.cytx.timecard.TimeCardApplicatoin;
import com.cytx.timecard.MainActivity;
import com.cytx.timecard.constants.Constants;
import com.cytx.timecard.dto.AllStudentInfoDto;
import com.cytx.timecard.dto.HealthStateDto;
import com.cytx.timecard.dto.StudentDto;
import com.cytx.timecard.dto.TeacherDto;
import com.cytx.timecard.service.WebService;
import com.cytx.timecard.service.impl.WebServiceImpl;
import com.cytx.timecard.utility.DataCacheTools;
import com.cytx.timecard.utility.DateTools;
import com.cytx.timecard.utility.FileTools;
import com.cytx.timecard.utility.JsonHelp;
import com.cytx.timecard.utility.UIUtils;
import com.cytx.timecard.utility.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * 创建时间：2014年7月30日 下午8:51:55 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：SettingDialog.java 类说明： 设置界面
 */
public class SettingDialog extends Dialog implements OnClickListener {

	private RelativeLayout updateRelativeLayout;
	private RelativeLayout startRelativeLayout;
	private RelativeLayout shutdownRelativeLayout;
	private RelativeLayout syncDataRelativeLayout;
	private LinearLayout   loadingLinearLayout;

	private TextView  startTextView;
	private TextView  shutdownTextView;
	private ImageView updateImageView;
    private TextView  loadingTextView;
    
    private List<StudentDto> studentList;// 所有学生的信息
	private Map<String, StudentDto> studentMap;// 将list 转为map；
	private AllStudentInfoDto allStudentInfoDto;
	private List<LessionDto> reminderList; //所有的提醒
	private List<TeacherDto> teacherList; //所有教师信息
	private Map<String,TeacherDto>  teacherMap;
	
	private Handler handler;
	private Context context;
	private int type = -1;// type为1时，弹出的是开机时间框；为0时，弹出的是关机时间框

	public SettingDialog(Context context, Handler handler) {
		super(context, R.style.style_dialog_setting);
		this.handler = handler;
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_setting);
		initViews();
		initDatas();
	}

	// 初始化view控件
	private void initViews() {

		startRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout4);
		startRelativeLayout.setOnClickListener(this);
		shutdownRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout5);
		shutdownRelativeLayout.setOnClickListener(this);
		updateRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout6);
		updateRelativeLayout.setOnClickListener(this);
		syncDataRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout7);
		syncDataRelativeLayout.setOnClickListener(this);
		loadingLinearLayout =(LinearLayout) findViewById(R.id.linearLayout_loading);
		loadingLinearLayout.setVisibility(View.GONE);
		loadingLinearLayout.setOnTouchListener(new OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		loadingTextView = (TextView) findViewById(R.id.textView_loading);

		startTextView = (TextView) findViewById(R.id.textView_start);
		shutdownTextView = (TextView) findViewById(R.id.textView_shutdown);
		updateImageView = (ImageView) findViewById(R.id.imageView_update_tip);

	}

	// 初始化数据
	private void initDatas() {
		String startTime = TimeCardApplicatoin.getInstance()
				.getStringFromShares("powerOn", "06:00");
		String shutdownTime = TimeCardApplicatoin.getInstance()
				.getStringFromShares("powerOff", "18:00");
		startTextView.setText(startTime);
		shutdownTextView.setText(shutdownTime);
		// 是否需要更新
		if (Constants.IS_NEED_UPDATE) {
			updateImageView.setVisibility(View.VISIBLE);
		} else {
			updateImageView.setVisibility(View.GONE);
		}
	}

	// 检测是否有最新的APK版本
	private void checkVersion() {
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					UmengUpdateAgent.showUpdateDialog(context,
							updateInfo);
					Constants.IS_NEED_UPDATE = true;
					break;
				case UpdateStatus.No: // has no update
					UIUtils.showToast(context,
							"太棒了，你使用是已是最新版本！",
							"Great, you are using the latest version!");
					Constants.IS_NEED_UPDATE = false;
					updateImageView.setVisibility(View.GONE);
					break;
				case UpdateStatus.NoneWifi: // none wifi
					UIUtils.showToast(context,
							"没有wifi连接，只在wifi下更新！",
							"No wifi connection, only update on wifi!");
					break;
				case UpdateStatus.Timeout: // time out
					UIUtils.showToast(context, "亲，你的网络不给力哦！",
							"Pro, the network signal is bad!");
					break;
				}
			}
		});
		
		UmengUpdateAgent.update(context);
	}
	
	// 加载所有学生的信息
	private void syncData()
	{	
			WebService webService = WebServiceImpl.getInstance();
			webService.getAllStudentInfo(Utils.getMachineNum(context),
					new AsyncHttpResponseHandler() {

						@Override
						public void onFinish() {
							super.onFinish();
						}

						@Override
						public void onStart() {
							super.onStart();
							loadingLinearLayout.setVisibility(View.VISIBLE);
							loadingTextView.setText("数据加载中...\nLoading...");
							cancelTimeOperation();
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2,
								Throwable arg3) {
							if (arg3 != null) {
								UIUtils.showToastSererError(arg3,context);
							}
							cancelTimeOperation();
							loadingLinearLayout.setVisibility(View.GONE);
							loadingTextView.setText("同步失败，请稍后重试！");
							// 如果加载学生数据失败，那么就用旧的数据
							//String oldStudentInfo = FileTools.readFileByLines(
									//Constants.STUDENT_INFO_DIR + "/"
										//	+ Constants.STUDENT_INFO_FILE_NAME, "");

							//studentList = JsonHelp.getListObject(oldStudentInfo, StudentDto.class);
							//studentMap = DataCacheTools.list2Map(studentList);

						}

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							cancelTimeOperation();
							loadingLinearLayout.setVisibility(View.GONE);
							Map<String, String> maps = new HashMap<String, String>();

							if (arg1 != null && arg1.length != 0) {

								for (int i = 0; i < arg1.length; i++) {
									maps.put(arg1[i].getName(), arg1[i].getValue());
								}
							}

							// 获取header中参数Code的值
							String code = maps.get("Code");
							if (code == null) {
								return;
							}
							// code=1时，成功；否则失败
							if ("1".equals(code)) {

								String studentInfo = new String(arg2);
								// 得到所有学生的信息
								allStudentInfoDto = JsonHelp.getObject(studentInfo,
										AllStudentInfoDto.class);

								if (allStudentInfoDto != null) {

									studentList = allStudentInfoDto.getStudent();
									studentMap = DataCacheTools.list2Map(studentList);
									reminderList = (allStudentInfoDto);
									teacherList =allStudentInfoDto.getTeacher();
									teacherMap=DataCacheTools.list2tMap(teacherList);
									// 将学生信息保存到本地
									FileTools.save2SDCard(
											Constants.STUDENT_INFO_DIR,
											Constants.STUDENT_INFO_FILE_NAME,
											studentInfo);
									
									MainActivity activity = (MainActivity) context;
									activity.allStudentInfoDto = allStudentInfoDto;
									activity.studentMap = studentMap;
									activity.studentList = studentList;
									activity.teacherList=teacherList;
									activity.teacherMap=teacherMap;
									activity.reminderList=reminderList;
									
									//activity.reminderList = allStudentInfoDto.getHealthstate();
									
									handler.sendEmptyMessage(Constants.MESSAGE_ID_UPDATE_UI);
								}
							}	
							UIUtils.showToastGetStudentsInfoCode(code,context);
						}
					});
		return;
	}

	@Override
	public void onClick(View arg0) {

		cancelTimeOperation();

		switch (arg0.getId()) {
		// 开机
		case R.id.relativeLayout4:
			String startTime = startTextView.getText().toString();
			String[] arrs = startTime.split(":");
			type = 1;
			TimePickerDialog timePickerDialog = new TimePickerDialog(context,
					onTimeSetListener, Integer.valueOf(arrs[0]),
					Integer.valueOf(arrs[1]), true);
			timePickerDialog.show();

			break;
		// 关机
		case R.id.relativeLayout5:
			String shutdownTime = shutdownTextView.getText().toString();
			String[] arrs2 = shutdownTime.split(":");
			type = 0;
			TimePickerDialog timePicker2Dialog = new TimePickerDialog(context,
					onTimeSetListener, Integer.valueOf(arrs2[0]),
					Integer.valueOf(arrs2[1]), true);
			timePicker2Dialog.show();
			break;
		// APK更新
		case R.id.relativeLayout6:

			checkVersion();

			break;

		case R.id.relativeLayout7:
			
			syncData();
			
			break;
		default:
			break;
		}
	}

	TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			// 得到开机时间
			if (type == 1) {
				String startTime = DateTools.isHaseZero(hourOfDay + "") + ":"
						+ DateTools.isHaseZero(minute + "");
				startTextView.setText(startTime);
				TimeCardApplicatoin.getInstance().setStringToShares("powerOn",
						startTime);
			}

			// 得到关机时间
			if (type == 0) {
				String shutdownTime = DateTools.isHaseZero(hourOfDay + "")
						+ ":" + DateTools.isHaseZero(minute + "");
				shutdownTextView.setText(shutdownTime);
				TimeCardApplicatoin.getInstance().setStringToShares("powerOff",
						shutdownTime);
			}

		}

	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:
			cancelTimeOperation();
			break;

		default:
			break;
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		cancelTimeOperation();
	}

	// 重新计时
	private void cancelTimeOperation() {
		handler.removeMessages(Constants.MESSAGE_ID_IDLE_DEVICE);
		handler.sendEmptyMessageDelayed(Constants.MESSAGE_ID_IDLE_DEVICE,
				Constants.TIME_NO_OPERATION);
	}

}
