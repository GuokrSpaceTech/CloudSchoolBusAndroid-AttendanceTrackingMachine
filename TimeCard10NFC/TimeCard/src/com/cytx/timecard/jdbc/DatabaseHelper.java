//package com.cytx.timecard.jdbc;
//
//import android.content.Context;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
///**
// * 创建时间：2014年8月9日 上午9:25:16 项目名称：TimeCard
// *
// * @author ben
// * @version 1.0 文件名称：DatabaseHelper.java 类说明：
// */
//public class DatabaseHelper extends SQLiteOpenHelper {
//
//	public static final String DATABASE_NAME = "yzxc.db"; // 数据库名称
//
//	public static final int DATABASE_VERSION = 1;
//	// 创建该数据库下学生表的语句:studentid唯一
//	private static final String CREATE_TABLE_STUDENT = "CREATE TABLE if not exists "
//			+ StudentDB.SQLITE_TABLE
//			+ " ("
//			+ StudentDB.KEY_STUDENT_ID
//			+ " integer PRIMARY KEY autoincrement,"
//			+ StudentDB.KEY_CNNAME
//			+ ","
//			+ StudentDB.KEY_PID
//			+ ","
//			+ StudentDB.KEY_SCHOOL_NAME
//			+ ","
//			+ StudentDB.KEY_AWATAR + ");";
//	// 创建该数据库下班级表的语句:没有唯一性
//	private static final String CREATE_TABLE_CLASS = "CREATE TABLE if not exists "
//			+ ClassDB.SQLITE_TABLE
//			+ " ("
//			+ ClassDB.KEY_ROWID
//			+ " integer PRIMARY KEY autoincrement,"
//			+ ClassDB.KEY_CLASSNAME
//			+ "," + ClassDB.KEY_STUDENT_ID + ");";
//
//	// 创建该数据库考勤卡级表的语句:cardid唯一
//	private static final String CREATE_TABLE_CARD = "CREATE TABLE if not exists "
//			+ CardDB.SQLITE_TABLE
//			+ " ("
//			+ CardDB.KEY_CARD_ID
//			+ " integer PRIMARY KEY autoincrement,"
//			+ CardDB.KEY_STUDENT_ID
//			+ ");";
//	// 创建该数据库下接送孩子的家长表的语句:pid唯一
//	private static final String CREATE_TABLE_RECEIVER = "CREATE TABLE if not exists "
//			+ ReceiverDB.SQLITE_TABLE
//			+ " ("
//			+ ReceiverDB.KEY_ID
//			+ " integer PRIMARY KEY autoincrement,"
//			+ ReceiverDB.KEY_PID
//			+ ","
//			+ ReceiverDB.KEY_FILEPATH
//			+ ","
//			+ ReceiverDB.KEY_RELATIONSHIP
//			+ ","
//			+ ReceiverDB.KEY_STUDENT_ID + ");";
//
//	private SQLiteDatabase db;
//
//	public DatabaseHelper(Context context) {
//		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase arg0) {
//		this.db = arg0;
//		db.execSQL(CREATE_TABLE_STUDENT);
//		db.execSQL(CREATE_TABLE_CARD);
//		db.execSQL(CREATE_TABLE_RECEIVER);
//		db.execSQL(CREATE_TABLE_CLASS);
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
//
//	}
//
//	/**
//	 * 打开数据库
//	 *
//	 * @throws SQLException
//	 */
//	public SQLiteDatabase open() throws SQLException {
//		db = this.getWritableDatabase();
//		return db;
//	}
//
//	/**
//	 * 关闭数据库
//	 */
//	public void close() {
//		this.close();
//	}
//
//}
