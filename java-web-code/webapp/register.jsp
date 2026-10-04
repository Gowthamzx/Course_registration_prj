<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.college.model.Student" %>
<%@ page import="com.college.model.Course" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
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
        <h1>Register Student for a Course</h1>

        <form action="register" method="post">
            <label>Student</label>
            <select name="studentId" required>
                <option value="">-- Select Student --</option>
                <%
                    List<Student> students = (List<Student>) request.getAttribute("students");
                    if (students != null) {
                        for (Student s : students) {
                %>
                <option value="<%= s.getStudentId() %>"><%= s.getName() %> (<%= s.getEmail() %>)</option>
                <%
                        }
                    }
                %>
            </select>

            <label>Course</label>
            <select name="courseId" required>
                <option value="">-- Select Course --</option>
                <%
                    List<Course> courses = (List<Course>) request.getAttribute("courses");
                    if (courses != null) {
                        for (Course c : courses) {
                %>
                <option value="<%= c.getCourseId() %>"><%= c.getCourseCode() %> — <%= c.getCourseName() %></option>
                <%
                        }
                    }
                %>
            </select>

            <button type="submit">Register Student</button>
        </form>
    </div>
</body>
</html>
