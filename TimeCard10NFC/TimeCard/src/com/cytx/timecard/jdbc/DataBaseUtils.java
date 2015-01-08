package com.cytx.timecard.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cytx.timecard.constants.Constants;
import com.cytx.timecard.dto.AddStudentDto;
import com.cytx.timecard.dto.AllStudentInfoDto;
import com.cytx.timecard.dto.AddTeacherDto;
import com.cytx.timecard.dto.CardDto;
import com.cytx.timecard.dto.ClassInfoDto;
import com.cytx.timecard.dto.HeartPackageDto;
import com.cytx.timecard.dto.ModifyStudentDto;
import com.cytx.timecard.dto.RecieverAddDto;
import com.cytx.timecard.dto.RecieverDto;
import com.cytx.timecard.dto.SmartCardInfoDto;
import com.cytx.timecard.dto.StudentDto;
import com.cytx.timecard.dto.TeacherDto;
import com.cytx.timecard.utility.DataCacheTools;
import com.cytx.timecard.utility.FileTools;

/**
* 创建时间：2014年8月9日 下午12:10:54 项目名称：TimeCard
*
* @author ben
* @version 1.0 文件名称：DataBaseUtils.java 类说明：
*/
public class DataBaseUtils {

//	// 首次创建本地数据库
//	public static void createLocalDataBase(List<StudentDto> studentList) {
//
//		if (studentList == null || studentList.size() == 0) {
//			return;
//		}
//
//		ClassDB classDB = new ClassDB();
//		ReceiverDB receiverDB = new ReceiverDB();
//		CardDB cardDB = new CardDB();
//		StudentDB studentDB = new StudentDB();
//		for (int i = 0; i < studentList.size(); i++) {
//			StudentDto studentDto = studentList.get(i);
//			// 学生基本信息
//			studentDB.addStudent(studentDto);
//			// 考勤卡信息
//			List<SmartCardInfoDto> smartCardInfoList = studentDto
//					.getSmartcardinfo();
//			if (smartCardInfoList != null && smartCardInfoList.size() != 0) {
//				for (int j = 0; j < smartCardInfoList.size(); j++) {
//					cardDB.addCard(smartCardInfoList.get(j));
//				}
//
//			}
//
//			// 接送者信息
//			List<RecieverDto> recieverList = studentDto.getReceiver();
//			if (recieverList != null && recieverList.size() != 0) {
//				for (int j = 0; j < recieverList.size(); j++) {
//					receiverDB.addReceiver(recieverList.get(j),
//							studentDto.getStudentid());
//				}
//			}
//
//			// 班级信息
//			List<ClassInfoDto> classInfoList = studentDto.getClassinfo();
//			if (classInfoList != null && classInfoList.size() != 0) {
//				for (int j = 0; j < classInfoList.size(); j++) {
//					classDB.addClass(classInfoList.get(j),
//							studentDto.getStudentid());
//				}
//			}
//		}
//
//	}
//
//	//ClearDB
//	public static void clearDB()
//	{
//		ClassDB classDB = new ClassDB();
//		ReceiverDB receiverDB = new ReceiverDB();
//		CardDB cardDB = new CardDB();
//		StudentDB studentDB = new StudentDB();
//
//		classDB.clearClass();
//		receiverDB.clearReceivers();
//		cardDB.clearCards();
//		studentDB.clearStudents();
//
//	}
//
//	// 通过心跳包操作本地数据库
//	public static void operationLocalDataBase(HeartPackageDto heartPackageDto) {
//		if (heartPackageDto == null) {
//			return;
//		}
//
//		ClassDB classDB = new ClassDB();
//		ReceiverDB receiverDB = new ReceiverDB();
//		CardDB cardDB = new CardDB();
//		StudentDB studentDB = new StudentDB();
//
//		// 删除学生信息
//		List<String> outStudentList = heartPackageDto.getOutstudent();
//		if (outStudentList != null && outStudentList.size() != 0) {
//			for (int i = 0; i < outStudentList.size(); i++) {
//				studentDB.deletStudentById(outStudentList.get(i));
//			}
//		}
//
//		// Update the student information
//		List<ModifyStudentDto> modifyStudentList = heartPackageDto.getModifystudent();
//
//		if (modifyStudentList != null && modifyStudentList.size() != 0) {
//			for (int i = 0; i < modifyStudentList.size(); i++) {
//				ModifyStudentDto modifyStudentDto = modifyStudentList.get(i);
//			    studentDB.updateStudentItem(modifyStudentDto);
//			    String stuId = modifyStudentDto.getStudentid();
//			    //Delete the cached portrait file
//			    String filename = Constants.STUDENT_PORTRAIT +"/" + stuId
//						+ "_" + studentDB.getStudentPidById(stuId) + ".jpg";
//			    FileTools.deleteFile(filename);
//			}
//		}
//
//		// 新增学生信息
//		List<AddStudentDto> addStudentList = heartPackageDto.getAddstudent();
//		if (addStudentList != null && addStudentList.size() != 0) {
//			for (int i = 0; i < addStudentList.size(); i++) {
//				AddStudentDto addStudentDto = addStudentList.get(i);
//				studentDB.addStudentItem(addStudentDto);
//				// 操作考勤卡信息
//				List<ClassInfoDto> classList = addStudentDto.getClassinfo();
//				if (classList != null && classList.size() != 0) {
//					for (int j = 0; j < classList.size(); j++) {
//						classDB.addClassItem(classList.get(j),
//								addStudentDto.getStudentid());
//					}
//				}
//			}
//		}
//
//		// 操作接送人信心
//		List<RecieverAddDto> recieverAddList = heartPackageDto.getReceiver();
//		if (recieverAddList != null && recieverAddList.size() != 0) {
//			for (int i = 0; i < recieverAddList.size(); i++) {
//				RecieverAddDto recieverAddDto = recieverAddList.get(i);
//				// 表示删除
//				if ("1".equals(recieverAddDto.getIsdelete())) {
//					receiverDB.deleteReceiverByFilepath(recieverAddDto.getFilepath());
//				}
//				// 表示新增
//				else if ("0".equals(recieverAddDto.getIsdelete())) {
//					Long uid = receiverDB.addReceiverItem(recieverAddDto);
//					//Make sure the DTO files get the unique id generated by the database
//					heartPackageDto.getReceiver().get(i).setId(uid.toString());
//				}
//			}
//		}
//
//		// 操作考勤卡信息
//		List<CardDto> cardList = heartPackageDto.getCard();
//		if (cardList != null && cardList.size() != 0) {
//			for (int i = 0; i < cardList.size(); i++) {
//				CardDto cardDto = cardList.get(i);
//				// 如果isdelete = 1，表示删除信息
//				if (cardDto.getIsdelete().equals("1")) {
//					cardDB.deleteCardById(cardDto.getCardid());
//				}
//				else
//				{
//					cardDB.addCard(card2smartCard(cardDto));
//				}
//			}
//		}
//
//	}


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

	/**
	 * RecieverAddDto 转换为RecieverDto
	 *
	 * @param recieverAddDto
	 * @return
	 */
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

	/**
	 * CardDto 转为 SmartCardInfoDto
	 *
	 * @param cardDto
	 * @return
	 */
	public static SmartCardInfoDto card2smartCard(CardDto cardDto) {
		SmartCardInfoDto smartCardInfoDto = new SmartCardInfoDto();
		if (cardDto == null) {
			return smartCardInfoDto;
		}

		smartCardInfoDto.setCardid(cardDto.getCardid());
		smartCardInfoDto.setStudentid(cardDto.getStudentid());
		return smartCardInfoDto;
	}
}
