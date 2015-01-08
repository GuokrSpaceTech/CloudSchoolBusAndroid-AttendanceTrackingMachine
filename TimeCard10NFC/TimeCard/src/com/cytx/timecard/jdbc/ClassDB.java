//package com.cytx.timecard.jdbc;
//
//import android.content.ContentValues;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.cytx.timecard.TimeCardApplicatoin;
//import com.cytx.timecard.dto.ClassInfoDto;
//
///**
// * 创建时间：2014年8月9日 上午9:29:26 项目名称：TimeCard
// *
// * @author ben
// * @version 1.0 文件名称：CardDB.java 类说明：
// */
//public class ClassDB {
//
//	public static final String KEY_ROWID = "_id";
//	public static final String KEY_STUDENT_ID = "studentid";
//	public static final String KEY_CLASSNAME = "classname";
//
//	static final String SQLITE_TABLE = "class";
//
//	private SQLiteDatabase db;
//
//	public ClassDB() {
//		db = TimeCardApplicatoin.getInstance().getSqLiteDatabase();
//	}
//
//	/**
//	 * 插入班级表数据
//	 */
//	public long addClass(ClassInfoDto classDto, String studentid) {
//		long createResult = 0;
//		ContentValues initialValues = new ContentValues();
//		initialValues.put(KEY_STUDENT_ID, studentid);
//		initialValues.put(KEY_CLASSNAME, classDto.getClassname());
//		try {
//			createResult = db.insert(SQLITE_TABLE, null, initialValues);
//		} catch (Exception e) {
//
//		}
//		return createResult;
//	}
//
//	/**
//	 * 通过studentid删除班级信息
//	 */
//	public boolean deletClassById(String studentid) {
//		int isDelete = 0;
//		try {
//
//			String[] tName = new String[] { studentid };
//			isDelete = db.delete(SQLITE_TABLE, KEY_STUDENT_ID + "=?", tName);
//		} catch (Exception e) {
//		}
//		return isDelete > 0;
//	}
//
//	public boolean clearClass() {
//		int isDelete = 0;
//		try
//		{
//			isDelete = db.delete(SQLITE_TABLE, null, null  );
//		} catch (Exception e) {
//		}
//		return isDelete > 0;
//	}
//	/**
//	 *  操作心跳包数据
//	 */
//	public long addClassItem(ClassInfoDto classDto, String studentid) {
//		long createResult = 0;
//		ContentValues initialValues = new ContentValues();
//		initialValues.put(KEY_STUDENT_ID, studentid);
//		initialValues.put(KEY_CLASSNAME, classDto.getClassname());
//		try {
//			createResult = db.insert(SQLITE_TABLE, null, initialValues);
//		} catch (Exception e) {
//
//		}
//		return createResult;
//	}
//
//}
