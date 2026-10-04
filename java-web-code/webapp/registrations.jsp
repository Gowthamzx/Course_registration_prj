<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.college.model.Registration" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registrations</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav>
        <a href="index.jsp">Home</a>
        <a href="students">Students</a>
        <a href="courses">Courses</a>
        <a href="register">Register</a>
        <a href="registrations">Registrations</a>
    </nav>
    <div class="container">
        <h1>Registrations</h1>
        <table>
            <tr>
                <th>Registration ID</th>
                <th>Student</th>
                <th>Course</th>
            </tr>
            <%
                List<Registration> registrations = (List<Registration>) request.getAttribute("registrations");
                if (registrations != null) {
                    for (Registration r : registrations) {
            %>
            <tr>
                <td><%= r.getRegistrationId() %></td>
                <td><%= r.getStudentName() %></td>
                <td><%= r.getCourseName() %></td>
            </tr>
            <%
                    }
                }
            %>
        </table>
    </div>
</body>
</html>
