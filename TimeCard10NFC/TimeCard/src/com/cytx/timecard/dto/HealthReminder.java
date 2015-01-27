package com.cytx.timecard.dto;

/**
 * Created by wpfcool2008 on 15-1-4.
 */
public class HealthReminder {
    private String reminder;
    public boolean isSelected = false;
    private int id;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getHealthString() {
        return reminder;
    }

    public void setHealthString(String healthString) {
        this.reminder = healthString;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
