//package com.cytx.timecard.jdbc;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.cytx.timecard.TimeCardApplicatoin;
//import com.cytx.timecard.dto.AddStudentDto;
//import com.cytx.timecard.dto.ModifyStudentDto;
//import com.cytx.timecard.dto.StudentDto;
//
///**
// * 创建时间：2014年8月9日 上午9:27:32 项目名称：TimeCard
// *
// * @author ben
// * @version 1.0 文件名称：StudentDB.java 类说明：
// */
//public class StudentDB {
//
//	public static final String KEY_STUDENT_ID = "studentid";
//	public static final String KEY_PID = "pid";
//	public static final String KEY_CNNAME = "cnname";
//	public static final String KEY_SCHOOL_NAME = "schoolname";
//	public static final String KEY_AWATAR = "awatar";
//
//	public static final String SQLITE_TABLE = "student";
//	private SQLiteDatabase db;
//
//	public StudentDB() {
//		db = TimeCardApplicatoin.getInstance().getSqLiteDatabase();
//	}
//
//	/**
//	 * 插入学生表数据
//	 */
//	public long addStudent(StudentDto studentDto) {
//		long createResult = 0;
//		ContentValues initialValues = new ContentValues();
//		initialValues.put(KEY_STUDENT_ID, studentDto.getStudentid());
//		initialValues.put(KEY_CNNAME, studentDto.getCnname());
//		initialValues.put(KEY_PID, studentDto.getPid());
//		initialValues.put(KEY_SCHOOL_NAME, studentDto.getSchoolname());
//		initialValues.put(KEY_AWATAR, studentDto.getAvatar());
//		try {
//			createResult = db.insertWithOnConflict(SQLITE_TABLE, null, initialValues,5);
//		} catch (Exception e) {
//
//		}
//		return createResult;
//	}
//
//	/**
//	 * 通过studentid删除学生信息
//	 */
//	public boolean deletStudentById(String studentid) {
//		int isDelete = 0;
//		try {
//
//			String[] tName = new String[] { studentid };
//			isDelete = db.delete(SQLITE_TABLE, KEY_STUDENT_ID + "=?", tName);
//
//
//		} catch (Exception e) {
//
//		}
//		return isDelete > 0;
//	}
//
//	/**
//	 * 通过studentid get Student pid information
//	 */
//	public String getStudentPidById(String studentid) {
//		String pid = "";
//		try {
//			Cursor c = db.rawQuery("SELECT pid FROM "+ SQLITE_TABLE +" WHERE " + KEY_STUDENT_ID + "=?", new String[]{studentid});
//	        while (c.moveToNext()) {
//	        	pid = c.getString(c.getColumnIndex(KEY_PID));
//	        	}
//            c.close();
//		} catch (Exception e) {
//
//		}
//		return pid;
//	}
//
//	public boolean clearStudents() {
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
//	// 操作心跳包数据
//	public long addStudentItem(AddStudentDto addStudentDto) {
//		long createResult = 0;
//		ContentValues initialValues = new ContentValues();
//		initialValues.put(KEY_STUDENT_ID, addStudentDto.getStudentid());
//		initialValues.put(KEY_CNNAME, addStudentDto.getCnname());
//		try {
//			createResult = db.insert(SQLITE_TABLE, null, initialValues);
//		} catch (Exception e) {
//
//		}
//		return createResult;
//	}
//
//	//At this moment, only update the Avatar
//	public long updateStudentItem(ModifyStudentDto modifyStudentDto) {
//		long updateResult = 0;
//		ContentValues initialValues = new ContentValues();
//		initialValues.put(KEY_AWATAR, modifyStudentDto.getAvatar());
//		String[] tName = new String[] { modifyStudentDto.getStudentid() };
//		try {
//			updateResult = db.update(SQLITE_TABLE, initialValues, KEY_STUDENT_ID + "=?", tName);
//		} catch (Exception e) {
//
//		}
//		return updateResult;
//	}
//
//}
