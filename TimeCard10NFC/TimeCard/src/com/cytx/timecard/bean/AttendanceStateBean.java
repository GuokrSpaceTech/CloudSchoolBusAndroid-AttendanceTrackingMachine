package com.cytx.timecard.bean;

public class AttendanceStateBean {
	private String  cardid;
	private String  mid;
	private int     healthState;
	private String  reminder;
	private long    createtime;
    private String healthReminder;
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public int getHealthState() {
		return healthState;
	}
	public void setHealthState(int healthState) {
		this.healthState = healthState;
	}
	public String getReminder() {
		return reminder;
	}
	public void setReminder(String reminder) {
		this.reminder = reminder;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

    public String getHealthReminder() {
        return healthReminder;
    }

    public void setHealthReminder(String healthReminder) {
        this.healthReminder = healthReminder;
    }
}
