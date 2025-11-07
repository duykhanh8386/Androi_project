package com.example.studymate.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "grades")
public class Grade {
    @PrimaryKey(autoGenerate = true)
    private int gradeID;
    private int studentID;
    private int classID;
    private String gradeType; // enum
    private double score;
    private String note;
    private Date updatedAt;

    public Grade(int gradeID, int studentID, int classID, String gradeType, double score, String note, Date updatedAt) {
        this.gradeID = gradeID;
        this.studentID = studentID;
        this.classID = classID;
        this.gradeType = gradeType;
        this.score = score;
        this.note = note;
        this.updatedAt = updatedAt;
    }

    public int getGradeID() { return gradeID; }
    public void setGradeID(int gradeID) { this.gradeID = gradeID; }
    public int getStudentID() { return studentID; }
    public void setStudentID(int studentID) { this.studentID = studentID; }
    public int getClassID() { return classID; }
    public void setClassID(int classID) { this.classID = classID; }
    public String getGradeType() { return gradeType; }
    public void setGradeType(String gradeType) { this.gradeType = gradeType; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
