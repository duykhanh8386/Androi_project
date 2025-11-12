package com.example.studymate.data.model.response;

import java.time.LocalDate;

public class ClassDetailResponse {
    private int classId;
    private String className;
    private String classJoinCode;
    private String classTime;
    private String teacherName;
    private int studentCount;

    public ClassDetailResponse() {
    }

    public ClassDetailResponse(int classId, String className, String classJoinCode, String classTime, String teacherName, int studentCount) {
        this.classId = classId;
        this.className = className;
        this.classJoinCode = classJoinCode;
        this.classTime = classTime;
        this.teacherName = teacherName;
        this.studentCount = studentCount;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassJoinCode() {
        return classJoinCode;
    }

    public void setClassJoinCode(String classJoinCode) {
        this.classJoinCode = classJoinCode;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }
}
