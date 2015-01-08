package com.cytx.timecard.dto;

/**
 * 创建时间：2014年7月30日 下午2:29:17 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：SmartCardInfoDto.java 类说明： 智能卡信息
 */
public class SmartCardInfoDto {

	private String studentid;
	private String cardid;

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

}
