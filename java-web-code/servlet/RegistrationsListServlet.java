package com.college.servlet;

import com.college.dao.RegistrationDAO;
import com.college.model.Registration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/registrations")
public class RegistrationsListServlet extends HttpServlet {

    private final RegistrationDAO registrationDAO = new RegistrationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Registration> registrations = registrationDAO.getAllRegistrations();
            request.setAttribute("registrations", registrations);
            request.getRequestDispatcher("/registrations.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error fetching registrations", e);
        }
    }
}

