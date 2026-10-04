package com.college.dao;

import com.college.model.Registration;
import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDAO {

    public List<Registration> getAllRegistrations() throws SQLException {
        List<Registration> list = new ArrayList<>();
        String query = "SELECT r.registration_id, s.student_name, c.course_name " +
                       "FROM registrations r " +
                       "JOIN students s ON r.student_id = s.student_id " +
                       "JOIN courses c ON r.course_id = c.course_id " +
                       "ORDER BY r.registration_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Registration reg = new Registration(
                        rs.getInt("registration_id"),
                        rs.getString("student_name"),
                        rs.getString("course_name")
                );
                list.add(reg);
            }
        }
        return list;
    }

    public void addRegistration(Registration reg) throws SQLException {
        String query = "INSERT INTO registrations (student_id, course_id, registration_date, status) VALUES (?, ?, CURDATE(), 'Registered')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reg.getStudentId());
            ps.setInt(2, reg.getCourseId());
            ps.executeUpdate();
        }
    }
}


