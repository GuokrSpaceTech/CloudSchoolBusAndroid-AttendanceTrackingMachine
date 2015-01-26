package com.cytx.timecard.dto;

/**
 * Created by wpfcool2008 on 15-1-4.
 */
public class HealthReminder {
    private int id;
    public boolean isSelected = false;
    private int timeTaken;
    private int timeTotal;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    private String healthString;

    public String getHealthString() {
        return healthString;
    }

    public void setHealthString(String healthString) {
        this.healthString = healthString;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getDetailedInfo() { return "教师：毛毛 状态：正常";
    }

    public int getTimeTaken() {
        return timeTaken+(isSelected?1:0);
    }

    public int getTimeTotal() {
        return timeTotal;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    public void setTimeTotal(int timeTotal) {
        this.timeTotal = timeTotal;
    }
}
