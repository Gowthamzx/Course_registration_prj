# Combined Project — Two Web Layers, One MySQL Database

Both of these connect to the SAME MySQL database (`course_registration_prj`)
that you already created in MySQL Workbench. They are two DIFFERENT, SEPARATE
ways to build the web layer — you run ONE of them at a time, not both together.

## java-web-code/  (Java + JSP + Servlets, deployed on Tomcat)
- Needs: JDK, Maven, Apache Tomcat 11
- Edit DBConnection.java (in the full project) with your MySQL password
- Build: mvn clean package → deploy .war to Tomcat → http://localhost:8080/

## flask-web-code/  (Python + Flask, no Tomcat needed)
- Needs: Python, pip install -r requirements.txt
- Edit app.py DB_CONFIG with your MySQL password
- Run: python app.py → http://localhost:5000/

## Why they can't run at the same time
Both try to talk to the same `students`, `courses`, `registrations` tables —
that's fine, they can share the data. But don't run them on the same port,
and pick ONE stack (Java or Flask) to actually build your project with,
since mixing servlets and Flask routes in one running app isn't how either
framework works. Use this folder to compare the two approaches side by side.
