package com.cytx.timecard.dto;

import java.util.List;

/**
 * 创建时间：2014年7月30日 下午1:47:05 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：Student.java 类说明：学生信息
 */
public class StudentDto {

	private String studentid;
	private String pid;
	private String cnname;
	private String avatar;
	private String schoolname;
	private List<RecieverDto> receiver;
	private List<ClassInfoDto> classinfo;
	private List<SmartCardInfoDto> smartcardinfo;

	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getCnname() {
		return cnname;
	}

	public void setCnname(String cnname) {
		this.cnname = cnname;
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

	public List<RecieverDto> getReceiver() {
		return receiver;
	}

	public void setReceiver(List<RecieverDto> receiver) {
		this.receiver = receiver;
	}

	public List<ClassInfoDto> getClassinfo() {
		return classinfo;
	}

	public void setClassinfo(List<ClassInfoDto> classinfo) {
		this.classinfo = classinfo;
	}

	public List<SmartCardInfoDto> getSmartcardinfo() {
		return smartcardinfo;
	}

	public void setSmartcardinfo(List<SmartCardInfoDto> smartcardinfo) {
		this.smartcardinfo = smartcardinfo;
	}

}
