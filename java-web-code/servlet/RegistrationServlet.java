package com.college.servlet;

import com.college.dao.CourseDAO;
import com.college.dao.RegistrationDAO;
import com.college.dao.StudentDAO;
import com.college.model.Registration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final RegistrationDAO registrationDAO = new RegistrationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("students", studentDAO.getAllStudents());
            request.setAttribute("courses", courseDAO.getAllCourses());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error loading registration form", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int courseId = Integer.parseInt(request.getParameter("courseId"));

        try {
            Registration registration = new Registration(studentId, courseId);
            registrationDAO.addRegistration(registration);
        } catch (SQLException e) {
            throw new ServletException("Error saving registration", e);
        }

        response.sendRedirect(request.getContextPath() + "/registrations");
    }
}

