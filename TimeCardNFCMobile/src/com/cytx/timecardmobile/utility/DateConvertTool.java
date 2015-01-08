package com.cytx.timecardmobile.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cytx.timecardmobile.constants.Constants;
import com.cytx.timecardmobile.dto.AddStudentDto;
import com.cytx.timecardmobile.dto.AddTeacherDto;
import com.cytx.timecardmobile.dto.CardDto;
import com.cytx.timecardmobile.dto.HeartPackageDto;
import com.cytx.timecardmobile.dto.ModifyStudentDto;
import com.cytx.timecardmobile.dto.RecieverAddDto;
import com.cytx.timecardmobile.dto.RecieverDto;
import com.cytx.timecardmobile.dto.SmartCardInfoDto;
import com.cytx.timecardmobile.dto.StudentDto;
import com.cytx.timecardmobile.dto.TeacherDto;

public class DateConvertTool {
	public static List<TeacherDto> updateTeachersInfoWithHeartPackage(Map<String, TeacherDto> teacherMap, HeartPackageDto heartPackageDto)
	{
		if(teacherMap==null)
		{
			teacherMap=new HashMap<String, TeacherDto>();
		}
		if(heartPackageDto==null)
		{
			return DataCacheTools.mapt2List(teacherMap);
		}
		
		List<String> teacherIdList = heartPackageDto.getOutteacher();
		for (int stu = 0; stu < teacherIdList.size(); stu++) {
			teacherMap.remove(teacherIdList.get(stu));
		}
		
	//	private List<AddTeacherDto> modifyteacher;
		// 进行修改教师信息操作
		List<AddTeacherDto> modifyTeacherList =  heartPackageDto.getModifyteacher();
		
		if (modifyTeacherList != null && modifyTeacherList.size() != 0) {
			for (int i = 0; i < modifyTeacherList.size(); i++) {
				AddTeacherDto modifyteacherDto = modifyTeacherList.get(i);
				//Modify this student, set Avtar at this moment
				TeacherDto theTeacher = teacherMap.get(modifyteacherDto.getTeacherid());
				theTeacher.setAvatar(modifyteacherDto.getAvatar());
				theTeacher.setTeachername(modifyteacherDto.getTeachername());
				theTeacher.setClassname(modifyteacherDto.getClassname())
;				//Delete the cached portrait file
				String filename = Constants.STUDENT_PORTRAIT +"/" + theTeacher.getTeacherid() 
									+ "_"  + ".jpg"; 
			    FileTools.deleteFile(filename); 

			}
		}
		
		List<AddTeacherDto> addTeacherDtos = heartPackageDto.getAddteacher();
		if (addTeacherDtos != null && addTeacherDtos.size() != 0) {
			for (int i = 0; i < addTeacherDtos.size(); i++) {
				AddTeacherDto addTeacherDto = addTeacherDtos.get(i);
				TeacherDto teacherDto = new TeacherDto();
				teacherDto.setTeacherid(addTeacherDto.getTeacherid());
				teacherDto.setTeachername(addTeacherDto.getTeachername());
				teacherDto.setClassname(addTeacherDto.getClassname());
				teacherDto.setAvatar(addTeacherDto.getAvatar());
				teacherMap.put(addTeacherDto.getTeacherid(), teacherDto);
			}
		}
		List<CardDto> cardDtos = heartPackageDto.getCard();
		if (cardDtos != null && cardDtos.size() != 0) {
			for (int i = 0; i < cardDtos.size(); i++) {
				CardDto cardDto = cardDtos.get(i);
				// 只有当存在对应的studentid时，才添加数据
				if (teacherMap.containsKey(cardDto.getStudentid())) {
					TeacherDto teacherDto = teacherMap.get(cardDto
							.getStudentid());
					teacherDto.getSmartcardinfo().add(card2smartCard(cardDto));
					teacherMap.put(cardDto.getStudentid(), teacherDto);
				}
			}

		}

		return DataCacheTools.mapt2List(teacherMap);
		//return null;
	}
	public static SmartCardInfoDto card2smartCard(CardDto cardDto) {
		SmartCardInfoDto smartCardInfoDto = new SmartCardInfoDto();
		if (cardDto == null) {
			return smartCardInfoDto;
		}

		smartCardInfoDto.setCardid(cardDto.getCardid());
		smartCardInfoDto.setStudentid(cardDto.getStudentid());
		return smartCardInfoDto;
	}
	/**
	 * 通过心跳包修改学生信息
	 * 
	 * @return
	 */
	public static List<StudentDto> updateStudentsInfoWithHeartPackage(

			Map<String, StudentDto> studentMap, HeartPackageDto heartPackageDto) {

		if (studentMap == null) {

			studentMap = new HashMap<String, StudentDto>();

		}

		// 没有数据包信息，则不做操作

		if (heartPackageDto == null) {

			return DataCacheTools.map2List(studentMap);

		}



		// 进行删除操作

		List<String> studentIdList = heartPackageDto.getOutstudent();

		for (int stu = 0; stu < studentIdList.size(); stu++) {

			studentMap.remove(studentIdList.get(stu));

		}



		// 进行增加学生信息操作

		List<ModifyStudentDto> modifyStudentList = heartPackageDto.getModifystudent();

		

		if (modifyStudentList != null && modifyStudentList.size() != 0) {

			for (int i = 0; i < modifyStudentList.size(); i++) {

				ModifyStudentDto modifyStudentDto = modifyStudentList.get(i);

				

				//Modify this student, set Avtar at this moment

				StudentDto theStudent = studentMap.get(modifyStudentDto.getStudentid());

				theStudent.setAvatar(modifyStudentDto.getAvatar());

				theStudent.setCnname(modifyStudentDto.getCnname());

				//Delete the cached portrait file

				String filename = Constants.STUDENT_PORTRAIT +"/" + theStudent.getStudentid() 

									+ "_" + theStudent.getPid() + ".jpg"; 

			    FileTools.deleteFile(filename); 



			}

		}

		List<AddStudentDto> addStudentDtos = heartPackageDto.getAddstudent();

		if (addStudentDtos != null && addStudentDtos.size() != 0) {

			for (int i = 0; i < addStudentDtos.size(); i++) {

				AddStudentDto addStudentDto = addStudentDtos.get(i);

				StudentDto studentDto = new StudentDto();

				studentDto.setCnname(addStudentDto.getCnname());

				studentDto.setStudentid(addStudentDto.getStudentid());

				studentDto.setAvatar(addStudentDto.getAvatar());

				studentDto.setPid(addStudentDto.getPid());

				studentDto.setClassinfo(addStudentDto.getClassinfo());

				studentDto.setSchoolname(addStudentDto.getSchoolname());

				studentMap.put(addStudentDto.getStudentid(), studentDto);

			}

		}



		// 增加/删除接送人信息

		List<RecieverAddDto> remoteReceiverList = heartPackageDto.getReceiver();

		

		if (remoteReceiverList != null && remoteReceiverList.size() != 0) {

		

			for (int i = 0; i < remoteReceiverList.size(); i++) {

				

				RecieverAddDto remoteReceiver = remoteReceiverList.get(i);

				

				StudentDto studentDto = studentMap.get(remoteReceiver

						.getStudentid());



				//Walk throught current Student's receiver list to find the one to delete

				if( remoteReceiver.getIsdelete().equals("1"))

				{

					for(int j=0; j<studentDto.getReceiver().size(); j++)

					{

						RecieverDto localReceiver = studentDto.getReceiver().get(j);

						if(localReceiver.getFilepath().equals(remoteReceiver.getFilepath()))

						{

						    studentDto.getReceiver().remove(localReceiver);

						    

						    break;

						}

					}

				}

				//Just add if the remote added

				else

				{

					studentDto.getReceiver().add(recieverAdd2receiver(remoteReceiver));

				}

				

				studentMap.put(studentDto.getStudentid(), studentDto);

			}

		}





		// 增加考勤卡信息

		List<CardDto> cardDtos = heartPackageDto.getCard();

		if (cardDtos != null && cardDtos.size() != 0) {

			for (int i = 0; i < cardDtos.size(); i++) {

				CardDto cardDto = cardDtos.get(i);

				// 只有当存在对应的studentid时，才添加数据

				if (studentMap.containsKey(cardDto.getStudentid())) {

					StudentDto studentDto = studentMap.get(cardDto

							.getStudentid());

					studentDto.getSmartcardinfo().add(card2smartCard(cardDto));

					studentMap.put(cardDto.getStudentid(), studentDto);

				}
			}
		}
		return DataCacheTools.map2List(studentMap);
	}

	public static RecieverDto recieverAdd2receiver(RecieverAddDto recieverAddDto) {
		RecieverDto recieverDto = new RecieverDto();
		if (recieverAddDto == null) {
			return recieverDto;
		}
		
		recieverDto.setId(recieverAddDto.getId());
		recieverDto.setFilepath(recieverAddDto.getFilepath());
		recieverDto.setPid(recieverAddDto.getPid());
		recieverDto.setRelationship(recieverAddDto.getRelationship());
		return recieverDto;
	}

}
