package com.cytx.timecard.dto;

/**
 * Created by wpfcool2008 on 15-1-4.
 */
public class HealthReminder {
    private String healthString;
    public boolean isSelected = false;

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
}
