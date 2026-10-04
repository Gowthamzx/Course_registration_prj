CREATE DATABASE IF NOT EXISTS course_registration_prj;
USE course_registration_prj;

CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(150) NOT NULL,
    credits INT NOT NULL,
    instructor VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS registrations (
    registration_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    UNIQUE KEY unique_registration (student_id, course_id)
);

INSERT INTO courses (course_code, course_name, credits, instructor) VALUES
('CS101', 'Object Oriented Programming', 4, 'Dr. Kumar'),
('CS102', 'Database Management Systems', 4, 'Dr. Priya'),
('CS103', 'Web Technologies', 3, 'Dr. Anand');
