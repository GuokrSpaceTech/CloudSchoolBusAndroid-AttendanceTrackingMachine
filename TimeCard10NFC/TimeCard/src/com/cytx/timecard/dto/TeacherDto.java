package com.cytx.timecard.dto;

import java.util.List;

public class TeacherDto {
	private String teacherid;
	private String teachername;
	private String avatar;
	private String classname;
	private List<SmartCardInfoDto>smartcardinfo;
	public String getTeacherid() {
		return teacherid;
	}
	public void setTeacherid(String teacherid) {
		this.teacherid = teacherid;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getTeachername() {
		return teachername;
	}
	public void setTeachername(String teachername) {
		this.teachername = teachername;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public List<SmartCardInfoDto> getSmartcardinfo() {
		return smartcardinfo;
	}
	public void setSmartcardinfo(List<SmartCardInfoDto> smartcardinfo) {
		this.smartcardinfo = smartcardinfo;
	}
	
}
