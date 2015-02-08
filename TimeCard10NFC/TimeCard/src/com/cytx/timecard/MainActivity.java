package com.cytx.timecard;

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
import android.hardware.Camera.CameraInfo;
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
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cytx.timecard.bean.AttendanceStateBean;
import com.cytx.timecard.bean.TimeCardBean;
import com.cytx.timecard.constants.Constants;
import com.cytx.timecard.dto.AllStudentInfoDto;
import com.cytx.timecard.dto.AvdDto;
import com.cytx.timecard.dto.ClassInfoDto;
import com.cytx.timecard.dto.HeartPackageDto;
import com.cytx.timecard.dto.LessionDto;
import com.cytx.timecard.dto.RecieverDto;
import com.cytx.timecard.dto.SmartCardInfoDto;
import com.cytx.timecard.dto.StudentDto;
import com.cytx.timecard.dto.TeacherDto;
import com.cytx.timecard.dto.TrainingDto;
import com.cytx.timecard.jdbc.DataBaseUtils;
import com.cytx.timecard.service.TimeCardService;
import com.cytx.timecard.service.WebService;
import com.cytx.timecard.service.impl.WebServiceImpl;
import com.cytx.timecard.utility.DataCacheTools;
import com.cytx.timecard.utility.DateTools;
import com.cytx.timecard.utility.DebugClass;
import com.cytx.timecard.utility.FileTools;
import com.cytx.timecard.utility.JsonHelp;
import com.cytx.timecard.utility.UIUtils;
import com.cytx.timecard.utility.Utils;
import com.cytx.timecard.widget.HealthRemindersAdapter;
import com.cytx.timecard.widget.HorizontalListView;
import com.cytx.timecard.widget.ReceiverAdapter;
import com.cytx.timecard.widget.RemindersAdapter;
import com.cytx.timecard.widget.SettingDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 有时间，可以对此界面进行重构：将界面分四个分布自定义RelativeLayout写
 * 好处是：代码不会看起来这么臃肿
 *
 * @author Ben
 *         <p/>
 *         2014年8月14日 下午11:45:21
 */

public class MainActivity extends Activity implements OnClickListener {

    private RelativeLayout advRelativeLayout;// 广告界面
    private LinearLayout mainRelativeLayout;// 主界面

    private ImageView swipeImageView;

    private TextView dateTextView;// 日期
    private TextView timeTextView;// 时间
    private TextView weekTextView;// 星期
    private ImageView portraitImageView;// 学生头像
    private ProgressBar studentProgressBar;// 加载学生头像的progressbard
    private TextView nameTextView;// 学生姓名
    private TextView classTextView;// 班级
    private TextView schoolTextView;// 学校
    private TextView cardTextView;// 卡号

    private LinearLayout reminders_healthcheck_Layout;

    private HorizontalListView horizontalListView_family;
    private HorizontalListView horizontalListView_healthcheck;
    private HorizontalListView horizontalListView_reminders;
    private ReceiverAdapter receiverAdapter;
    private HealthRemindersAdapter healthCheckAdapter;
    private RemindersAdapter remindersAdapter;

    private ImageView confirmImageView;
    private boolean hasReminders;
    private Button infoButton;

    // right-bottom
    private SurfaceView surfaceView;// 拍照
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private boolean isCameraRunning = false;
    private EditText cardNumEditText;// 考勤卡卡号

    // avd
    private ImageView avdImageView;// 广告

    private MainHandler mainHandler;// 主Handler
    private SettingDialog settingDialog;// 设置界面

    //  日常接送人
    private TextView receiverView;
    private TextView receiverNoteView;

    public List<LessionDto> currentStudentLessons;

    public AllStudentInfoDto allStudentInfoDto;
    public List<StudentDto> studentList; // 所有学生的信息
    public List<LessionDto> allStudentLessons; //所有的提醒
    public int healthState = 1;
    public List<TeacherDto> teacherList; //所有教师信息
    private HeartPackageDto heartPackageDto;// 心跳包信息
    private AvdDto avdDto;// 广告信息
    private String cardNum;// 卡号
    private String base64;// 图片信息
    private LinearLayout loadingLinearLayout;// 数据加载中
    private TextView loadingTextView;// 数据加载中

    private int timing = 0;// 计时，30秒等待上传打卡信息
    public Map<String, StudentDto> studentMap;// 将list 转为map；
    public Map<String, TeacherDto> teacherMap;

    private SoundPool sp;
    private int soundId;

    // 自定义TextView替换toast的功能：目的是缩短提示信息显示的时间
    private TextView toastTextView;
    private ToastThread toastThread;

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置无标题、全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        // 屏幕常亮
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        //初始化view控件
        initViews();
        mainHandler = new MainHandler();
//        allStudentLessons = loadHealthData();
//        loadHealthRemiderUI();

        if(!Constants.INTENT_START_ACTIVITY_ONLY.equals(getIntent().getAction()))
        {
            DebugClass.displayCurrentStack("Activity launched from home, start service ...");
            // 开启service服务, will be started in Receiver
            Intent intent = new Intent(this, TimeCardService.class);
            intent.setAction(Constants.INTENT_START_SERVICE_ONLY);
            startService(intent);
        }
        else
        {
            DebugClass.displayCurrentStack("Activity launched from service, NOT start service ...");
        }

        // 初始化各种listener监听事件
        initListeners();
        // 初始化数据
        initDatas();
        // 检测是否有新版本
        checkVersion();
    }

    // 初始化view控件
    private void initViews() {

        //Loading Progress indicator
        loadingLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_loading);
        loadingLinearLayout.setVisibility(View.GONE);
        loadingTextView = (TextView) findViewById(R.id.textView_loading);

        // 广告界面
        advRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout_avd);
        advRelativeLayout.setVisibility(View.GONE);

        // 主界面
        mainRelativeLayout = (LinearLayout) findViewById(R.id.relativeLayout_main);
        mainRelativeLayout.setVisibility(View.VISIBLE);

        //Date, time and Week
        dateTextView = (TextView) findViewById(R.id.textView_date);
        timeTextView = (TextView) findViewById(R.id.textView_time);
        weekTextView = (TextView) findViewById(R.id.textView_week);

        //Student Information
        portraitImageView = (ImageView) findViewById(R.id.imageView_student_portrait);
        studentProgressBar = (ProgressBar) findViewById(R.id.progressBar_student);
        studentProgressBar.setVisibility(View.GONE);
        nameTextView = (TextView) findViewById(R.id.textView_student_name);
        classTextView = (TextView) findViewById(R.id.textView_class);
        schoolTextView = (TextView) findViewById(R.id.textView_school);
        cardTextView = (TextView) findViewById(R.id.textView_card);

        //SwipeCardImage
        swipeImageView = (ImageView) findViewById(R.id.imageView_card_swipe);

        //Family members who pick up the kids after school
        receiverView = (TextView) findViewById(R.id.textView1);
        receiverNoteView = (TextView) findViewById(R.id.textView2);
        horizontalListView_family = (HorizontalListView) findViewById(R.id.horizonListview_family_members);

        //Preview
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceCallback);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //Confirm on reminders and health checks
        confirmImageView = (ImageView) findViewById(R.id.imageView_confirm);
        confirmImageView.setImageResource(R.drawable.confirm_white);

        reminders_healthcheck_Layout = (LinearLayout) findViewById(R.id.linearLayout_health_check);

        //Health check
        horizontalListView_healthcheck = (HorizontalListView) findViewById(R.id.horizonListview_health_checks);

       // horizontalListView_healthcheck.setAdapter();
        //Normal Reminders
        horizontalListView_reminders = (HorizontalListView) findViewById(R.id.horizonListview_reminders);

        //ID Card reader as OTG device support
        cardNumEditText = (EditText) findViewById(R.id.editText_card_num);

        // avd
        avdImageView = (ImageView) findViewById(R.id.imageView_avd);

        // Information Button
        infoButton = (Button) findViewById(R.id.information);

        // init Toast
        initOverlay();
    }

    // 初始化提示框
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
        loadingLinearLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        // 广告界面的touch事件
        advRelativeLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                advRelativeLayout.setVisibility(View.GONE);
                mainRelativeLayout.setVisibility(View.VISIBLE);
                openCamera();
                startPreview();
                return false;
            }
        });


        //Init NFC
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            swipeImageView.setVisibility(View.GONE);
        } else {

            mPendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            swipeImageView.setVisibility(View.VISIBLE);
        }

        //Confirmation
        confirmImageView.setOnClickListener(this);

        //Information
        infoButton.setOnClickListener(this);

        // 监听edittext的值变化
        cardNumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 如果还在下载数据，是不允许打卡的
                if (loadingLinearLayout.getVisibility() == View.VISIBLE) {
                    UIUtils.showToast(getApplicationContext(), "正在下载数据，请稍等会儿", "Loading data, please wait for a moment");
                    return;
                }

                if (arg0.toString().length() == 10) {
                    // 检测到打卡，重新计时，并隐藏广告页面显示主界面
                    cancelTimeOperation();
                    // 去掉字符串前面的“0”

                    cardTextView.setText("卡号：" + arg0.toString());
                    cardNum = cardNumEditText.getText().toString().replaceFirst("^0*", "");
                    if (cardNum.length() > 7) {
                        cardNum = cardNum.substring(0, 7);
                    }
                    cardNumEditText.setText("");
                    cardTextView.setText("卡号：" + cardNum);

                    // 得到当前刷卡学生的信息
                    Object currentObject = getCurrentInfo(cardNum);
                    if(currentObject==null)
                    {
                        clearUI();
                        showTextViewToast("此卡不存在！", "Card is not exist!");
                        return;
                    }
                    if (currentObject instanceof StudentDto) {
                        StudentDto studentDto = (StudentDto) currentObject;
                        loadStudentInfoUI(studentDto);
                        loadReceiverInfoUI(studentDto);
                        takePic();
                    } else if (currentObject instanceof TeacherDto) {
                        TeacherDto teacherDto = (TeacherDto) currentObject;
                        loadTeacherInfoUI(teacherDto);
                        takePic();
                    } else {
                        clearUI();
                        showTextViewToast("此卡不存在！", "Card is not exist!");
                    }
                }
            }
        });
        // 防止触摸EditText弹出软键盘
        cardNumEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
    }

    // 初始化数据
    private void initDatas() {

        toastThread = new ToastThread();

        // 检测30秒无操作
        mainHandler.sendEmptyMessageDelayed(Constants.MESSAGE_ID_IDLE_DEVICE, Constants.TIME_NO_OPERATION);

        // 每10秒检测一次是否联网
        mainHandler.sendEmptyMessage(Constants.MESSAGE_ID_CHECK_NETWORK);

        // 每10更新一次时间
        mainHandler.sendEmptyMessage(Constants.MESSAGE_ID_UPDATE_TIME);

        // sharepreference存储首次进入程序的日期firstDate
        if ("".equals(TimeCardApplicatoin.getInstance().getStringFromShares(Constants.FIRST_DATE, ""))) {
            TimeCardApplicatoin.getInstance().setStringToShares(Constants.FIRST_DATE, DateTools.getCurrentDate());
        }
        // 清除缓存数据
        DataCacheTools.clearCacheDatas();

        // 先判断本地是否有学生数据
        // 如果有，那么直接下载心跳包即可；如果没有，那么需要先下载学生数据，再下载心跳包
        String oldStudentInfo = FileTools.readFileByLines(
                Constants.STUDENT_INFO_DIR + "/"
                        + Constants.STUDENT_INFO_FILE_NAME, "");

        if (oldStudentInfo == null || oldStudentInfo.equals("")) {
            if (Utils.checkNetworkInfo(this)) {
                // 加载所有学生的信息
                loadAllStudentInfo();
            } else {
                // 提示联网下载数据
                UIUtils.showToast(this, "数据为空，请先联网下载数据!", "Data is null, please download data first!");
            }
        } else {
            if (Utils.checkNetworkInfo(this)) {
                // 加载所有学生的信息
                getStudentCheck();
            } else {
                UIUtils.showToast(this, "数据为空，请先联网下载数据!", "Data is null, please download data first!");
            }

            //TODO saigon allStudentInfoDto has been initalized before,need to check if this is still needed
            allStudentInfoDto = JsonHelp.getObject(oldStudentInfo, AllStudentInfoDto.class);
            studentList = allStudentInfoDto.getStudent();
            studentMap = DataCacheTools.list2Map(studentList);
            teacherList = allStudentInfoDto.getTeacher();
            teacherMap = DataCacheTools.list2tMap(teacherList);
        }

        //
        loadRemindersUI();

        // 下载广告图片
        downloadAvdPicture();

        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        soundId = sp.load(this, R.raw.picture, 1);
    }

    protected void loadTeacherInfoUI(TeacherDto teacherDto) {
        FileTools.loadImage(portraitImageView,
                teacherDto.getAvatar(),
                Constants.STUDENT_PORTRAIT,
                teacherDto.getTeacherid() + "_" + ".jpg",
                studentProgressBar,
                true);
        // 学生姓名
        reminders_healthcheck_Layout.setVisibility(View.GONE);
//		bottom_left_outLayout.setVisibility(View.GONE);
        nameTextView.setText(teacherDto.getTeachername());

        // 学生班级：因为可能存在两个或多个班级
        String classInfoDtos = teacherDto.getClassname();
        classTextView.setText(classInfoDtos.substring(0,
                classInfoDtos.length() - 1));
        schoolTextView.setText("");
        cardTextView.setText("卡号：\n" + cardNum);

        receiverNoteView.setText("");
        if (receiverAdapter != null) {
            receiverAdapter.setReceiverList(new ArrayList<RecieverDto>());
            receiverAdapter.notifyDataSetChanged();
        }
    }

    protected void loadStudentInfoUI(StudentDto studentDto) {
        reminders_healthcheck_Layout.setVisibility(View.VISIBLE);
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
            StringBuilder sBuffer = new StringBuilder();
            for (int i = 0; i < classInfoDtos.size(); i++) {
                sBuffer.append(classInfoDtos.get(i).getClassname()).append(",");
            }
            String classsString = sBuffer.toString();
            classTextView.setText(classsString.substring(0,
                    classsString.length() - 1));
        }
        // 学生学校名称
        schoolTextView.setText(studentDto.getSchoolname());
        cardTextView.setText("卡号：\n" + cardNum);
    }

    protected void loadReceiverInfoUI(StudentDto studentDto) {
        receiverNoteView.setText(R.string.left_bottom_title_2);
        receiverView.setText(R.string.left_bottom_title_1);

        List<RecieverDto> receiverList = studentDto.getReceiver();
        if (receiverAdapter == null) {
            receiverAdapter = new ReceiverAdapter(MainActivity.this, receiverList);
            horizontalListView_family.setAdapter(receiverAdapter);
        } else {
            receiverAdapter.setReceiverList(receiverList);
            receiverAdapter.notifyDataSetChanged();
        }

    }

    protected void loadRemindersUI() {
        if (remindersAdapter == null) {
            remindersAdapter = new RemindersAdapter(getApplicationContext(), mainHandler, allStudentLessons);
            horizontalListView_reminders.setAdapter(remindersAdapter);
        } else {
            remindersAdapter.setReminderDtoList(allStudentLessons);
            remindersAdapter.notifyDataSetChanged();
        }
    }

    //TODO saigon need to get it from network
    protected List<LessionDto> loadCurriculum(AllStudentInfoDto allInfo, String studentID)
    {
        for(TrainingDto training : allInfo.getTraining())
        {
            if(studentID.equals(training.getStudentid()))
            {
                return training.getLesson();
            }
        }
        return null;
    }

    protected List<LessionDto> loadHealthData(AllStudentInfoDto allInfo)
    {
        Map<String, LessionDto> lessons=new HashMap<String, LessionDto>();

        for(TrainingDto training : allInfo.getTraining())
        {
            for(LessionDto lesson : training.getLesson())
            {
                if(lessons.containsKey(lesson.getLessonid()))
                {
                    int num = lessons.get(lesson.getLessonid()).getNum();
                    lessons.get(lesson.getLessonid()).setNum(num+1);
                }
                else
                {
                    lesson.setNum(1);
                    lessons.put(lesson.getLessonid(), lesson);
                }
            }
        }
        return new ArrayList<LessionDto>(lessons.values());
    }
    protected void loadHealthRemiderUI()
    {
        if(healthCheckAdapter==null)
        {
            healthCheckAdapter=new HealthRemindersAdapter(getApplicationContext(),  mainHandler, currentStudentLessons);
            horizontalListView_healthcheck.setAdapter(healthCheckAdapter);
        }
        else
        {
            healthCheckAdapter.setReminderDtoList(currentStudentLessons);
            healthCheckAdapter.notifyDataSetChanged();
        }
    }

    protected Object getCurrentInfo(String cardNum) {
        if (allStudentInfoDto == null) {
            return null;
        }

        List<StudentDto> studentlist = allStudentInfoDto.getStudent();
        List<TeacherDto> teacherlist = allStudentInfoDto.getTeacher();

        if(studentlist!=null)
        {
            for (int i = 0; i < studentlist.size(); i++) {
                StudentDto studentdto = studentlist.get(i);
                List<SmartCardInfoDto> smartcardinfolist = studentdto.getSmartcardinfo();

                for (int j = 0; j < smartcardinfolist.size(); j++) {
                    SmartCardInfoDto smartcardinfo = smartcardinfolist.get(j);
                    if (cardNum.equals(smartcardinfo.getCardid())) {
                        return studentdto;
                    }
                }
            }
        }



        if(teacherlist!=null)
        {
            for (int i = 0; i < teacherlist.size(); i++) {
                TeacherDto teacherdto = teacherlist.get(i);
                List<SmartCardInfoDto> smartcardinfolist = teacherdto.getSmartcardinfo();

                for (int j = 0; j < smartcardinfolist.size(); j++) {
                    SmartCardInfoDto smartcardinfo = smartcardinfolist.get(j);
                    if (cardNum.equals(smartcardinfo.getCardid())) {
                        return teacherdto;
                    }
                }
            }
        }


        return null;
    }

    // 设置toast不可见
    private class ToastThread implements Runnable {

        @Override
        public void run() {
            toastTextView.setVisibility(View.GONE);
        }
    }

    // 设置toast可见
    private void showTextViewToast(String messageZn, String messageEn) {
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

                        if (oldStudentInfo.equals(""))
                            return;
                        allStudentInfoDto = JsonHelp.getObject(oldStudentInfo,
                                AllStudentInfoDto.class);

                        studentList = allStudentInfoDto.getStudent();
                        studentMap = DataCacheTools.list2Map(studentList);
                        allStudentLessons = loadHealthData(allStudentInfoDto);
                        teacherList = allStudentInfoDto.getTeacher();
                        teacherMap = DataCacheTools.list2tMap(teacherList);

                        if (allStudentLessons != null && allStudentLessons.size() != 0)
                            loadRemindersUI();
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        cancelTimeOperation();
                        Map<String, String> maps = new HashMap<String, String>();
                        if (arg1 != null && arg1.length != 0) {

                            for (Header head : arg1) {
                                maps.put(head.getName(), head.getValue());
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

                            // 得到所有学生jiao'shi的信息
                            allStudentInfoDto = JsonHelp.getObject(studentInfo,
                                    AllStudentInfoDto.class);

                            if (allStudentInfoDto != null) {
                                studentList = allStudentInfoDto.getStudent();
                                studentMap = DataCacheTools.list2Map(studentList);
                                allStudentLessons = allStudentInfoDto.getAllStudentLessons();

                                //Update the reminders
                                if (allStudentLessons != null && allStudentLessons.size() != 0)
                                    loadRemindersUI();

                                // 将学生信息保存到本地
                                teacherList = allStudentInfoDto.getTeacher();
                                teacherMap = DataCacheTools.list2tMap(teacherList);

                                // 将学生信息保存到本地
                                FileTools.save2SDCard(
                                        Constants.STUDENT_INFO_DIR,
                                        Constants.STUDENT_INFO_FILE_NAME,
                                        studentInfo);
                            }
                        }
                        else
                        {
                            DebugClass.displayCurrentStack("Fail to get student info with return code: "+code);
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
                                Constants.MESSAGE_ID_HEART,
                                Constants.TIME_CHECK_HEART_PACKAGE);
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
                                Constants.MESSAGE_ID_HEART,
                                Constants.TIME_CHECK_HEART_PACKAGE);
                        cancelTimeOperation();
                        loadingLinearLayout.setVisibility(View.GONE);

                        Map<String, String> maps = new HashMap<String, String>();

                        if (arg1 != null && arg1.length != 0) {
                            for (Header head : arg1) {
                                maps.put(head.getName(), head.getValue());
                            }
                        }

                        // 获取header中参数Code的值
                        String code = maps.get("Code");
                        if (code == null) {
                            return;
                        }
                        // code=1时，成功；否则失败

                        String oldStudentInfo = FileTools.readFileByLines(
                                Constants.STUDENT_INFO_DIR + "/"
                                        + Constants.STUDENT_INFO_FILE_NAME, "");

                        if (oldStudentInfo.equals(""))
                            return;
                        allStudentInfoDto = JsonHelp.getObject(oldStudentInfo,
                                AllStudentInfoDto.class);

                        if ("1".equals(code)) {
                            String studentCheck = new String(arg2);
                            heartPackageDto = JsonHelp.getObject(studentCheck,
                                    HeartPackageDto.class);

                            if (heartPackageDto.getAddstudent().size() != 0
                                    || heartPackageDto.getCard().size() != 0
                                    || heartPackageDto.getModifystudent().size() != 0
                                    || heartPackageDto.getReceiver().size() != 0
                                    || heartPackageDto.getOutstudent().size() != 0) {
                                // 根据心跳包更新studentList数据
                                studentList = DataBaseUtils
                                        .updateStudentsInfoWithHeartPackage(
                                                studentMap, heartPackageDto);
                                teacherList = DataBaseUtils.updateTeachersInfoWithHeartPackage(teacherMap, heartPackageDto);


                                allStudentInfoDto.setStudent(studentList);
                                allStudentInfoDto.setTeacher(teacherList);


                                DebugClass.displayCurrentStack("add reminder: " + allStudentLessons.size());

                                if (allStudentLessons != null && allStudentLessons.size() != 0)
                                    loadRemindersUI();

                                // 将学生信息保存到本地
                                FileTools.save2SDCard(
                                        Constants.STUDENT_INFO_DIR,
                                        Constants.STUDENT_INFO_FILE_NAME,
                                        JsonHelp.jsonObjectToString(allStudentInfoDto));
                            }

                        }
                        //Is relocating, remove the databases
                        else if ("2".equals(code)) {
                            FileTools.deleteDirectory(Constants.BASE_CACHE_DIR);
                        }

                        UIUtils.showToastGetHeartPackageCode(code,
                                getApplicationContext());
                    }
                });
    }

    /**
     * Compose the reminders and health check package to server
     *
     * @return
     */
    private AttendanceStateBean composeAttendanceStateBean() {
        AttendanceStateBean attStateBean = null;


        if (cardNum != null && allStudentLessons != null) {
            attStateBean = new AttendanceStateBean();
            StringBuilder reminders = new StringBuilder();
            for (int i = 0; i < allStudentLessons.size(); i++) {
                if (allStudentLessons.get(i).isSelected) {
                    reminders.append(allStudentLessons.get(i).getLessonid()).append(",");
                }
            }
            attStateBean.setReminder(reminders.toString());
            attStateBean.setHealthState(healthState);
            attStateBean.setCardid(cardNum);
            attStateBean.setMid(Utils.getMachineNum(this));
            attStateBean.setCreatetime(System.currentTimeMillis() / 1000);

            StringBuilder healthreminders = new StringBuilder();
            for (int i = 0; i < currentStudentLessons.size(); i++) {
                if (currentStudentLessons.get(i).isSelected) {
                    healthreminders.append(currentStudentLessons.get(i).getLessonid()).append(",");
                }
            }

            if(healthreminders.toString().equals(""))
            {
                attStateBean.setHealthState(0);
            }
            else
            {
                attStateBean.setHealthState(1);
            }


            attStateBean.setHealthReminder(healthreminders.toString());

        }

        return attStateBean;
    }

    // SurfaceHodler Callback handle to open the camera, off camera and photo
    // size changes
    // Camera.CameraInfo.CAMERA_FACING_FRONT(前置) 和
    // Camera.CameraInfo.CAMERA_FACING_BACK(后置)
    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

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


    private PalmTask palmTask;

    // 拍照
    private void takePic() {
//		camera.stopPreview();// stop the preview
        if(null == camera)
        {
            DebugClass.displayCurrentStack("error: camera has been released when take picture!");
            return;
        }
//        playSound();
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
            return BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
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
     * 播放照相的声音
     */
    private void playSound() {
        sp.play(soundId, 0.5F, 0.5F, 1, 0, 1);
    }


    /**
     * 操作摄像头获取的帧视频（即图片）
     *
     * @param bmp
     */
    protected void cardSwipeProcess(Bitmap bmp) {

        TimeCardBean currTimeCardBean = new TimeCardBean();

        currTimeCardBean.setMachine(Utils.getMachineNum(this));
        currTimeCardBean.setCreatetime(System.currentTimeMillis() / 1000);
        currTimeCardBean.setSmartid(cardNum);
        Bitmap newBitmap = FileTools.resizeImage(bmp, 200, 200);
        base64 = FileTools.bitmapToBase64(newBitmap);
        currTimeCardBean.setFbody(base64);

        Message msg = mainHandler.obtainMessage();
        msg.what = Constants.MESSAGE_ID_GEN_CARD_INFO;
        msg.obj = currTimeCardBean;

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
                                "Bro, the network signal is bad!");
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
                // 收到消息就跳转到广告界面
                case Constants.MESSAGE_ID_IDLE_DEVICE:
                    mainToAdvSwap();
                    // 清空UI界面
                    clearUI();

                    // Clear the card number
                    cardNum = null;

                    // 同时将SettingDialog退出
                    if (settingDialog != null && settingDialog.isShowing()) {
                        settingDialog.dismiss();
                    }

                    break;
                // 每隔10秒检测一次是否联网
                case Constants.MESSAGE_ID_CHECK_NETWORK:
                    // 如果没联网则提示
                    if (!Utils.checkNetworkInfo(MainActivity.this)) {
                        UIUtils.showToastId(MainActivity.this,
                                R.string.taost_check_network,
                                R.string.taost_check_network_en);
                    }
                    // 轮询检测
                    sendEmptyMessageDelayed(Constants.MESSAGE_ID_CHECK_NETWORK,
                            Constants.TIME_CHECK_NETWORK);

                    break;
                // 10秒更新一次时间
                case Constants.MESSAGE_ID_UPDATE_TIME:
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
                    sendEmptyMessageDelayed(Constants.MESSAGE_ID_UPDATE_TIME, Constants.TIME_UPDATE_TIME);
                    break;
                // 每30分钟更新一个心跳包
                case Constants.MESSAGE_ID_HEART:

                    getStudentCheck();

                    break;
                // 打卡之后后，等30秒再和健康状态信息一起上传
                case Constants.MESSAGE_ID_UPLOAD_DISEASE:
                    if (timing < 30) {
                        timing++;
                        sendEmptyMessageDelayed(Constants.MESSAGE_ID_UPLOAD_DISEASE, 1000);
                    } else {
                        mainHandler.sendEmptyMessage(Constants.MESSAGE_ID_GEN_CARD_INFO);
                    }

                    break;
                // 开始上传打卡信息
                case Constants.MESSAGE_ID_GEN_CARD_INFO:
                    timing = 0;

                    TimeCardBean tmcardBean = (TimeCardBean) msg.obj;
                    String cardData = JsonHelp.jsonObjectToString(tmcardBean);

                    // 文件名称：yyMMddHHmmss+cardid+.dto
                    FileTools.save2SDCard(Constants.CARD_INFO_DIR_NO,
                            DateTools.getDateForm("yyMMddHHmmss", System.currentTimeMillis()) + tmcardBean.getSmartid() + ".dto",
                            cardData);

                    //Set reminders to default
                    //setReminders2Default();

                    // 重新计时30秒无操作
                    cancelTimeOperation();

                    break;
                // 手动同步结束
                case Constants.MESSAGE_ID_UPDATE_UI:
                    loadRemindersUI();
                    break;
                case Constants.MESSAGE_ID_UPDATE_CONFIRM_BUTTON:
                    int reminderClickedNum = remindersAdapter.getClickedNum();
                    int healthReminderClickedNum = healthCheckAdapter.getClickedNum();
                    if(healthReminderClickedNum>0)
                    {
                        confirmImageView.setImageResource(R.drawable.confirm_red);
                    }
                    else
                    {
                        confirmImageView.setImageResource(reminderClickedNum>0?R.drawable.confirm_blue:R.drawable.confirm_white);
                    }
                    healthCheckAdapter.notifyDataSetChanged();
                    remindersAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    /**
     * 清空UI界面
     */
    private void clearUI() {
        // 将界面信息清空
        nameTextView.setText("");
        schoolTextView.setText("");
        classTextView.setText("");
        cardTextView.setText("");
        cardNumEditText.setText("");
        portraitImageView.setImageResource(R.drawable.portrait_big);
        if (receiverAdapter != null) {
            receiverAdapter.setReceiverList(new ArrayList<RecieverDto>());
            receiverAdapter.notifyDataSetChanged();
        }

        if(healthCheckAdapter!=null)
        {
            if(currentStudentLessons !=null)
            {
                for (int i=0;i< currentStudentLessons.size();i++)
                {
                    LessionDto health= currentStudentLessons.get(i);
                    health.isSelected = false;
                }
                healthCheckAdapter.setReminderDtoList(currentStudentLessons);
                healthCheckAdapter.notifyDataSetChanged();
            }
        }

        if(remindersAdapter!=null)
        {
            if(allStudentLessons !=null)
            {
                for (int i=0;i< allStudentLessons.size();i++)
                {
                    LessionDto dto= allStudentLessons.get(i);
                    dto.isSelected = false;
                }
                remindersAdapter.setReminderDtoList(allStudentLessons);
                remindersAdapter.notifyDataSetChanged();

            }
        }

        confirmImageView.setImageResource(R.drawable.confirm_white);
    }

    private void UploadAttState(AttendanceStateBean attState) {
        WebService webService = WebServiceImpl.getInstance();
        webService.postReminderHealthState(attState, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                Map<String, String> maps = new HashMap<String, String>();
                if (arg1 != null && arg1.length != 0) {
                    for (Header head : arg1) {
                        maps.put(head.getName(), head.getValue());
                    }
                }

                // 获取header中参数Code的值
                String code = maps.get("Code");
                // code=1时，成功；否则失败
                if ("1".equals(code)) {
                    showTextViewToast("记录成功！", "Reminders Captured");
                }
            }
        });
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
                mainHandler.removeMessages(Constants.MESSAGE_ID_IDLE_DEVICE);
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

                    for (Header head : arg1) {
                        maps.put(head.getName(), head.getValue());
                    }
                }

                avdDto = JsonHelp.getObject(new String(arg2), AvdDto.class);
                if (avdDto != null) {
                    FileTools.loadAvdImage(avdImageView, avdDto.getFilepath(),
                            Constants.AVD_PIC_DIR, Constants.AVD_PIC_NAME);
                    DebugClass.displayCurrentStack("Success get AD and success to  ecode pic");
                }
                else
                {
                    DebugClass.displayCurrentStack("Success get AD but fail to decode pic");
                }
            }

        });

    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        cancelTimeOperation();

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if ((System.currentTimeMillis() - exitTime) > 800) {
                UIUtils.showToastId(this, R.string.taost_press_again,
                        R.string.taost_press_again_en);
                exitTime = System.currentTimeMillis();
            } else {
                if (mainHandler != null) {
                    mainHandler.removeMessages(Constants.MESSAGE_ID_IDLE_DEVICE);
                    mainHandler.removeMessages(Constants.MESSAGE_ID_CHECK_NETWORK);
                    mainHandler.removeMessages(Constants.MESSAGE_ID_UPDATE_TIME);
                    mainHandler.removeMessages(Constants.MESSAGE_ID_HEART);
                    mainHandler.removeMessages(Constants.MESSAGE_ID_UPLOAD_DISEASE);
                    mainHandler.removeMessages(Constants.MESSAGE_ID_GEN_CARD_INFO);
                }
                finish();
            }
            return true;
        }
        else if(keyCode == KeyEvent.KEYCODE_HOME)
        {
            if(Constants.DEBUG_MODE_ON)            return true;
        }
//        just for test on devices without NFC function
        else if(keyCode == KeyEvent.KEYCODE_MENU)
        {
            if(Constants.DEBUG_MODE_ON)
            {
                Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setAction(NfcAdapter.ACTION_TAG_DISCOVERED);
                Parcel cardId = Parcel.obtain();
                cardId.writeString("440312967");
                Tag tag = Tag.CREATOR.createFromParcel(cardId);
                intent.putExtra(NfcAdapter.EXTRA_TAG, tag);
                startActivity(intent);
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 各种点击事件
     */
    @Override
    public void onClick(View arg0) {

        cancelTimeOperation();

        switch (arg0.getId()) {
            case R.id.information:
                settingDialog = new SettingDialog(this, mainHandler);
                settingDialog.show();
                break;
//            case R.id.health_state_ill:
//                if (cardNum == null) {
//                    showTextViewToast("请先刷卡！", "Please swipe card!");
//                    break;
//                }
//                healthState = 0;
//                TextView textView = (TextView) findViewById(R.id.textView_center);
//                textView.setText(getResources().getString(R.string.healthstate_ill));
//                break;
//            case R.id.health_state_well:
//                if (cardNum == null) {
//                    showTextViewToast("请先刷卡！", "Please swipe card!");
//                    break;
//                }
//                healthState = 1;
//                textView = (TextView) findViewById(R.id.textView_center);
//                textView.setText(getResources().getString(R.string.healthstate_well));
//                break;
            //确认
            case R.id.imageView_confirm:
                if (cardNum == null) {
                    showTextViewToast("请先刷卡！", "Please swipe card!");
                    break;
                }
                AttendanceStateBean attState = this.composeAttendanceStateBean();
                if (attState != null && (attState.getReminder() != "" ))
                    UploadAttState(attState);
                else
                    showTextViewToast("默认状态！", "Default Status");
                break;
            //取消
            default:
                break;
        }

    }

    // 重新计时
    private void cancelTimeOperation() {
        advToMainSwap();
        mainHandler.removeMessages(Constants.MESSAGE_ID_IDLE_DEVICE);
        mainHandler.sendEmptyMessageDelayed(Constants.MESSAGE_ID_IDLE_DEVICE,
                Constants.TIME_NO_OPERATION);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            //Restart all timers
            cancelTimeOperation();

            Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            cardNum = getNFCTagId(tag);

         //   cardNum = "76968361323170";
            if(Constants.DEBUG_MODE_ON)
                cardNum = "440312967";
            showTextViewToast("swipe card", "swipe card "+cardNum+"!");
            DebugClass.displayCurrentStack("get card number: "+cardNum);

            cardTextView.setText("卡号：\n" + cardNum);

            // 得到当前刷卡学生的信息
            Object currentObject = getCurrentInfo(cardNum);

            if(currentObject==null)
            {
                clearUI();
                showTextViewToast("此卡不存在！", "Card is not exist!");
                return;
            }
            if (currentObject instanceof StudentDto) {
                StudentDto studentDto = (StudentDto) currentObject;
                loadStudentInfoUI(studentDto);
                loadReceiverInfoUI(studentDto);
                currentStudentLessons = loadCurriculum(allStudentInfoDto, cardNum);
                loadHealthRemiderUI();                takePic();

            } else if (currentObject instanceof TeacherDto) {
                TeacherDto teacherDto = (TeacherDto) currentObject;
                loadTeacherInfoUI(teacherDto);
                takePic();


            } else {
                clearUI();
                showTextViewToast("此卡不存在！", "Card is not exist!");
            }
        }
    }


    private String getNFCTagId(Parcelable p) {
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

    @Override
    public void onNewIntent(Intent intent) {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (pm.isScreenOn()) {
            setIntent(intent);
            resolveIntent(intent);
        }
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void openCamera() {
        if (isCameraRunning)
            return;
        else {

            try {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT); // Turn on the camera
                camera.setPreviewDisplay(surfaceHolder); // Set Preview
                Camera.Parameters parameters = camera.getParameters(); // Camera
                CameraInfo info = new CameraInfo();
                Camera.getCameraInfo(1, info);
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                int degrees = 0;
                switch (rotation) {
                    case Surface.ROTATION_0:
                        degrees = 0;
                        break;
                    case Surface.ROTATION_90:
                        degrees = 90;
                        break;
                    case Surface.ROTATION_180:
                        degrees = 180;
                        break;
                    case Surface.ROTATION_270:
                        degrees = 270;
                        break;
                }
                int result;
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    result = (info.orientation + degrees) % 360;
                    result = (360 - result) % 360;   // compensate the mirror
                } else {
                    // back-facing
                    result = (info.orientation - degrees + 360) % 360;
                }
                camera.setDisplayOrientation(result);
                // parameters to obtain
                parameters.setPictureFormat(PixelFormat.JPEG);// Setting Picture
                // parameters.set("rotation", 180); // Arbitrary rotation
                //camera.setDisplayOrientation(180);
                // parameters.setPreviewSize(400, 300); // Set Photo Size
                camera.setParameters(parameters); // Setting camera parameters
                isCameraRunning = true;
            } catch (IOException e) {
                camera.stopPreview();
                camera.release();// release camera
                camera = null;
                isCameraRunning = false;
            }
            catch (RuntimeException e)
            {
                camera = null;
                e.printStackTrace();
                isCameraRunning = false;
            }
        }
    }

    private void startPreview() {
        if (isCameraRunning)
            camera.startPreview();
    }

    private void closeCamera() {
        if (null != camera) {
            camera.setOneShotPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }

        isCameraRunning = false;
    }

    private void advToMainSwap() {
        advRelativeLayout.setVisibility(View.GONE);

        mainRelativeLayout.setVisibility(View.VISIBLE);

        openCamera();

        startPreview();
    }

    private void mainToAdvSwap() {
        advRelativeLayout.setVisibility(View.VISIBLE);

        mainRelativeLayout.setVisibility(View.GONE);

        closeCamera();
    }
}