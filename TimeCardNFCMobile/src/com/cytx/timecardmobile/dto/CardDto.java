package com.cytx.timecardmobile.dto;

/**
 * 创建时间：2014年7月30日 下午3:01:09 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：CardDto.java 类说明： 卡信息
 */
public class CardDto {

	private String cardid;
	private String inuse;
	private String isdelete;
	private String studentid;

	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getInuse() {
		return inuse;
	}

	public void setInuse(String inuse) {
		this.inuse = inuse;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

}
