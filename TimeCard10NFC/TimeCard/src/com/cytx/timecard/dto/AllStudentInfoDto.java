package com.cytx.timecard.dto;

import java.util.List;

/**
 * 创建时间：2014年8月8日 下午8:55:38 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：AllStudentInfoDto.java 类说明：
 */
public class AllStudentInfoDto {

    private boolean is_training_agency;
	private List<StudentDto> student;
	private List<HealthStateDto> reminderstate;
	private List<HealthStateDto> healthstate;
	private List<TeacherDto> teacher;
	public List<StudentDto> getStudent() {
		return student;
	}

	public void setStudent(List<StudentDto> student) {
		this.student = student;
	}

	public List<HealthStateDto> getReminder() {
		return reminderstate;
	}

	public void setReminder(List<HealthStateDto> reminder) {
		this.reminderstate = reminder;
	}

	public List<TeacherDto> getTeacher() {
		return teacher;
	}

	public void setTeacher(List<TeacherDto> teacher) {
		this.teacher = teacher;
	}

	public List<HealthStateDto> getHealthstate() {
		return healthstate;
	}

	public void setHealthstate(List<HealthStateDto> healthstate) {
		this.healthstate = healthstate;
	}

}
