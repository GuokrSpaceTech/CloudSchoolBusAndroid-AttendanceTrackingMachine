package com.cytx.timecardmobile.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cytx.timecardmobile.TimeCardApplicatoin;
import com.cytx.timecardmobile.constants.Constants;
import com.cytx.timecardmobile.dto.StudentDto;
import com.cytx.timecardmobile.dto.TeacherDto;

/**
 * 创建时间：2014年7月30日 下午2:21:04 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：DataCacheTools.java 类说明： 缓存各种数据
 */
public class DataCacheTools {

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
