<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.college.model.Course" %>
<!DOCTYPE html>
<html>
<head>
    <title>Courses</title>
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
        <h1>Courses</h1>

        <form action="courses" method="post">
            <label>Course Code</label>
            <input type="text" name="courseCode" required>

            <label>Course Name</label>
            <input type="text" name="courseName" required>

            <label>Credits</label>
            <input type="number" name="credits" min="1" max="10" required>

            <label>Instructor</label>
            <input type="text" name="instructor" required>

            <button type="submit">Save Course</button>
        </form>

        <h2>All Courses</h2>
        <table>
            <tr>
                <th>ID</th>
                <th>Code</th>
                <th>Name</th>
                <th>Credits</th>
                <th>Instructor</th>
            </tr>
            <%
                List<Course> courses = (List<Course>) request.getAttribute("courses");
                if (courses != null) {
                    for (Course c : courses) {
            %>
            <tr>
                <td><%= c.getCourseId() %></td>
                <td><%= c.getCourseCode() %></td>
                <td><%= c.getCourseName() %></td>
                <td><%= c.getCredits() %></td>
                <td><%= c.getInstructor() %></td>
            </tr>
            <%
                    }
                }
            %>
        </table>
    </div>
</body>
</html>
