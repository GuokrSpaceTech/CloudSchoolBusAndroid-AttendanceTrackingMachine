//package com.cytx.timecard.jdbc;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.cytx.timecard.TimeCardApplicatoin;
//import com.cytx.timecard.dto.RecieverAddDto;
//import com.cytx.timecard.dto.RecieverDto;
//
///**
// * 创建时间：2014年8月9日 上午9:29:26 项目名称：TimeCard
// *
// * @author ben
// * @version 1.0 文件名称：CardDB.java 类说明：
// */
//public class ReceiverDB {
//
//	public static final String KEY_ID = "id";
//	public static final String KEY_PID = "pid";
//	public static final String KEY_FILEPATH = "filepath";
//	public static final String KEY_STUDENT_ID = "studentid";
//	public static final String KEY_RELATIONSHIP = "relationship";
//
//	static final String SQLITE_TABLE = "receiver";
//
//	private SQLiteDatabase db;
//
//	public ReceiverDB() {
//		db = TimeCardApplicatoin.getInstance().getSqLiteDatabase();
//	}
//
//	/**
//	 * 插入考勤卡表数据
//	 */
//	public long addReceiver(RecieverDto recieverDto, String studentid) {
//		long createResult = 0;
//		ContentValues initialValues = new ContentValues();
//		initialValues.put(KEY_FILEPATH, recieverDto.getFilepath());
//		initialValues.put(KEY_PID, recieverDto.getPid());
//		initialValues.put(KEY_RELATIONSHIP, recieverDto.getRelationship());
//		initialValues.put(KEY_STUDENT_ID, studentid);
//		try {
//			createResult = db.insert(SQLITE_TABLE, null, initialValues);
//		} catch (Exception e) {
//
//		}
//		return createResult;
//	}
//
//	/**
//	 * 通过删除考勤卡信息
//	 */
//	public boolean deleteReceiverById(String id) {
//		int isDelete = 0;
//
//		try {
//			String[] tName = new String[] { id };
//			isDelete = db.delete(SQLITE_TABLE, KEY_ID + "=?", tName);
//
//		} catch (Exception e) {
//		}
//		return isDelete > 0;
//	}
//
//	/**
//	 * 通过删除考勤卡信息
//	 */
//	public String deleteReceiverByFilepath(String filename) {
//		int isDelete = 0;
//		String id = "";
//
//		try {
//			String[] tName = new String[] { filename };
//			Cursor c = db.rawQuery("select id from " + SQLITE_TABLE + " where " + KEY_FILEPATH + "=? ",  tName);
//	        while (c.moveToNext()) {
//	        	id = c.getString(c.getColumnIndex(KEY_ID));
//	        	}
//            c.close();
//
//            if(id != "")
//    			isDelete = db.delete(SQLITE_TABLE, KEY_FILEPATH + "=?", tName);
//		} catch (Exception e) {
//		}
//
//		if( isDelete == 0)
//		    return "";
//		else
//		    return id;
//	}
//
//	public boolean clearReceivers() {
//		int isDelete = 0;
//		try
//		{
//			isDelete = db.delete(SQLITE_TABLE, null, null  );
//		} catch (Exception e) {
//		}
//
//		return isDelete > 0;
//	}
//
//	/**
//	 * 操作心跳包数据
//	 */
//	public long addReceiverItem(RecieverAddDto recieverDto) {
//		long createResult = 0;
//		ContentValues initialValues = new ContentValues();
//		initialValues.put(KEY_FILEPATH, recieverDto.getFilepath());
//		initialValues.put(KEY_PID, recieverDto.getPid());
//		initialValues.put(KEY_RELATIONSHIP, recieverDto.getRelationship());
//		initialValues.put(KEY_STUDENT_ID, recieverDto.getStudentid());
//		try {
//			createResult = db.insert(SQLITE_TABLE, null, initialValues);
//		} catch (Exception e) {
//
//		}
//		return createResult;
//	}
//
//
//}
