package com.cytx.timecard.dto;

import java.util.List;

/**
 * 创建时间：2014年7月30日 下午2:46:03 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：ModifyStudentDto.java 类说明：更新学生信息
 */
public class ModifyStudentDto {

	private String pid;
	private String avatar;
	private String schoolname;
	private String studentid;
	private String cnname;
	private List<ClassInfoDto> classinfo;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}

	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}

	public String getCnname() {
		return cnname;
	}

	public void setCnname(String cnname) {
		this.cnname = cnname;
	}

	public List<ClassInfoDto> getClassinfo() {
		return classinfo;
	}

	public void setClassinfo(List<ClassInfoDto> classinfo) {
		this.classinfo = classinfo;
	}
}