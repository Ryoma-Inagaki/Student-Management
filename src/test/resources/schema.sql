CREATE TABLE students (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  kana_name VARCHAR(100) NOT NULL,
  nickname VARCHAR(100),
  email VARCHAR(255) NOT NULL UNIQUE,
  area VARCHAR(100),
  age INT,
  sex VARCHAR(50),
  remark TEXT,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE students_courses (
  id INT PRIMARY KEY AUTO_INCREMENT,
  student_id VARCHAR(36) NOT NULL,
  course_name VARCHAR(100) NOT NULL,
  start_at TIMESTAMP NOT NULL,
  end_at TIMESTAMP,
  FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

CREATE TABLE application_statuses (
  id VARCHAR(36) PRIMARY KEY,
  student_course_id INT NOT NULL,
  status VARCHAR(20) NOT NULL,
  status_id INT NOT NULL DEFAULT 1,
  FOREIGN KEY (student_course_id) REFERENCES students_courses(id) ON DELETE CASCADE
);