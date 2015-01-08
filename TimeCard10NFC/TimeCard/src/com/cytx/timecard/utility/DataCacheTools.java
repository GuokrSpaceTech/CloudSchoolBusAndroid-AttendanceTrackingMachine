package com.cytx.timecard.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cytx.timecard.TimeCardApplicatoin;
import com.cytx.timecard.constants.Constants;
import com.cytx.timecard.dto.StudentDto;
import com.cytx.timecard.dto.TeacherDto;

/**
 * 创建时间：2014年7月30日 下午2:21:04 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：DataCacheTools.java 类说明： 缓存各种数据
 */
public class DataCacheTools {
	// 心跳数据包
//	private static HeartPackageDto heartPackageDtp = new HeartPackageDto();
	// 所有学生信息
//	private static List<StudentDto> studentList = new ArrayList<StudentDto>();

//	private static Map<String, StudentDto> studentMap = new HashMap<String, StudentDto>();

//	public static HeartPackageDto getHeartPackageDtp() {
//		return heartPackageDtp;
//	}

//	public static void setHeartPackageDtp(HeartPackageDto heartPackageDtp) {
//		DataCacheTools.heartPackageDtp = heartPackageDtp;
//	}

//	public static List<StudentDto> getStudentList() {
//		return studentList;
//	}

//	public static void setStudentList(List<StudentDto> studentList) {
//		DataCacheTools.studentList = studentList;
//	}

//	public static Map<String, StudentDto> getStudentMap() {
//		return studentMap;
//	}

//	public static void setStudentMap(Map<String, StudentDto> studentMap) {
//		DataCacheTools.studentMap = studentMap;
//	}

	/**
	 * map 转为 list
	 */
	public static List<StudentDto> map2List(Map<String, StudentDto> studentMap) {
		List<StudentDto> studentDtos = new ArrayList<StudentDto>();
		Collection<StudentDto> c = studentMap.values();
		Iterator it = c.iterator();
		for (; it.hasNext();) {
			studentDtos.add((StudentDto)it.next());
		}
		return studentDtos;
	}
	
	public static List<TeacherDto> mapt2List(Map<String, TeacherDto> teacherMap) {
		List<TeacherDto> teacherDtos = new ArrayList<TeacherDto>();
		Collection<TeacherDto> c = teacherMap.values();
		Iterator it = c.iterator();
		for (; it.hasNext();) {
			teacherDtos.add((TeacherDto)it.next());
		}
		return teacherDtos;
	}
	
	/**
	 * list 转 map
	 * @return
	 */
	public static Map<String, StudentDto> list2Map(List<StudentDto> studentList){
		Map<String, StudentDto> studentMap = new HashMap<String, StudentDto>();
		if (studentList == null || studentList.size() == 0) {
			return studentMap;
		}
		for (int i = 0; i < studentList.size(); i++) {
			StudentDto studentDto = studentList.get(i);
			studentMap.put(studentDto.getStudentid(), studentDto);
		}
		return studentMap;
	}

	
	public static Map<String, TeacherDto> list2tMap(List<TeacherDto> teacherList){
		Map<String, TeacherDto> teacherMap = new HashMap<String, TeacherDto>();
		if (teacherList == null || teacherList.size() == 0) {
			return teacherMap;
		}
		for (int i = 0; i < teacherList.size(); i++) {
			TeacherDto teacherDto = teacherList.get(i);
			teacherMap.put(teacherDto.getTeacherid(), teacherDto);
		}
		return teacherMap;
	}
	// 清除缓存数据：每个月1号将一个月前的数据删除。
	// 例如：6月1日将4月份的数据删除
//	public static void clearCacheDatas() {
//		// 如果不需要清除缓存数据，则return
//		if (!Constants.IS_CLEAR_CACHE) {
//			return;
//		}
//		// 如果不是1号，return
//		if (!"01".equals(DateTools.getDay())) {
//			return;
//		}
//		// 如果文件夹不存在，return
//		File dirFile = new File(Constants.BASE_CACHE_DIR);
//		if (dirFile == null || !dirFile.exists()) {
//			return;
//		}
//
//		File[] listFile = dirFile.listFiles();
//		// lengh小于3时，说明还不到两个月的数据，return
//		if (listFile == null || listFile.length < 3) {
//			return;
//		}
//
//		// 否则：同时存在三个文件夹，那么必须要删除一个文件夹的数据
//		// 本月
//		int currentMonth = Integer.valueOf(DateTools.getMonth());
//		// 两个月前
//		int twoMonthAgo = currentMonth - 2;
//		// 说明在同一年之内
//		if (twoMonthAgo > 0) {
//			// 得到两个月前数据存放的文件夹
//			String filePath = Constants.BASE_CACHE_DIR
//					+ DateTools.isHaseZero(twoMonthAgo + "");
//			// 清除缓存数据
//			FileTools.DeleteFolder(filePath);
//			return;
//		}
//
//		// 若是11、12和1月份的数据，那么删除11月份的数据
//		if ("01".equals(DateTools.getMonth())) {
//			String filePath = Constants.BASE_CACHE_DIR + "11";
//			FileTools.DeleteFolder(filePath);
//			return;
//		}
//
//		// 若是12、1和2月份的数据，那么删除12月份的数据
//		if ("02".equals(DateTools.getMonth())) {
//			String filePath = Constants.BASE_CACHE_DIR + "12";
//			FileTools.DeleteFolder(filePath);
//			return;
//		}
//
//	}

	/**
	 * 每5天清除一次已上传的打卡数据
	 */
	public static void clearCacheDatas() {
		// 如果不需要清除缓存数据，则return
		if (!Constants.IS_CLEAR_CACHE) {
			return;
		}
		
		String firstDate = TimeCardApplicatoin.getInstance().getStringFromShares(Constants.FIRST_DATE, "2014-08-18");
		long days = DateTools.getDaysBetween2Date(firstDate, DateTools.getCurrentDate());
		// 若相隔的天数是5的倍数，那么开始清除已上传的打卡数据
		if (days != 0 && days % 5 == 0) {
			Utils.clearCardCache();
		}
		
	}
}
