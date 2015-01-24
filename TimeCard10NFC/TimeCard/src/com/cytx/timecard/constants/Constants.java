package com.cytx.timecard.constants;

import com.cytx.timecard.utility.FileTools;

/**
 * 创建时间：2014年7月30日 下午1:53:54  
 * 项目名称：TimeCard  
 * @author ben
 * @version 1.0
 * 文件名称：Constants.java  
 * 类说明： 常量类
 */
public class Constants {

    public static final String INTENT_START_SERVICE_ONLY = "com.cytx.timecard.startserviceonly";
    public static final String INTENT_START_SERVICE_ACTIVITY = "com.cytx.timecard.startserviceactivity";
    public static final String INTENT_START_ACTIVITY_ONLY = "com.cytx.timecard.startactivityonly";
    // 是否需要版本更新
	public static boolean IS_NEED_UPDATE = false;
	//=====================数据缓存=================//
	// 是否清除缓存数据
	public static final boolean IS_CLEAR_CACHE = true;

    // base cache directory
	public static final String BASE_CACHE_DIR = FileTools.getSDcardPath() + "/yzxc/";
    /**
	 *  学生数据缓存目录
	 */
	public static final String STUDENT_CACHE_DIR = BASE_CACHE_DIR + "/studentInfo";
    // 学生数据照片存储路径
	public static final String STUDENT_PICTURES_DIR = STUDENT_CACHE_DIR + "/picture";
    // 学生头像存储路径
	public static final String STUDENT_PORTRAIT = STUDENT_PICTURES_DIR + "/student_portrait";
    // 接送人头像缓存路径
	public static final String RECEIVER_PORTRAIT = STUDENT_PICTURES_DIR + "/receiver_portrait";
    // 所有的学生信息存储路径
	public static final String STUDENT_INFO_DIR = STUDENT_CACHE_DIR + "/info";
    // 学生信息文件名称
	public static final String STUDENT_INFO_FILE_NAME = "studentInfo.dto";
    // 心跳包信息文件名称
	public static final String STUDENT_CHECK_FILE_NAME = "heartpackage.dto";
	/**
	 * 打卡信息缓存路径
	 */
	public static final String CARD_CACHE_DIR = BASE_CACHE_DIR + "/cardInfo";

	//yes:表示已经上传的打卡信息
	public static final String CARD_CACHE_DIR_YES = CARD_CACHE_DIR + "/yes";

    //no:表示未上传的打卡信息
	public static final String CARD_CACHE_DIR_NO = CARD_CACHE_DIR + "/no";
    //err:表示错误的打卡信息
	public static final String CARD_CACHE_DIR_FAIL = CARD_CACHE_DIR + "/fail";
	// 打卡信息存储的路径:已上传
	public static final String CARD_INFO_DIR_YES = CARD_CACHE_DIR_YES + "/info";

    // 打卡信息存储的路径:未上传
	public static final String CARD_INFO_DIR_NO = CARD_CACHE_DIR_NO + "/info";
    // 打卡信息存储的路径:错误
	public static final String CARD_INFO_DIR_FAIL = CARD_CACHE_DIR_FAIL + "/info";
	/**
	 * 广告图片存储的路径
	 */
	// 广告图片缓存目录
	public static final String AVD_PIC_DIR = BASE_CACHE_DIR + "/avd";

    // 广告图片名称
	public static final String AVD_PIC_NAME = "cytx_avd.jpg";
	//=============header参数字段================//
	public static final String VERSION = "Version";// 版本号


    public static final String APIKEY = "apikey";// 唯一的apikey
	//=============Url=========================//
	// BaseUrl
//	public static final String BASE_URL = "http://124.64.109.181:81/";
//	public static final String BASE_URL = "http://222.128.71.186:81/";
//	public static final String BASE_URL = "http://apitest.yunxiaoche.com/";
	public static final String BASE_URL = "http://api35.yunxiaoche.com:81/";

    // 下载所有的学生信息
	public static final String SCHOOL_STUDENT_URL = BASE_URL + "/nfcallinfo";
    // 下载心跳包信息
	public static final String SCHOOL_CHECK_URL = BASE_URL + "/nfccheck";
    // 考勤卡
	public static final String SMART_CARD_URL = BASE_URL + "/nfcsmartcard";
    // 获取广告图片
	public static final String AVD_PICTURE_URL = BASE_URL + "/schoolad";
	//upload reminders and health
	public static final String HEALTH_STATE_REMINDER_URL = BASE_URL + "/attendancestate";

	//============Handler 消息==================//
	public static final int MESSAGE_ID_IDLE_DEVICE = 1;// 30秒待机Message
    public static final int MESSAGE_ID_CHECK_NETWORK = 2;// 10秒检测一次是否有网络Message
    public static final int MESSAGE_ID_UPDATE_TIME = 3;// 10秒刷新一次时间
    public static final int MESSAGE_ID_HEART = 4;// 30分钟更新一次心跳包
    public static final int MESSAGE_ID_UPLOAD_DISEASE = 5;// 每30秒钟检查是否有上传疾病信息
    public static final int MESSAGE_ID_GEN_CARD_INFO = 6;// 开始上传打卡信息
    public static final int MESSAGE_ID_UPDATE_UI = 7;
    public static final int MESSAGE_ID_UPDATE_CONFIRM_BUTTON = 8;
    public static final int MESSAGE_ID_CLEAN_OLD_CARD_INFO = 9;
    public static final int MESSAGE_ID_UPLOAD_CARD_INFO = 10;

    public static final boolean DEBUG_MODE_ON = true;


	public static final int TIME_NO_OPERATION = 60 * 1000;// 30秒无操作
    public static final int TIME_CHECK_NETWORK = 30 * 1000;// 10秒钟检测一次网络
    public static final int TIME_UPDATE_TIME = 10 * 1000;// 10秒钟更新一次时间
	public static final int TIME_CHECK_HEART_PACKAGE = 30 * 60 * 1000;// 每30分钟更新一次心跳包
	public static final int SUBMIT_DISEASE_30 = 30 * 1000;// 每30秒钟检查是否有上传疾病信息

	//=======================错误处理========================//
	// 无法解析主机
	public static final String HOST_ERROR = "can't resolve host";
	// 网络超时
	public static final String SOCKET_TIME_OUT ="socket time out";
	
	// 数据库文件路径
	
	// sharepreference数据KEY
	public static final String FIRST_DATE = "firstDate";
}
