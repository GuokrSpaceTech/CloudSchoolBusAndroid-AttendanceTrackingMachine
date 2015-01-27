package com.cytx.timecard.dto;

import java.util.List;

/**
 * 创建时间：2014年8月8日 下午8:55:38 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：AllStudentInfoDto.java 类说明：
 */
public class AllStudentInfoDto {

    private Boolean is_training_agency;
	private List<StudentDto> student;
    private List<HealthReminder> reminderstate;
    private List<HealthStateDto> healthstate;
    private List<TeacherDto> teacher;

    public Boolean getIs_training_agency() {
        return is_training_agency;
    }

    public void setIs_training_agency(Boolean is_training_agency) {
        this.is_training_agency = is_training_agency;
    }

    public List<HealthReminder> getReminderstate() {
        return reminderstate;
    }
    public void setReminderstate(List<HealthReminder> reminderstate) {
        this.reminderstate = reminderstate;
    }
	public List<StudentDto> getStudent() {
		return student;
	}

	public void setStudent(List<StudentDto> student) {
		this.student = student;
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
