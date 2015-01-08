package com.cytx.timecard.dto;

/**
 * 创建时间：2014年7月30日 下午2:27:10 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：RecieverDto.java 类说明：接送家人信息
 */
public class RecieverDto {

	private String id;
	private String pid;
	private String filepath;
	private String relationship;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
