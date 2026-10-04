from flask import Flask, render_template, request, redirect, url_for, flash
import mysql.connector

app = Flask(__name__)
app.secret_key = "course_registration_super_secret_key"

# --- Database config ---
# Same database (course_registration_prj) you already created in MySQL Workbench.
# Just change PASSWORD to your actual MySQL root password.
DB_CONFIG = {
    "host": "localhost",
    "port": 3306,
    "user": "root",
    "password": "mysql123",
    "database": "course_registration_prj",
}


def get_connection():
    return mysql.connector.connect(**DB_CONFIG)


# ---------- Home ----------
@app.route("/")
def index():
    stats = {"students": 0, "courses": 0, "registrations": 0}
    recent_registrations = []
    try:
        conn = get_connection()
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT COUNT(*) AS count FROM students")
        row = cursor.fetchone()
        if row:
            stats["students"] = row["count"]

        cursor.execute("SELECT COUNT(*) AS count FROM courses")
        row = cursor.fetchone()
        if row:
            stats["courses"] = row["count"]

        cursor.execute("SELECT COUNT(*) AS count FROM registrations")
        row = cursor.fetchone()
        if row:
            stats["registrations"] = row["count"]

        cursor.execute("SHOW COLUMNS FROM students")
        cols = [c["Field"] for c in cursor.fetchall()]
        name_col = "s.student_name" if "student_name" in cols else "s.name"

        query = f"""
            SELECT r.registration_id, {name_col} AS student_name, c.course_name, c.course_code
            FROM registrations r
            JOIN students s ON r.student_id = s.student_id
            JOIN courses c ON r.course_id = c.course_id
            ORDER BY r.registration_id DESC
            LIMIT 5
        """
        cursor.execute(query)
        recent_registrations = cursor.fetchall()
        cursor.close()
        conn.close()
    except Exception as e:
        print(f"Error fetching stats: {e}")

    return render_template("index.html", stats=stats, recent=recent_registrations)


# ---------- Students ----------
@app.route("/students", methods=["GET", "POST"])
def students():
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)

    if request.method == "POST":
        name = request.form["name"]
        email = request.form["email"]
        department = request.form["department"]

        cursor.execute("SHOW COLUMNS FROM students")
        cols = [c["Field"] for c in cursor.fetchall()]
        name_col = "student_name" if "student_name" in cols else "name"

        if "department_id" in cols:
            cursor.execute("SELECT department_id FROM departments WHERE department_name = %s", (department,))
            dep_row = cursor.fetchone()
            if dep_row:
                dep_id = dep_row["department_id"]
            else:
                cursor.execute("INSERT INTO departments (department_name) VALUES (%s)", (department,))
                dep_id = cursor.lastrowid

            cursor.execute(
                f"INSERT INTO students ({name_col}, email, department_id) VALUES (%s, %s, %s)",
                (name, email, dep_id),
            )
        else:
            cursor.execute(
                f"INSERT INTO students ({name_col}, email, department) VALUES (%s, %s, %s)",
                (name, email, department),
            )
        conn.commit()
        cursor.close()
        conn.close()
        flash(f"Student '{name}' added successfully!", "success")
        return redirect(url_for("students"))

    cursor.execute("SHOW TABLES LIKE 'departments'")
    has_dept_table = bool(cursor.fetchall())

    cursor.execute("SHOW COLUMNS FROM students")
    cols = [c["Field"] for c in cursor.fetchall()]
    name_expr = "s.student_name AS name" if "student_name" in cols else "s.name AS name"

    if "department_id" in cols and has_dept_table:
        query = f"""
            SELECT s.student_id, {name_expr}, s.email, COALESCE(d.department_name, 'General') AS department
            FROM students s
            LEFT JOIN departments d ON s.department_id = d.department_id
            ORDER BY s.student_id
        """
    elif "department" in cols:
        query = f"SELECT s.student_id, {name_expr}, s.email, s.department FROM students s ORDER BY s.student_id"
    else:
        query = f"SELECT s.student_id, {name_expr}, s.email, '' AS department FROM students s ORDER BY s.student_id"

    cursor.execute(query)
    all_students = cursor.fetchall()
    cursor.close()
    conn.close()
    return render_template("students.html", students=all_students)


# ---------- Courses ----------
@app.route("/courses", methods=["GET", "POST"])
def courses():
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)

    if request.method == "POST":
        course_code = request.form["course_code"]
        course_name = request.form["course_name"]
        credits = request.form["credits"]
        instructor = request.form["instructor"]

        cursor.execute("SHOW COLUMNS FROM courses")
        cols = [c["Field"] for c in cursor.fetchall()]

        if "instructor_id" in cols:
            cursor.execute("SHOW TABLES LIKE 'instructors'")
            if cursor.fetchall():
                cursor.execute("SELECT instructor_id FROM instructors WHERE instructor_name = %s", (instructor,))
                inst_row = cursor.fetchone()
                if inst_row:
                    inst_id = inst_row["instructor_id"]
                else:
                    cursor.execute("INSERT INTO instructors (instructor_name) VALUES (%s)", (instructor,))
                    inst_id = cursor.lastrowid

                cursor.execute(
                    "INSERT INTO courses (course_code, course_name, credits, instructor_id) VALUES (%s, %s, %s, %s)",
                    (course_code, course_name, credits, inst_id),
                )
            else:
                cursor.execute(
                    "INSERT INTO courses (course_code, course_name, credits) VALUES (%s, %s, %s)",
                    (course_code, course_name, credits),
                )
        else:
            cursor.execute(
                "INSERT INTO courses (course_code, course_name, credits, instructor) VALUES (%s, %s, %s, %s)",
                (course_code, course_name, credits, instructor),
            )
        conn.commit()
        cursor.close()
        conn.close()
        flash(f"Course '{course_code} - {course_name}' created successfully!", "success")
        return redirect(url_for("courses"))

    cursor.execute("SHOW COLUMNS FROM courses")
    cols = [c["Field"] for c in cursor.fetchall()]

    if "instructor_id" in cols:
        query = """
            SELECT c.course_id, c.course_code, c.course_name, c.credits, COALESCE(i.instructor_name, 'TBD') AS instructor
            FROM courses c
            LEFT JOIN instructors i ON c.instructor_id = i.instructor_id
            ORDER BY c.course_id
        """
    elif "instructor" in cols:
        query = "SELECT course_id, course_code, course_name, credits, instructor FROM courses ORDER BY course_id"
    else:
        query = "SELECT course_id, course_code, course_name, credits, '' AS instructor FROM courses ORDER BY course_id"

    cursor.execute(query)
    all_courses = cursor.fetchall()
    cursor.close()
    conn.close()
    return render_template("courses.html", courses=all_courses)


# ---------- Register ----------
@app.route("/register", methods=["GET", "POST"])
def register():
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)

    if request.method == "POST":
        name = request.form.get("name", "").strip()
        email = request.form.get("email", "").strip()
        department = request.form.get("department", "").strip()
        course_id = request.form.get("course_id")

        if not name or not email or not course_id:
            flash("Please fill out student name, email, and select a course.", "danger")
            cursor.close()
            conn.close()
            return redirect(url_for("register"))

        cursor.execute("SHOW COLUMNS FROM students")
        student_cols = [c["Field"] for c in cursor.fetchall()]
        name_col = "student_name" if "student_name" in student_cols else "name"

        # Check if student exists by email
        cursor.execute("SELECT student_id FROM students WHERE email = %s", (email,))
        existing_student = cursor.fetchone()

        if existing_student:
            student_id = existing_student["student_id"]
        else:
            # Add new student
            if "department_id" in student_cols:
                cursor.execute("SHOW TABLES LIKE 'departments'")
                if cursor.fetchall():
                    cursor.execute("SELECT department_id FROM departments WHERE department_name = %s", (department,))
                    dep_row = cursor.fetchone()
                    if dep_row:
                        dep_id = dep_row["department_id"]
                    else:
                        cursor.execute("INSERT INTO departments (department_name) VALUES (%s)", (department or "General",))
                        dep_id = cursor.lastrowid
                    cursor.execute(
                        f"INSERT INTO students ({name_col}, email, department_id) VALUES (%s, %s, %s)",
                        (name, email, dep_id),
                    )
                else:
                    cursor.execute(
                        f"INSERT INTO students ({name_col}, email) VALUES (%s, %s)",
                        (name, email),
                    )
            elif "department" in student_cols:
                cursor.execute(
                    f"INSERT INTO students ({name_col}, email, department) VALUES (%s, %s, %s)",
                    (name, email, department),
                )
            else:
                cursor.execute(
                    f"INSERT INTO students ({name_col}, email) VALUES (%s, %s)",
                    (name, email),
                )
            student_id = cursor.lastrowid

        # Check for duplicate registration
        cursor.execute(
            "SELECT registration_id FROM registrations WHERE student_id = %s AND course_id = %s",
            (student_id, course_id),
        )
        existing_reg = cursor.fetchone()
        if existing_reg:
            cursor.close()
            conn.close()
            flash(f"Student '{name}' ({email}) is already registered for this course.", "warning")
            return redirect(url_for("registrations"))

        cursor.execute("SHOW COLUMNS FROM registrations")
        reg_cols = [c["Field"] for c in cursor.fetchall()]

        if "registration_date" in reg_cols:
            cursor.execute(
                "INSERT INTO registrations (student_id, course_id, registration_date, status) "
                "VALUES (%s, %s, CURDATE(), 'Registered')",
                (student_id, course_id),
            )
        else:
            cursor.execute(
                "INSERT INTO registrations (student_id, course_id) VALUES (%s, %s)",
                (student_id, course_id),
            )
        conn.commit()
        cursor.close()
        conn.close()
        flash(f"Student '{name}' registered successfully!", "success")
        return redirect(url_for("registrations"))

    cursor.execute("SELECT course_id, course_code, course_name FROM courses ORDER BY course_id")
    all_courses = cursor.fetchall()
    cursor.close()
    conn.close()
    return render_template("register.html", courses=all_courses)


# ---------- Registrations ----------
@app.route("/registrations")
def registrations():
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)

    cursor.execute("SHOW COLUMNS FROM students")
    cols = [c["Field"] for c in cursor.fetchall()]
    name_col = "s.student_name" if "student_name" in cols else "s.name"

    query = f"""
        SELECT r.registration_id, {name_col} AS student_name, s.email, c.course_name, c.course_code
        FROM registrations r
        JOIN students s ON r.student_id = s.student_id
        JOIN courses c ON r.course_id = c.course_id
        ORDER BY r.registration_id DESC
    """
    cursor.execute(query)
    all_registrations = cursor.fetchall()
    cursor.close()
    conn.close()
    return render_template("registrations.html", registrations=all_registrations)


# ---------- Delete Actions / Customizations ----------
@app.route("/registrations/delete/<int:reg_id>", methods=["POST"])
def delete_registration(reg_id):
    try:
        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute("SHOW TABLES LIKE 'grades'")
        if cursor.fetchall():
            cursor.execute("DELETE FROM grades WHERE registration_id = %s", (reg_id,))
        cursor.execute("DELETE FROM registrations WHERE registration_id = %s", (reg_id,))
        conn.commit()
        cursor.close()
        conn.close()
        flash(f"Registration #{reg_id} removed successfully.", "success")
    except Exception as e:
        flash(f"Error removing registration: {e}", "danger")
    return redirect(url_for("registrations"))


@app.route("/students/delete/<int:student_id>", methods=["POST"])
def delete_student(student_id):
    try:
        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute("SHOW TABLES LIKE 'grades'")
        if cursor.fetchall():
            cursor.execute(
                "DELETE FROM grades WHERE registration_id IN (SELECT registration_id FROM registrations WHERE student_id = %s)",
                (student_id,),
            )
        cursor.execute("DELETE FROM registrations WHERE student_id = %s", (student_id,))
        cursor.execute("DELETE FROM students WHERE student_id = %s", (student_id,))
        conn.commit()
        cursor.close()
        conn.close()
        flash(f"Student #{student_id} and related enrollments removed successfully.", "success")
    except Exception as e:
        flash(f"Error removing student: {e}", "danger")
    return redirect(url_for("students"))


@app.route("/courses/delete/<int:course_id>", methods=["POST"])
def delete_course(course_id):
    try:
        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute("SHOW TABLES LIKE 'grades'")
        if cursor.fetchall():
            cursor.execute(
                "DELETE FROM grades WHERE registration_id IN (SELECT registration_id FROM registrations WHERE course_id = %s)",
                (course_id,),
            )
        cursor.execute("DELETE FROM registrations WHERE course_id = %s", (course_id,))
        cursor.execute("SHOW TABLES LIKE 'course_schedule'")
        if cursor.fetchall():
            cursor.execute("DELETE FROM course_schedule WHERE course_id = %s", (course_id,))
        cursor.execute("DELETE FROM courses WHERE course_id = %s", (course_id,))
        conn.commit()
        cursor.close()
        conn.close()
        flash(f"Course #{course_id} and associated enrollments removed successfully.", "success")
    except Exception as e:
        flash(f"Error removing course: {e}", "danger")
    return redirect(url_for("courses"))


if __name__ == "__main__":
    app.run(debug=True, port=5000)
