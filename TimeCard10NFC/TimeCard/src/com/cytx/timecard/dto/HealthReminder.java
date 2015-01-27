package com.cytx.timecard.dto;

/**
 * Created by wpfcool2008 on 15-1-4.
 */
public class HealthReminder {
    private String id;
    private String reminder;
    public boolean isSelected = false;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getReminder() {
        return reminder;
    }
    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
