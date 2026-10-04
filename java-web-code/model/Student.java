package com.college.model;

public class Student {
    private int studentId;
    private String name;
    private String email;
    private String department;

    public Student() {}

    public Student(String name, String email, String department) {
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public Student(int studentId, String name, String email, String department) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
