package com.cytx.timecard.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.view.View;

import com.cytx.timecard.constants.Constants;

/**
 * 创建时间：2014年8月2日 下午11:14:30 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：Utils.java 类说明：公用方法
 */
public class Utils {

	/**
	 * 判断用户是否联网
	 * 
	 * @param context
	 * @return 如果有可用网络，则返回true；否则返回false
	 */
	public static boolean checkNetworkInfo(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null)
			return false;
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 通过图片路径加载图片(增加了getField("inNativeAlloc")，尝试解决图片内存的问题
	 */
	public static Bitmap getOptionBitmap(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		opts.inJustDecodeBounds = false;
		opts.inDither = false;
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inPurgeable = true;
		try {
			BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(
					opts, true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return BitmapFactory.decodeFile(path, opts);
	}

	/**
	 * 将图片设置为圆角
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		if (bitmap == null) {
			return bitmap;
		}
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	// 获取machine
	public static String getMachineNum(Context context) {
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String szWLANMAC = wm.getConnectionInfo().getMacAddress();
        if(Constants.DEBUG_MODE_ON)
        {
            return "30e96523eb91"; // TODO: you can change it to a working mac for testing
        }
        else
        {
            return szWLANMAC == null ? "" : szWLANMAC.replaceAll(":", "");
        }
    }

	/**
	 * 清除打卡缓存数据:已上传
	 * 
	 * @return
	 */
	public static void clearCardCache() {
		FileTools.deleteDirectory(Constants.CARD_CACHE_DIR_YES);
	}

	/**
	 * 清除所有学生数据
	 */
	public static void clearStudentCache() {
		FileTools.deleteDirectory(Constants.STUDENT_INFO_DIR);
	}

	public static Bitmap takeScreenShot(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		Rect rect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		int statusBarHeight = rect.top;
		System.out.println(statusBarHeight);

		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();

		Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width,
				height - statusBarHeight);
		view.destroyDrawingCache();
		return bitmap2;
	}
	
	public static String getFileNameFromPath(String filePath)
	{
		String pathSegs[] = filePath.split("\\/");
		if(pathSegs.length != 0)
			return pathSegs[pathSegs.length-1];
		else
		    return "";
	}

}
