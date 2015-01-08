package com.cytx.timecard;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 创建时间：2014年7月30日 下午2:09:25 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：TimeCardApplicatoin.java 类说明：自定义Application
 */
public class TimeCardApplicatoin extends Application {

	private static TimeCardApplicatoin app;
//	private SQLiteDatabase db;
//	private DatabaseHelper databaseHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化异步加载图片类
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(options)
				.threadPoolSize(5).memoryCache(new WeakMemoryCache()).build();
		ImageLoader.getInstance().init(config);
		app = this;
//		DatabaseHelper databaseHelper = new DatabaseHelper(this);
//		db = databaseHelper.open();
	}

	public static TimeCardApplicatoin getInstance() {
		return app;
	}
//	// 获得SQLiteDatabase
//	public SQLiteDatabase getSqLiteDatabase(){
//		return db;
//	}
//	// 关闭数据库
//	public void closeDatabase(){
//		if (databaseHelper != null) {
//			databaseHelper.close();
//		}
//
//	}

	// 获取版本名称
	public String getVersionName() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		String versionName = "";
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			versionName = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	// 获取版本号
	public int getVersionCode() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		int versionCode = 0;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			versionCode = packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
	
	private static final String Config = "Server_Config";

	/**
	 * 保存字符串到Share
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setStringToShares(String key, String value) {
		if (key == null || value == null)
			return false;
		if (key.trim().equalsIgnoreCase(""))
			return false;
		SharedPreferences shares = getSharedPreferences(Config,
				Context.MODE_WORLD_WRITEABLE);
		Editor editor = shares.edit();
		editor.putString(key.trim(), value.trim());
		editor.commit();
		return true;
	}

	/**
	 * 从Share中获取字符串
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getStringFromShares(String key, String defaultValue) {
		if (key == null || defaultValue == null)
			return null;
		SharedPreferences shares = getSharedPreferences(Config,
				Context.MODE_WORLD_READABLE);
		String returnValue = shares.getString(key.trim(), defaultValue);
		return returnValue;
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		// 关闭数据库
//		closeDatabase();
		
	}


}
