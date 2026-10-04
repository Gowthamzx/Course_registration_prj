package com.college.model;

public class Registration {
    private int registrationId;
    private int studentId;
    private int courseId;
    private String studentName;
    private String courseName;

    public Registration() {}

    public Registration(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public Registration(int registrationId, String studentName, String courseName) {
        this.registrationId = registrationId;
        this.studentName = studentName;
        this.courseName = courseName;
    }

    public int getRegistrationId() { return registrationId; }
    public void setRegistrationId(int registrationId) { this.registrationId = registrationId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
}
