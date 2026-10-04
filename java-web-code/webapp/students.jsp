<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.college.model.Student" %>
<!DOCTYPE html>
<html>
<head>
    <title>Students</title>
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
        <h1>Students</h1>

        <form action="students" method="post">
            <label>Name</label>
            <input type="text" name="name" required>

            <label>Email</label>
            <input type="email" name="email" required>

            <label>Department</label>
            <input type="text" name="department" required>

            <button type="submit">Save Student</button>
        </form>

        <h2>All Students</h2>
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Department</th>
            </tr>
            <%
                List<Student> students = (List<Student>) request.getAttribute("students");
                if (students != null) {
                    for (Student s : students) {
            %>
            <tr>
                <td><%= s.getStudentId() %></td>
                <td><%= s.getName() %></td>
                <td><%= s.getEmail() %></td>
                <td><%= s.getDepartment() %></td>
            </tr>
            <%
                    }
                }
            %>
        </table>
    </div>
</body>
</html>
