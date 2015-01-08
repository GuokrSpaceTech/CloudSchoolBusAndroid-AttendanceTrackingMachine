package com.cytx.timecard.dto;

/**
 * 创建时间：2014年7月30日 下午3:02:43 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：RecieverAddDto.java 类说明：新增学生的接送家长信息
 */
public class RecieverAddDto {

	private String id;
	private String pid;
	private String filepath;
	private String relationship;
	private String isdelete;
	private String studentid;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getIsdelete() {
		return isdelete;
	}

	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	

}	
