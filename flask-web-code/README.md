# Course Registration — Flask + MySQL

Same database (`course_registration_prj`) you already created in MySQL Workbench.
This just replaces the Java/Tomcat web layer with Python/Flask.

## Setup

1. Make sure `course_registration_prj` already exists in MySQL (run `database.sql`
   in Workbench if you haven't already — safe to re-run, uses IF NOT EXISTS).

2. Install Python packages:
   ```
   pip install -r requirements.txt
   ```

3. Open `app.py` and set your real MySQL password:
   ```python
   DB_CONFIG = {
       "host": "localhost",
       "port": 3306,
       "user": "root",
       "password": "YOUR_MYSQL_PASSWORD",
       "database": "course_registration_prj",
   }
   ```

4. Run the app:
   ```
   python app.py
   ```

5. Open in browser:
   ```
   http://localhost:5000/
   ```

## Structure
```
flask-course-registration/
├── app.py                  # Flask routes (replaces Servlets)
├── requirements.txt
├── database.sql
├── templates/               # Jinja2 templates (replaces JSP)
│   ├── index.html
│   ├── students.html
│   ├── courses.html
│   ├── register.html
│   └── registrations.html
└── static/css/style.css
```

## How it connects
- `app.py` uses `mysql-connector-python` to open a connection and run SQL directly
  (no separate DAO/model classes — Flask keeps this in the route functions).
- Each route (`/students`, `/courses`, `/register`, `/registrations`) does a
  `SELECT` or `INSERT` against MySQL and renders the matching Jinja2 template.
- No Tomcat needed — Flask's built-in dev server runs the whole app.
