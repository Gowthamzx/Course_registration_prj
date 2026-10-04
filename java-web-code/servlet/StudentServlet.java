package com.college.servlet;

import com.college.dao.StudentDAO;
import com.college.model.Student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Student> students = studentDAO.getAllStudents();
            request.setAttribute("students", students);
            request.getRequestDispatcher("/students.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error fetching students", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String department = request.getParameter("department");

        try {
            Student student = new Student(name, email, department);
            studentDAO.addStudent(student);
        } catch (SQLException e) {
            throw new ServletException("Error saving student", e);
        }

        response.sendRedirect(request.getContextPath() + "/students");
    }
}

