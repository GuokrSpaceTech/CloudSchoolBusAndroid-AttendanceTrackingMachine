package com.cytx.timecardmobile.bean;


/**
 * 创建时间：2014年7月30日 下午3:16:59 项目名称：TimeCard
 * 
 * @author ben
 * @version 1.0 文件名称：TimeCardBean.java 类说明：考勤卡信息（访问服务器）
 */
public class TimeCardBean {

	private String smartid;
	private String machine;
	private float temperature;
	private String healthstate;
	private long createtime;
	private String fbody;
	private String fext;

	public String getSmartid() {
		return smartid;
	}

	public void setSmartid(String smartid) {
		this.smartid = smartid;
	}

	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public String getHealthstate() {
		return healthstate;
	}

	public void setHealthstate(String healthstate) {
		this.healthstate = healthstate;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public String getFbody() {
		return fbody;
	}

	public void setFbody(String fbody) {
		this.fbody = fbody;
	}

	public String getFext() {
		return fext;
	}

	public void setFext(String fext) {
		this.fext = fext;
	}
}
