package com.cytx.timecardmobile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;

import android.media.AudioManager;
import android.media.SoundPool;

import android.nfc.NfcAdapter;
import android.nfc.Tag;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cytx.timecardmobile.R;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import com.cytx.timecardmobile.TimeCardApplicatoin;
import com.cytx.timecardmobile.bean.TimeCardBean;
import com.cytx.timecardmobile.constants.Constants;
import com.cytx.timecardmobile.dto.AllStudentInfoDto;
import com.cytx.timecardmobile.dto.AvdDto;
import com.cytx.timecardmobile.dto.ClassInfoDto;
import com.cytx.timecardmobile.dto.HealthStateDto;
import com.cytx.timecardmobile.dto.HeartPackageDto;
import com.cytx.timecardmobile.dto.RecieverDto;
import com.cytx.timecardmobile.dto.SmartCardInfoDto;
import com.cytx.timecardmobile.dto.StudentDto;
import com.cytx.timecardmobile.dto.TeacherDto;
import com.cytx.timecardmobile.lbs.BusStopDto;
import com.cytx.timecardmobile.lbs.BusStopListDto;
import com.cytx.timecardmobile.service.TimeCardService;
import com.cytx.timecardmobile.service.WebService;
import com.cytx.timecardmobile.service.impl.WebServiceImpl;
import com.cytx.timecardmobile.utility.DataCacheTools;
import com.cytx.timecardmobile.utility.DateConvertTool;
import com.cytx.timecardmobile.utility.DateTools;
import com.cytx.timecardmobile.utility.FileTools;
import com.cytx.timecardmobile.utility.JsonHelp;
import com.cytx.timecardmobile.utility.UIUtils;
import com.cytx.timecardmobile.utility.Utils;
import com.cytx.timecardmobile.widget.HorizontalListView;
import com.cytx.timecardmobile.widget.ReceiverAdapter;
import com.cytx.timecardmobile.widget.SettingDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * 有时间，可以对此界面进行重构：将界面分四个分布自定义RelativeLayout写 
 * 好处是：代码不会看起来这么臃肿
 * 
 * @author Ben
 * 
 *         2014年8月14日 下午11:45:21
 */

public class MainActivity extends Activity implements OnClickListener {	

	// top界面
	private TextView dateTextView;// 日期
	private TextView timeTextView;// 时间
	private TextView weekTextView;// 星期
	private TextView weekEnTextView;// week
	private ImageView portraitImageView;// 学生头像
	private ProgressBar studentProgressBar;// 加载学生头像的progressbard
	private TextView nameTextView;// 学生姓名
	private TextView classTextView;// 班级
	private TextView schoolTextView;// 学校
	private TextView cardTextView;// 卡号
	private HeartPackageDto heartPackageDto;// 心跳包信息
	// Middle
	private HorizontalListView horizontalListView;
	private ReceiverAdapter receiverAdapter;

	private LinearLayout middleLinearoutLayout;
	// bottom
	private SurfaceView surfaceView;// 拍照
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	private File picture;// 拍照包保存到本地的照片文件
	private ImageView settingImgView;// 中间的图片

	// avd
	private ImageView avdImageView;// 广告
	private RelativeLayout advRelativeLayout;

	// Setting Dialog
	private SettingDialog settingDialog;// 设置界面

    // Data loading prompt	
	private FrameLayout loadingLinearLayout;// 数据加载中
	private TextView loadingTextView;// 数据加载中

	private MainHandler mainHandler;// 主Handler
	
	//Data Caches and data structures
	public AllStudentInfoDto allStudentInfoDto;
	public List<HealthStateDto> reminderList; //所有的提醒
	public List<StudentDto> studentList;// 所有学生的信息
	public List<TeacherDto> teacherList; //所有教师信息
	public List<BusStopDto> busStopList;
	private AvdDto avdDto;// 广告信息
	private String cardNum;// 卡号
	private String base64;// 图片信息
	private StudentDto currentStudentDto;// 当前学生信息
	public Map<String, StudentDto> studentMap;// 将list 转为map；
	public Map<String,TeacherDto>  teacherMap;
	private TimeCardBean currentTimeCardBean;// 本次打卡信息
	
	private boolean isCameraRunning = false;
	private SoundPool sp;  
	private int soundId;
	private LocationClient mLocationClient;
	private NotifyLister mNotifyLister;
	
	// 自定义TextView替换toast的功能：目的是缩短提示信息显示的时间
	private TextView toastTextView;
	private ToastThread toastThread;

	private PalmTask palmTask;
	
	//NFC Related
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置无标题、全屏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		// 屏幕常亮
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// 初始化view控件
		initViews();
		
		// 初始化各种listener监听事件
		initListeners();
		
		// 初始化数据
		initDatas();
		
		// 检测是否有新版本
		checkVersion();
	
		// Init NFC
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
        	//UIUtils.showToast(getApplicationContext(), R.string.app_name, R.string.app_name);
            //finish();
            //return;
        }

        if(mAdapter != null)
            mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        
	}
	
    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }       
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.removeView(toastTextView);
		stopLBS();
	}
	
	@Override
	protected void onStart() {
		startLBS();
		
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	/*
	 * NFC TAG-DISCOVERED Events comes in a new Intent. See the main xml for the intent filter settings.
	 */
    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }
    
    /*
     * This is the main handling of the user swipe the NFC card
     */
    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
        	
        	//Restart all timers
        	cancelTimeOperation();
        	
        	//At this moment we only cares about the tag id
            Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        
            cardNum = getNFCCardID(tag);
            
            
			FileTools.append2SDCard(
					Constants.STUDENT_INFO_DIR,
					Constants.CARD_ID_RECORD,
					cardNum + "\n");
                
            if(cardNum !=null || cardNum != "")
            {
            	 Object currentObject=getCurrentInfo(cardNum);
                 
                 if(currentObject instanceof StudentDto)
                 {
                 	StudentDto studentDto=(StudentDto)currentObject;
                 	loadStudentInfoUI(studentDto);
                 	loadReceiverInfoUI(studentDto);
                 	takePic();
                 	
                 }
                 else if(currentObject instanceof TeacherDto) {
                 	TeacherDto teacherDto=(TeacherDto)currentObject;
                 	loadTeacherInfoUI(teacherDto);
                 	takePic();
                 	
                 	
     			}
                 else {
                 	clearUI();
     				showTextViewToast("此卡不存在！", "Card is not exist!");
     			}
        }
    }
   }
	protected void loadTeacherInfoUI(TeacherDto teacherDto) {
		middleLinearoutLayout.setVisibility(View.GONE);
		FileTools.loadImage(portraitImageView, 
				teacherDto.getAvatar(),
				            Constants.STUDENT_PORTRAIT, 
				            teacherDto.getTeacherid() + "_"  + ".jpg", 
				            studentProgressBar, 
				            true);
		// 学生姓名
		//right_top_layoutLayout.setVisibility(View.GONE);
		//bottom_left_outLayout.setVisibility(View.GONE);
		nameTextView.setText(teacherDto.getTeachername());

		// 学生班级：因为可能存在两个或多个班级
		String classInfoDtos = teacherDto.getClassname();
		classTextView.setText(classInfoDtos);
		schoolTextView.setText("");
		cardTextView.setText("卡号：" + cardNum);
		
		//recevceNoteView.setText("");
		//recevceNoteView.setText("");
		if(receiverAdapter != null){
			
			receiverAdapter.setReceiverList(new ArrayList<RecieverDto>());
			receiverAdapter.notifyDataSetChanged();
		}
	}
	protected void loadReceiverInfoUI(StudentDto studentDto) {
		//recevceNoteView.setText(R.string.left_bottom_title_2);
		//recevceView.setText(R.string.left_bottom_title_1);
	//	recevceNoteView.setText("");
		middleLinearoutLayout.setVisibility(View.VISIBLE);
		
		List<RecieverDto> receiverList = studentDto.getReceiver();
		if (receiverAdapter == null) {
			receiverAdapter = new ReceiverAdapter(MainActivity.this, receiverList);
			horizontalListView.setAdapter(receiverAdapter);
		} else {
			receiverAdapter.setReceiverList(receiverList);
			receiverAdapter.notifyDataSetChanged();
		}
		
	}
    	protected void loadStudentInfoUI(StudentDto studentDto) {
    		//	String filename = studentDto.getStudentid()
    				//	+ "_" + studentDto.getPid() + ".jpg";
    			//String url = studentDto.getAvatar();
    			//right_top_layoutLayout.setVisibility(View.VISIBLE);
    			//bottom_left_outLayout.setVisibility(View.VISIBLE);
    			FileTools.loadImage(portraitImageView, 
    					studentDto.getAvatar(),
    					            Constants.STUDENT_PORTRAIT, 
    					            studentDto.getStudentid() + "_" + studentDto.getPid() + ".jpg", 
    					            studentProgressBar, 
    					            true);
    			// 学生姓名
    			nameTextView.setText(studentDto.getCnname());

    			// 学生班级：因为可能存在两个或多个班级
    			List<ClassInfoDto> classInfoDtos = studentDto.getClassinfo();
    			if (classInfoDtos != null && classInfoDtos.size() != 0) {
    				StringBuffer sBuffer = new StringBuffer();
    				for (int i = 0; i < classInfoDtos.size(); i++) {
    					sBuffer.append(classInfoDtos.get(i).getClassname()).append(",");
    				}
    				String classsString = sBuffer.toString();
    				classTextView.setText(classsString.substring(0,
    						classsString.length() - 1));
    			}
    			// 学生学校名称
    			schoolTextView.setText(studentDto.getSchoolname());
    			cardTextView.setText("卡号：" + cardNum);
    		}
        protected Object getCurrentInfo(String cardNum)
    	{
    		if(allStudentInfoDto==null)
    		{
    			return null;
    		}
    		
    		List<StudentDto> studentlist=allStudentInfoDto.getStudent();
    		List<TeacherDto> teacherlist=allStudentInfoDto.getTeacher();
    		for(int i=0;i<studentlist.size();i++)
    		{
    			StudentDto studentdto=studentlist.get(i);
    			List<SmartCardInfoDto>smartcardinfolist=studentdto.getSmartcardinfo();
    			
    			for(int j=0;j<smartcardinfolist.size();j++)
    			{
    				SmartCardInfoDto smartcardinfo=smartcardinfolist.get(j);
    				if(cardNum.equals(smartcardinfo.getCardid()))
    				{
    					return studentdto;
    				}
    			}
    			
    		}
    		
    		
    		for(int i=0;i<teacherlist.size();i++)
    		{
    			TeacherDto teacherdto=teacherlist.get(i);
    			List<SmartCardInfoDto>smartcardinfolist=teacherdto.getSmartcardinfo();
    			
    			for(int j=0;j<smartcardinfolist.size();j++)
    			{
    				SmartCardInfoDto smartcardinfo=smartcardinfolist.get(j);
    				if(cardNum.equals(smartcardinfo.getCardid()))
    				{
    					return teacherdto;
    				}
    			}
    			
    		}
    		
    		return null;
    	}
    /*
     * User double tap the key BACK to exit the program
     */
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		cancelTimeOperation();

		if (keyCode == KeyEvent.KEYCODE_BACK
		   && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 800) {
				UIUtils.showToastId(this, R.string.taost_press_again,
						R.string.taost_press_again_en);
				exitTime = System.currentTimeMillis();
			} else {
				if (mainHandler != null) {
					mainHandler.removeMessages(Constants.MESSAGE_30);
					mainHandler.removeMessages(Constants.MESSAGE_60);
					mainHandler.removeMessages(Constants.MESSAGE_10);
					mainHandler.removeMessages(Constants.MESSAGE_TIME_10);
					mainHandler.removeMessages(Constants.MESSAGE_HEART_60);
					mainHandler.removeMessages(Constants.MESSAGE_DISEASE_30);
					mainHandler.removeMessages(Constants.MESSAGE_PUNCH_CARD);
				}
				
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/*
	 * Go into the Advertising screen if no user operation in 30 Seconds
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;

		case MotionEvent.ACTION_MOVE:
			break;

		case MotionEvent.ACTION_UP:
			// 触摸屏幕，就会重新及时：30秒无操作
			cancelTimeOperation();
			break;

		default:
			break;
		}
		
		return super.onTouchEvent(event);
	}
	
	// 初始化view控件
	private void initViews() {

		//Loading data
		loadingLinearLayout =(FrameLayout) findViewById(R.id.linearLayout_loading);
		loadingLinearLayout.setVisibility(View.GONE);
		loadingLinearLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		loadingTextView = (TextView) findViewById(R.id.textView_loading);

		// 主界面
		
		// Top
		settingImgView     = (ImageView)      findViewById(R.id.settingImgView);
		dateTextView       = (TextView)       findViewById(R.id.textView_date);
		timeTextView       = (TextView)       findViewById(R.id.textView_time);
		weekTextView       = (TextView)       findViewById(R.id.textView_week);
		weekEnTextView     = (TextView)       findViewById(R.id.textView_week_en);
		portraitImageView  = (ImageView)      findViewById(R.id.imageView_student_portrait);
		studentProgressBar = (ProgressBar)    findViewById(R.id.progressBar_student);
		studentProgressBar.setVisibility(View.GONE);
		nameTextView       = (TextView)       findViewById(R.id.textView_student_name);
		classTextView      = (TextView)       findViewById(R.id.textView_class);
		schoolTextView     = (TextView)       findViewById(R.id.textView_school);
		cardTextView       = (TextView)       findViewById(R.id.textView_card);

		// Middle
		horizontalListView = (HorizontalListView) findViewById(R.id.horizonListview);
		middleLinearoutLayout=(LinearLayout)findViewById(R.id.linearLayout_left_bottom_title);
		// Bottom
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(surfaceCallback);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// 广告界面
		advRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout_avd);
		avdImageView = (ImageView) findViewById(R.id.imageView_avd);
		
		// init Toast
		initOverlay();
	}
	
	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		toastTextView = (TextView) inflater.inflate(R.layout.overlay, null);
		toastTextView.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(toastTextView, lp);
	}

	// 初始化各种监听事件
	private void initListeners() {
		// 广告界面的touch事件
		advRelativeLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				cancelTimeOperation();
				return false;
			}
		});
		// 设置点击事件
		settingImgView.setOnClickListener(this);
	}

	// 加载学生信息
	protected void loadStudentInfo() {
		
		// 加载学生头像
		String filename = currentStudentDto.getStudentid()
				+ "_" + currentStudentDto.getPid() + ".jpg";
		String url = currentStudentDto.getAvatar();
	
		FileTools.loadImage(portraitImageView, url, 
				Constants.STUDENT_PORTRAIT, filename, studentProgressBar, true);
		// 学生姓名
		nameTextView.setText(currentStudentDto.getCnname());

		// 学生班级：因为可能存在两个或多个班级
		List<ClassInfoDto> classInfoDtos = currentStudentDto.getClassinfo();
		if (classInfoDtos != null && classInfoDtos.size() != 0) {
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < classInfoDtos.size(); i++) {
				sBuffer.append(classInfoDtos.get(i).getClassname()).append(",");
			}
			String classsString = sBuffer.toString();
			classTextView.setText(classsString.substring(0,
					classsString.length() - 1));
		}
		// 学生学校名称
		schoolTextView.setText(currentStudentDto.getSchoolname());
		cardTextView.setText("卡号：" + cardNum);
	}

	// 加载接送人信息
	protected void loadReceiverInfo() {
		List<RecieverDto> receiverList = currentStudentDto.getReceiver();
		if (receiverAdapter == null) {
			receiverAdapter = new ReceiverAdapter(MainActivity.this, receiverList);
			horizontalListView.setAdapter(receiverAdapter);
		} else {
			receiverAdapter.setReceiverList(receiverList);
			receiverAdapter.notifyDataSetChanged();
		}
		
	}

	// 得到当前刷卡学生的信息
	protected StudentDto getCurrentStudentInfo(String cardNum) {
		if (studentList == null || studentList.size() == 0) {
			return null;
		}
		for (int i = 0; i < studentList.size(); i++) {
			StudentDto studentDto = studentList.get(i);

			// 一个学生存在多个考勤卡？？
			List<SmartCardInfoDto> smartCardInfoDtos = studentDto.getSmartcardinfo();
			if (smartCardInfoDtos != null && smartCardInfoDtos.size() != 0) {
				for (int j = 0; j < smartCardInfoDtos.size(); j++) {
					// 当找到卡号对应的学生
					if (cardNum.equals(smartCardInfoDtos.get(j).getCardid())) {
						return studentDto;
					}
				}
			}
		}

		return null;
	}

	// 初始化数据
	private void initDatas() {
		mainHandler = new MainHandler();
		toastThread = new ToastThread();
		// 检测60秒无操作
		mainHandler.sendEmptyMessageDelayed(Constants.MESSAGE_60,
				Constants.NO_OPERATION_TIME);
		// 每10秒检测一次是否联网
		mainHandler.sendEmptyMessage(Constants.MESSAGE_10);
		// 每10更新一次时间
		mainHandler.sendEmptyMessage(Constants.MESSAGE_TIME_10);
		// sharepreference存储首次进入程序的日期firstDate
		if ("".equals(TimeCardApplicatoin.getInstance().getStringFromShares(Constants.FIRST_DATE, ""))) {
			TimeCardApplicatoin.getInstance().setStringToShares(Constants.FIRST_DATE, DateTools.getCurrentDate());
		}
		// 清除缓存数据
		DataCacheTools.clearCacheDatas();
		
		// 先判断本地是否有学生数据
		// 如果有，那么直接下载心跳包即可；如果没有，那么需要先下载学生数据，再下载心跳包
		//String oldStudentInfo = FileTools.readFileByLines(
			//	Constants.STUDENT_INFO_DIR + "/"
				//		+ Constants.STUDENT_INFO_FILE_NAME, "");
		
		
		
		String oldStudentInfo = FileTools.readFileByLines(
				Constants.STUDENT_INFO_DIR + "/"
						+ Constants.STUDENT_INFO_FILE_NAME, "");
		
		if(oldStudentInfo==null || oldStudentInfo.equals(""))
		{
				if(Utils.checkNetworkInfo(this)){
					// 加载所有学生的信息
					loadAllStudentInfo();
				} else {
					
					// 提示联网下载数据
					UIUtils.showToast(this, "数据为空，请先联网下载数据!", "Data is null, please download data first!");				
				}
				

		}
		else {
			if(Utils.checkNetworkInfo(this)){
				// 加载所有学生的信息
				getStudentCheck();
			}
			else {
				UIUtils.showToast(this, "数据为空，请先联网下载数据!", "Data is null, please download data first!");
			}
			
			allStudentInfoDto = JsonHelp.getObject(oldStudentInfo,
					AllStudentInfoDto.class);
			
			studentList = allStudentInfoDto.getStudent();
			studentMap = DataCacheTools.list2Map(studentList);
			
			teacherList =allStudentInfoDto.getTeacher();
			teacherMap=DataCacheTools.list2tMap(teacherList);
		}
		
		
	//	studentList = JsonHelp.getListObject(oldStudentInfo, StudentDto.class);
		//studentMap = DataCacheTools.list2Map(studentList);
		

		
		
//		if (studentList == null || studentList.size() == 0) {
//			// 有网
//			if(Utils.checkNetworkInfo(this)){
//				// 加载所有学生的信息
//				loadAllStudentInfo();
//			} else {			
//				// 提示联网下载数据
//				UIUtils.showToast(this, "数据为空，请先联网下载数据!", 
//						"Data is null, please download data first!");
//			}
//			
//		} else {
//			// 直接下载心跳包
//			getStudentCheck();
//		}
//		
		// 获取站点列表
		getBusStopList();
		
		// 下载广告图片
		downloadAvdPicture();
		
		// 开启service服务
		startService(new Intent(this, TimeCardService.class));
		
		sp = new SoundPool(5,AudioManager.STREAM_MUSIC, 0);
		soundId = sp.load(this, R.raw.picture, 1);
    }
	
	// 设置toast不可见
	private class ToastThread implements Runnable {

		@Override
		public void run() {
			toastTextView.setVisibility(View.GONE);
		}
	}
	
	// 设置toast可见
	private void showTextViewToast(String messageZn, String messageEn){
		toastTextView.setText(messageZn + "\n" + messageEn);
		toastTextView.setVisibility(View.VISIBLE);
		mainHandler.removeCallbacks(toastThread);
		// 延迟一秒半后执行，让overlay为不可见
		mainHandler.postDelayed(toastThread, 1500);
	}
	
	// 加载所有学生的信息
	private void loadAllStudentInfo() {

		WebService webService = WebServiceImpl.getInstance();
		webService.getAllStudentInfo(Utils.getMachineNum(this),
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
							UIUtils.showToastSererError(arg3,
									getApplicationContext());
						}
						cancelTimeOperation();
						loadingLinearLayout.setVisibility(View.GONE);
						// 如果加载学生数据失败，那么就用旧的数据
						String oldStudentInfo = FileTools.readFileByLines(
								Constants.STUDENT_INFO_DIR + "/"
										+ Constants.STUDENT_INFO_FILE_NAME, "");

						if(oldStudentInfo.equals(""))
							return;
						allStudentInfoDto = JsonHelp.getObject(oldStudentInfo,
								AllStudentInfoDto.class);
						
						studentList = allStudentInfoDto.getStudent();
						studentMap = DataCacheTools.list2Map(studentList);
						
						teacherList =allStudentInfoDto.getTeacher();
						teacherMap=DataCacheTools.list2tMap(teacherList);


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
							    reminderList = allStudentInfoDto.getHealthstate();
								
								//Update the reminders
							   // if(reminderList!=null && reminderList.size()!=0)
							        //updateReminders(reminderList);
								
							    // 将学生信息保存到本地								
								teacherList =allStudentInfoDto.getTeacher();
								teacherMap=DataCacheTools.list2tMap(teacherList);

								// 将学生信息保存到本地
								FileTools.save2SDCard(
										Constants.STUDENT_INFO_DIR,
										Constants.STUDENT_INFO_FILE_NAME,
										studentInfo);
								
								
								
							}							
						}
						
						// 加载心跳包信息
						getStudentCheck();
						
						UIUtils.showToastGetStudentsInfoCode(code,
								getApplicationContext());
					}
				});
	}

	// 下载心跳包信息
	private void getStudentCheck() {
		WebService webService = WebServiceImpl.getInstance();
		webService.getHeartPackageInfo(Utils.getMachineNum(this),
				new AsyncHttpResponseHandler() {

					@Override
					public void onFinish() {
						super.onFinish();
					}

					@Override
					public void onStart() {
						super.onStart();
						cancelTimeOperation();
						loadingTextView.setText("心跳包更新...\nUpdating...");
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// 隔60秒更新一次心跳包
						mainHandler.sendEmptyMessageDelayed(
								Constants.MESSAGE_HEART_60,
								Constants.CHECK_HEART_PACKAGE_TIME);
						if (arg3 != null) {
							UIUtils.showToastSererError(arg3,
									getApplicationContext());
						}
						loadingLinearLayout.setVisibility(View.GONE);
						cancelTimeOperation();
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// 隔60秒更新一次心跳包
						mainHandler.sendEmptyMessageDelayed(
								Constants.MESSAGE_HEART_60,
								Constants.CHECK_HEART_PACKAGE_TIME);
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
							String studentCheck = new String(arg2);
							heartPackageDto = JsonHelp.getObject(studentCheck,
									HeartPackageDto.class);
						
							if( heartPackageDto.getAddstudent().size() != 0
							 || heartPackageDto.getCard().size() != 0
							 || heartPackageDto.getModifystudent().size() != 0
							 || heartPackageDto.getReceiver().size() != 0
							 || heartPackageDto.getOutstudent().size() != 0 )
							{
								// 根据心跳包更新studentList数据
								studentList = DateConvertTool
										.updateStudentsInfoWithHeartPackage(
												studentMap, heartPackageDto);
								teacherList=DateConvertTool.updateTeachersInfoWithHeartPackage(teacherMap, heartPackageDto);
								
								
								allStudentInfoDto.setStudent(studentList);
								allStudentInfoDto.setTeacher(teacherList);
								
							//	String aa= JsonHelp.jsonObjectToString(allStudentInfoDto);
								// 将学生信息保存到本地
								FileTools.save2SDCard(
										Constants.STUDENT_INFO_DIR,
										Constants.STUDENT_INFO_FILE_NAME,
										JsonHelp.jsonObjectToString(allStudentInfoDto));						
							}
							
						}
						//Is relocating, remove the databases
						else if("2".equals(code)){
							FileTools.deleteDirectory(Constants.BASE_CACHE_DIR);
						}
						
						UIUtils.showToastGetHeartPackageCode(code,
								getApplicationContext());
					}
				});
	}

	// SurfaceHodler Callback handle to open the camera, off camera and photo
	// size changes
	// Camera.CameraInfo.CAMERA_FACING_FRONT(前置) 和
	// Camera.CameraInfo.CAMERA_FACING_BACK(后置)
	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

		@SuppressLint("NewApi")
		public void surfaceCreated(SurfaceHolder holder) {
			openCamera();
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			closeCamera();
		}
	};



	// 拍照
	private void takePic() {
        //camera.stopPreview();// stop the preview
		//There is a small chance the camera is closed by user putting the phone to sleep
			playSound();
			camera.setOneShotPreviewCallback(new PreviewCallback() {
				
				@Override
				public void onPreviewFrame(byte[] data, Camera camera) {
					palmTask = new PalmTask(data);
					palmTask.execute();
				}
			});
	}
	
	
	/*自定义的PalmTask类，开启一个线程分析数据*/
	private class PalmTask extends AsyncTask<Void, Void, Bitmap> {
		private byte[] mData;

		// 构造函数
		public PalmTask(byte[] data) {
			this.mData = data;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			Size size = camera.getParameters().getPreviewSize(); // 获取预览大小
			final int w = size.width; // 宽度
			final int h = size.height;
			final YuvImage image = new YuvImage(mData, ImageFormat.NV21, w, h,
					null);
			ByteArrayOutputStream os = new ByteArrayOutputStream(mData.length);
			if (!image.compressToJpeg(new Rect(0, 0, w, h), 100, os)) {
				return null;
			}
			byte[] tmp = os.toByteArray();
			Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
			
			return bmp;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result == null) {
				UIUtils.showToast(MainActivity.this, "拍照失败！", "Pictures failed！");
			} else {
				cardSwipeProcess(result);
			}
		}
	}
	
	 /** 
     *  播放照相的声音
     */  
    private void playSound() {  
        sp.play(soundId, 0.5F, 0.5F, 1, 0, 1);
    } 
	
	/**
	 * 操作摄像头获取的帧视频（即图片）
	 * @param bmp
	 */
	protected void cardSwipeProcess(Bitmap bmp) {
		
		// 获取本次打卡信息
		TimeCardBean currTimeCardBean = new TimeCardBean();
		
		currTimeCardBean.setMachine(Utils.getMachineNum(this));
		currTimeCardBean.setCreatetime(System.currentTimeMillis() / 1000);
		currTimeCardBean.setSmartid(cardNum);
		Bitmap newBitmap = FileTools.resizeImage(bmp, 300, 300);
		base64 = FileTools.bitmapToBase64(newBitmap);
		currTimeCardBean.setFbody(base64);
				
		// 如果上次已打卡，那么将本次打卡信息赋值给上次打卡信息
		Message msg = mainHandler.obtainMessage();
		msg.what = Constants.MESSAGE_PUNCH_CARD;
		msg.obj  = currTimeCardBean;
		mainHandler.sendMessage(msg);
	}

	// 检测是否有最新的APK版本
	private void checkVersion() {
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					UmengUpdateAgent.showUpdateDialog(MainActivity.this,
							updateInfo);
					Constants.IS_NEED_UPDATE = true;
					break;
				case UpdateStatus.No: // has no update
					UIUtils.showToast(getApplicationContext(),
							"太棒了，你使用是已是最新版本！",
							"Great, you are using the latest version!");
					Constants.IS_NEED_UPDATE = false;
					break;
				case UpdateStatus.NoneWifi: // none wifi
					UIUtils.showToast(getApplicationContext(),
							"没有wifi连接，只在wifi下更新！",
							"No wifi connection, only update on wifi!");
					break;
				case UpdateStatus.Timeout: // time out
					UIUtils.showToast(getApplicationContext(), "亲，你的网络不给力哦！",
							"Pro, the network signal is bad!");
					break;
				}
			}
		});
		UmengUpdateAgent.update(this);
	}

	// mainHandler
	class MainHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			
			case Constants.MESSAGE_LOCATION_UPDATE:
				break;
			
			// 收到消息就跳转到广告界面
			case Constants.MESSAGE_60:
				
				mainToAdvSwap();
				
				// 清空UI界面
				clearUI();

				// 同时将SettingDialog退出
				if (settingDialog != null && settingDialog.isShowing()) {
					settingDialog.dismiss();
				}

				break;
			// 每隔10秒检测一次是否联网
			case Constants.MESSAGE_10:
				// 如果没联网则提示
				if (!Utils.checkNetworkInfo(MainActivity.this)) {
					UIUtils.showToastId(MainActivity.this,
							R.string.taost_check_network,
							R.string.taost_check_network_en);
				}
				// 轮询检测
				sendEmptyMessageDelayed(Constants.MESSAGE_10,
						Constants.CHECK_NETWORK_TIME);
				break;
			// 10秒更新一次时间
			case Constants.MESSAGE_TIME_10:
				// 年月日，时间，星期（中英文）
				String date = DateTools.getDateForm("yyyy年MM月dd日",
						System.currentTimeMillis());
				String time = DateTools.getCurrentTime();
				String week = DateTools.getWeekFromDate(DateTools
						.getCurrentDate(),
						getResources().getStringArray(R.array.week_zn));
				String weekEn = DateTools.getWeekFromDate(DateTools
						.getCurrentDate(),
						getResources().getStringArray(R.array.week_en));
				dateTextView.setText(date);
				timeTextView.setText(time);
				weekTextView.setText(week);
				weekEnTextView.setText(weekEn);
				sendEmptyMessageDelayed(Constants.MESSAGE_TIME_10,
						Constants.UPDATE_TIME);
				break;
			// 每30分钟更新一个心跳包
			case Constants.MESSAGE_HEART_60:

				 getStudentCheck();

				 break;
			// 打卡之后后，等30秒再和健康状态信息一起上传
			// 开始上传打卡信息
			case Constants.MESSAGE_PUNCH_CARD:
				/**
				 * 有无网络都将数据存储到本地，然后再service后台每隔1秒上传一条数据
				 */
				TimeCardBean tmcardBean = (TimeCardBean) msg.obj;
				String cardData = JsonHelp.jsonObjectToString(tmcardBean);
				
				// 文件名称：yyMMddHHmmss+cardid+.dto
				FileTools.save2SDCard(Constants.CARD_INFO_DIR_NO, 
						              DateTools.getDateForm("yyMMddHHmmss", System.currentTimeMillis()) + tmcardBean.getSmartid() + ".dto", 
						              cardData);
				
				showTextViewToast("打卡成功！", "Checked In!");
				
				// 重新计时30秒无操作
				cancelTimeOperation();
				
				currentTimeCardBean = null;
				
				break;
			}
		}
	}

	/**
	 * 清空UI界面
	 */
	private void clearUI(){
		// 将界面信息清空
		nameTextView.setText("");
		schoolTextView.setText("");
		classTextView.setText("");
		cardTextView.setText("");
		portraitImageView.setImageResource(R.drawable.portrait_big);
		if(receiverAdapter != null){
			
			receiverAdapter.setReceiverList(new ArrayList<RecieverDto>());
			receiverAdapter.notifyDataSetChanged();
		}
	}
	
	// 下载广告图片
	private void downloadAvdPicture() {
		WebService webService = WebServiceImpl.getInstance();
		webService.getAvdPicture(new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onStart() {
				super.onStart();
				mainHandler.removeMessages(Constants.MESSAGE_30);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				if (arg3 != null) {
					UIUtils.showToastSererError(arg3, getApplicationContext());
				}
				// 加载缓存的图片
				avdImageView.setImageBitmap(Utils.toRoundCorner(
						Utils.getOptionBitmap(Constants.AVD_PIC_DIR + "/"
								+ Constants.AVD_PIC_NAME), 30));

				cancelTimeOperation();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				cancelTimeOperation();
				Map<String, String> maps = new HashMap<String, String>();

				if (arg1 != null && arg1.length != 0) {

					for (int i = 0; i < arg1.length; i++) {
						maps.put(arg1[i].getName(), arg1[i].getValue());
					}
				}

				avdDto = JsonHelp.getObject(new String(arg2), AvdDto.class);
				if (avdDto != null) {
					FileTools.loadAvdImage(avdImageView, avdDto.getFilepath(),
							Constants.AVD_PIC_DIR, Constants.AVD_PIC_NAME);

				}
			}

		});

	}



	/**
	 * 各种点击事件
	 */
//	int number = 0;
	@Override
	public void onClick(View arg0) {

		cancelTimeOperation();

		switch (arg0.getId()) {
		// 点击中间图片，弹出设置界面Dialog
		case R.id.settingImgView:
			settingDialog = new SettingDialog(this, mainHandler);
			settingDialog.show();
			
			// 测试数据
//			number ++;
//			if (number % 3 == 0) {
//				cardNum = "1966461";
//			} 
//			if (number % 3 == 1) {
//				cardNum = "1969831";
//			}
//			if (number % 3 == 2) {
//				cardNum = "1969832";
//			}
//			
//			currentStudentDto = getCurrentStudentInfo(cardNum);
//			if (currentStudentDto != null) {
//
//				// 加载学生信息
//				loadStudentInfo();
//				// 加载接送人信息
//				loadReceiverInfo();
//				// 开始拍照
//				takePic();
//			} else {
//				// 清空UI界面
//				clearUI();
//				UIUtils.showToast(getApplicationContext(), "此卡不存在！", "Card is not exist!");
//			}			
			break;
		default:
			break;
		}

	}
	
	// 重新计时
	private void cancelTimeOperation() {
		advToMainSwap();
		mainHandler.removeMessages(Constants.MESSAGE_30);
		mainHandler.sendEmptyMessageDelayed(Constants.MESSAGE_30,
				Constants.NO_OPERATION_TIME);
		mainHandler.removeMessages(Constants.MESSAGE_60);
		mainHandler.sendEmptyMessageDelayed(Constants.MESSAGE_60,
				Constants.NO_OPERATION_TIME);
	}

    private String getNFCCardID(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        sb.append(getDec(id));
        return sb.toString();
    }


    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private void openCamera()
    {
    	if(isCameraRunning)
    		return;
    	else
    	{
			camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK); // Turn on the camera
			
			try {
				camera.setPreviewDisplay(surfaceHolder); // Set Preview
				Camera.Parameters parameters = camera.getParameters(); // Camera
				// parameters to obtain
				parameters.setPictureFormat(PixelFormat.JPEG);// Setting Picture
				// parameters.set("rotation", 180); // Arbitrary rotation
				camera.setDisplayOrientation(90);
				// parameters.setPreviewSize(400, 300); // Set Photo Size
				camera.setParameters(parameters); // Setting camera parameters
			} catch (IOException e) {
				camera.stopPreview();
				camera.release();// release camera
				camera = null;
			}
		
		    isCameraRunning = true;
    	}
    }
    
    private void startPreview()
    {
    	if(isCameraRunning)
    	    camera.startPreview();
    }
    
    private void closeCamera()
    {
        if (null != camera) {
        	camera.setOneShotPreviewCallback(null);
        	camera.stopPreview();
        	camera.release();
        	camera = null;
        }
        
        isCameraRunning = false;
    }
    
    private void advToMainSwap()
    {
		advRelativeLayout.setVisibility(View.GONE);
    	
		openCamera();
		
		startPreview();
    }
    
    private void mainToAdvSwap()
    {
    	advRelativeLayout.setVisibility(View.VISIBLE);
		
		closeCamera();
    }
    
    private void getBusStopList()
    {
		WebService webService = WebServiceImpl.getInstance();
		webService.getBusStopList(Utils.getMachineNum(this),new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				if (arg3 != null) {
					UIUtils.showToastSererError(arg3, getApplicationContext());
				}
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				cancelTimeOperation();
//				Map<String, String> maps = new HashMap<String, String>();
//
//				if (arg1 != null && arg1.length != 0) {
//
//					for (int i = 0; i < arg1.length; i++) {
//						maps.put(arg1[i].getName(), arg1[i].getValue());
//					}
//				}

				String result = new String(arg2);
			    BusStopListDto busStopList = JsonHelp.getObject(result, BusStopListDto.class);
				if(busStopList!=null)
				{
				    List<BusStopDto> list = busStopList.getGeofence();
				    int i;
				    for(i=0; i<list.size(); i++ )
				    {
				    	BusStopDto busStop = list.get(i);
				    	
				        mNotifyLister = new NotifyLister(busStop);
					    //mNotifyLister.SetNotifyLocation(39.962026, 116.439021, 3000, "bd09ll");//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
					    mNotifyLister.SetNotifyLocation(busStop.getLatitude(), busStop.getLongitude(), 500, "bd09ll");//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
				        mLocationClient.registerNotify(mNotifyLister);
				    }
				    
				    if( i > 0)
				    {
				    	mLocationClient.start();
				    }
				}
			
			}
		});
    }
    
	private void startLBS(){
	    mLocationClient = ((TimeCardApplicatoin)getApplication()).mLocationClient;
	  
        LocationClientOption option = new LocationClientOption();
	    option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
	    option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
	    int span=20*1000;
	    option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
	    option.setIsNeedAddress(false);
        mLocationClient.setLocOption(option);
      
        //This is a Must-have, otherwise there will be Null pointer error
        mLocationClient.registerLocationListener(new NotiftLocationListener());
	  
	}
	
	private void stopLBS()
	{
		mLocationClient.removeNotifyEvent(mNotifyLister);
		mLocationClient.stop();
	}
	
	public void notifyServerBusStop(BusStopDto busStop)
	{	
		WebService webService = WebServiceImpl.getInstance();
		webService.postBusStopArrival(busStop.getGeofenceid(), 
				                      Utils.getMachineNum(this), 
				                      System.currentTimeMillis()/1000, 
				                      new AsyncHttpResponseHandler() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				if (arg3 != null) {
					UIUtils.showToastSererError(arg3, getApplicationContext());
				}
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			}
		});
	}

	public class NotiftLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			//double longitude = location.getLongitude();
			//double latitude = location.getLatitude();
		}
	}
	
	public class NotifyLister extends BDNotifyListener{
		
		BusStopDto mBusStop;
		
		public NotifyLister(BusStopDto busStop)
		{
			mBusStop = busStop;
		}
		
	    public void onNotify(BDLocation mlocation, float distance){
	    	//Toast.makeText(NotifyActivity.this, "震动提醒", Toast.LENGTH_SHORT).show();
	    	//Log.i("","Location Deteced!");
	    	notifyServerBusStop(mBusStop);
	    }
	}
}