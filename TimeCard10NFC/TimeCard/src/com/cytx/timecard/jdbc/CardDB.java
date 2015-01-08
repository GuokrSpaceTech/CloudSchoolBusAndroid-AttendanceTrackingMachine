//package com.cytx.timecard.jdbc;
//
//import android.content.ContentValues;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.cytx.timecard.TimeCardApplicatoin;
//import com.cytx.timecard.dto.SmartCardInfoDto;
//
///**
// * 创建时间：2014年8月9日 上午9:29:26 项目名称：TimeCard
// *
// * @author ben
// * @version 1.0 文件名称：CardDB.java 类说明：
// */
//public class CardDB {
//
//	public static final String KEY_STUDENT_ID = "studentid";
//	public static final String KEY_CARD_ID = "cardid";
//
//	static final String SQLITE_TABLE = "card";
//
//	private SQLiteDatabase db;
//
//	public CardDB() {
//		db = TimeCardApplicatoin.getInstance().getSqLiteDatabase();
//	}
//
//	/**
//	 * 插入考勤卡表数据
//	 */
//	public long addCard(SmartCardInfoDto cardDto) {
//		long createResult = 0;
//		ContentValues initialValues = new ContentValues();
//		initialValues.put(KEY_CARD_ID, cardDto.getCardid());
//		initialValues.put(KEY_STUDENT_ID, cardDto.getStudentid());
//		try {
//			createResult = db.insert(SQLITE_TABLE, null, initialValues);
//		} catch (Exception e) {
//
//		}
//		return createResult;
//	}
//
//	public boolean clearCards() {
//		int isDelete = 0;
//		try
//		{
//			isDelete = db.delete(SQLITE_TABLE, null, null  );
//		} catch (Exception e) {
//		}
//
//		return isDelete > 0;
//	}
//	/**
//	 * 通过cardid删除考勤卡信息
//	 */
//	public boolean deleteCardById(String cardid) {
//		int isDelete = 0;
//
//		try {
//
//			String[] tName = new String[] { cardid };
//			isDelete = db.delete(SQLITE_TABLE, KEY_CARD_ID + "=?", tName);
//
//		} catch (Exception e) {
//		}
//		return isDelete > 0;
//	}
//
//}
