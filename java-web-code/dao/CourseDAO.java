package com.college.dao;

import com.college.model.Course;
import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public List<Course> getAllCourses() throws SQLException {
        List<Course> list = new ArrayList<>();
        String query = "SELECT c.course_id, c.course_code, c.course_name, c.credits, COALESCE(i.instructor_name, 'TBD') AS instructor " +
                       "FROM courses c LEFT JOIN instructors i ON c.instructor_id = i.instructor_id ORDER BY c.course_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getString("instructor")
                );
                list.add(course);
            }
        }
        return list;
    }

    public void addCourse(Course course) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            Integer instructorId = null;
            String instructorName = course.getInstructor();

            if (instructorName != null && !instructorName.trim().isEmpty()) {
                String findInstSql = "SELECT instructor_id FROM instructors WHERE instructor_name = ?";
                try (PreparedStatement psFind = conn.prepareStatement(findInstSql)) {
                    psFind.setString(1, instructorName.trim());
                    try (ResultSet rs = psFind.executeQuery()) {
                        if (rs.next()) {
                            instructorId = rs.getInt("instructor_id");
                        }
                    }
                }

                if (instructorId == null) {
                    String insertInstSql = "INSERT INTO instructors (instructor_name, email) VALUES (?, ?)";
                    try (PreparedStatement psIns = conn.prepareStatement(insertInstSql, Statement.RETURN_GENERATED_KEYS)) {
                        psIns.setString(1, instructorName.trim());
                        String email = instructorName.trim().toLowerCase().replaceAll("[^a-z0-9]", "") + "@college.edu";
                        psIns.setString(2, email);
                        psIns.executeUpdate();
                        try (ResultSet rs = psIns.getGeneratedKeys()) {
                            if (rs.next()) {
                                instructorId = rs.getInt(1);
                            }
                        }
                    }
                }
            }

            String query = "INSERT INTO courses (course_code, course_name, credits, instructor_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, course.getCourseCode());
                ps.setString(2, course.getCourseName());
                ps.setInt(3, course.getCredits());
                if (instructorId != null) {
                    ps.setInt(4, instructorId);
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                ps.executeUpdate();
            }
        }
    }
}


