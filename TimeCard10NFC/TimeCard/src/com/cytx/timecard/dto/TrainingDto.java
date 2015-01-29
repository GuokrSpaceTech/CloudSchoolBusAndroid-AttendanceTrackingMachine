package com.cytx.timecard.dto;

import java.util.List;

/**
 * Created by caobb on 2015/1/30.
 */
public class TrainingDto {
    private String studentid;
    private List<LessionDto> lesson;

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public List<LessionDto> getLesson() {
        return lesson;
    }

    public void setLesson(List<LessionDto> lesson) {
        this.lesson = lesson;
    }
}
