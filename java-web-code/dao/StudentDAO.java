package com.college.dao;

import com.college.model.Student;
import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String query = "SELECT s.student_id, s.student_name, s.email, COALESCE(d.department_name, 'General') AS department " +
                       "FROM students s LEFT JOIN departments d ON s.department_id = d.department_id ORDER BY s.student_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getString("email"),
                        rs.getString("department")
                );
                list.add(s);
            }
        }
        return list;
    }

    public void addStudent(Student student) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            Integer deptId = null;
            String deptName = student.getDepartment();

            if (deptName != null && !deptName.trim().isEmpty()) {
                String findDeptSql = "SELECT department_id FROM departments WHERE department_name = ?";
                try (PreparedStatement psFind = conn.prepareStatement(findDeptSql)) {
                    psFind.setString(1, deptName.trim());
                    try (ResultSet rs = psFind.executeQuery()) {
                        if (rs.next()) {
                            deptId = rs.getInt("department_id");
                        }
                    }
                }

                if (deptId == null) {
                    String insertDeptSql = "INSERT INTO departments (department_name) VALUES (?)";
                    try (PreparedStatement psIns = conn.prepareStatement(insertDeptSql, Statement.RETURN_GENERATED_KEYS)) {
                        psIns.setString(1, deptName.trim());
                        psIns.executeUpdate();
                        try (ResultSet rs = psIns.getGeneratedKeys()) {
                            if (rs.next()) {
                                deptId = rs.getInt(1);
                            }
                        }
                    }
                }
            }

            String query = "INSERT INTO students (student_name, email, department_id) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, student.getName());
                ps.setString(2, student.getEmail());
                if (deptId != null) {
                    ps.setInt(3, deptId);
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                ps.executeUpdate();
            }
        }
    }
}


