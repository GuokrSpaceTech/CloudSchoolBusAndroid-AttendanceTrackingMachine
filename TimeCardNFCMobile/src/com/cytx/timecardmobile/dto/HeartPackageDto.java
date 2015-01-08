package com.cytx.timecardmobile.dto;

import java.util.List;

/**
 * 创建时间：2014年7月30日 下午2:45:13 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：HeartPackageDtp.java 类说明： 心跳包信息
 */
public class HeartPackageDto {
	private List<String> outstudent;
	private List<String> outteacher;
	private List<AddStudentDto> addstudent;
	private List<AddTeacherDto> addteacher;
	private List<CardDto> card;
	private List<ModifyStudentDto> modifystudent;
	private List<AddTeacherDto> modifyteacher;
	
	private List<RecieverAddDto> receiver;

	public List<String> getOutstudent() {
		return outstudent;
	}

	public void setOutstudent(List<String> outstudent) {
		this.outstudent = outstudent;
	}

	public List<AddStudentDto> getAddstudent() {
		return addstudent;
	}

	public void setAddstudent(List<AddStudentDto> addstudent) {
		this.addstudent = addstudent;
	}

	public List<CardDto> getCard() {
		return card;
	}

	public List<ModifyStudentDto> getModifystudent() {
		return modifystudent;
	}
	
	public void setModifystudent(List<ModifyStudentDto> modifystudent) {
		this.modifystudent = modifystudent;
	}
	
	public void setCard(List<CardDto> card) {
		this.card = card;
	}

	public List<RecieverAddDto> getReceiver() {
		return receiver;
	}

	public void setReceiver(List<RecieverAddDto> receiver) {
		this.receiver = receiver;
	}

	public List<String> getOutteacher() {
		return outteacher;
	}

	public void setOutteacher(List<String> outteacher) {
		this.outteacher = outteacher;
	}

	public List<AddTeacherDto> getAddteacher() {
		return addteacher;
	}

	public void setAddteacher(List<AddTeacherDto> addteacher) {
		this.addteacher = addteacher;
	}

	public List<AddTeacherDto> getModifyteacher() {
		return modifyteacher;
	}

	public void setModifyteacher(List<AddTeacherDto> modifyteacher) {
		this.modifyteacher = modifyteacher;
	}


}
