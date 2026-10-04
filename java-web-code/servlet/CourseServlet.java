package com.college.servlet;

import com.college.dao.CourseDAO;
import com.college.model.Course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/courses")
public class CourseServlet extends HttpServlet {

    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Course> courses = courseDAO.getAllCourses();
            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/courses.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error fetching courses", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String courseCode = request.getParameter("courseCode");
        String courseName = request.getParameter("courseName");
        String creditsStr = request.getParameter("credits");
        String instructor = request.getParameter("instructor");

        int credits = 0;
        if (creditsStr != null && !creditsStr.trim().isEmpty()) {
            try {
                credits = Integer.parseInt(creditsStr.trim());
            } catch (NumberFormatException e) {
                throw new ServletException("Invalid number for credits: " + creditsStr, e);
            }
        }

        try {
            Course course = new Course(courseCode, courseName, credits, instructor);
            courseDAO.addCourse(course);
        } catch (SQLException e) {
            throw new ServletException("Error saving course", e);
        }

        response.sendRedirect(request.getContextPath() + "/courses");
    }
}

