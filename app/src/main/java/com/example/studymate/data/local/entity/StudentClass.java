package com.example.studymate.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "student_classes")
public class StudentClass {
    @PrimaryKey(autoGenerate = true)
    private int id; // Room cần primary key
    private int studentID;
    private int classID;
    private Date joinedAt;
    private Date leftAt;

    public StudentClass(int id, int studentID, int classID, Date joinedAt, Date leftAt) {
        this.id = id;
        this.studentID = studentID;
        this.classID = classID;
        this.joinedAt = joinedAt;
        this.leftAt = leftAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStudentID() { return studentID; }
    public void setStudentID(int studentID) { this.studentID = studentID; }
    public int getClassID() { return classID; }
    public void setClassID(int classID) { this.classID = classID; }
    public Date getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Date joinedAt) { this.joinedAt = joinedAt; }
    public Date getLeftAt() { return leftAt; }
    public void setLeftAt(Date leftAt) { this.leftAt = leftAt; }
}
