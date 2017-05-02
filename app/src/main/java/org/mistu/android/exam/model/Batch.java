package org.mistu.android.exam.model;


import org.mistu.android.exam.util.PrefUtil;
import org.mistu.android.exam.util.TimeUtil;

import java.util.List;

/**
 * Created by kedee on 28/4/17.
 */

public class Batch {
    private String name;
    private String std;
    private String subject;
    private String teacherName;
    private List<BatchTime> batchTimeList;
    private String startDate;
    private String endDate;
    private Status status;

    public Batch() {

    }

    public Batch(String name, String std, String subject, String teacherName, List<BatchTime> batchTimeList) {
        this.name = name;
        this.std = std;
        this.subject = subject;
        this.teacherName = teacherName;
        this.batchTimeList = batchTimeList;
        this.startDate = TimeUtil.getCurrentDate();
    }

    public Batch(String name, String std, String subject, List<BatchTime> batchTimeList) {
        this.name = name;
        this.std = std;
        this.subject = subject;
        this.teacherName = PrefUtil.getTeacherName();
        this.batchTimeList = batchTimeList;
        this.startDate = TimeUtil.getCurrentDate();
        this.status = Status.ACTIVE;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<BatchTime> getBatchTimeList() {
        return batchTimeList;
    }

    public void setBatchTimeList(List<BatchTime> batchTimeList) {
        this.batchTimeList = batchTimeList;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "name='" + name + '\'' +
                ", std='" + std + '\'' +
                ", subject='" + subject + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", batchTimeList=" + batchTimeList +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", status=" + status +
                '}';
    }
}
