# KEC Academic Performance Management System

[![Java Version](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/Apache%20Maven-3.9%2B-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.11-25A162?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![Status](https://img.shields.io/badge/Status-Completed-success?style=for-the-badge)]()

> **Kongu Engineering College (Autonomous), Perundurai, Erode**  
> **Course Assignment**: Java Programming & Academic Performance Management  
> **Author**: Veeravigneshwar K V  
> **Roll Number**: 25ITR168  
> **Department**: Information Technology  

---

## 📌 Executive Summary

The **KEC Academic Performance Management System** is a robust, enterprise-grade Java CLI application designed to automate, evaluate, and manage academic records in strict compliance with the **autonomous academic regulations of Kongu Engineering College (KEC)**.

Built on an extensible **Data Access Object (DAO)** layered architecture using **Java 17**, **JDBC**, and a relational **MySQL** database, the system enforces multi-tiered academic validation rules:
- Differentiated course handling: **THEORY**, **PRACTICAL**, and **EMBEDDED** courses.
- Strict attendance eligibility checks (**≥ 75%** required, **65%–74%** conditionally eligible upon medical condonation, **< 65%** debarred with `SA`).
- Multi-component Continuous Internal Assessment (CIA) and End Semester Examination (ESE) mark computations.
- KEC Autonomous 10-point scale Letter Grade calculation and Semester Grade Point Average (**SGPA**) aggregation.
- Real-time generation of student report cards and marksheet outputs.

---

## 🏗️ System Architecture

The application adopts the **DAO (Data Access Object)** pattern to maintain strict separation of concerns between business evaluation logic, user interaction, and persistent storage:

```
┌────────────────────────────────────────────────────────┐
│                   CLI Presentation                     │
│           (MainApp.java / Menu Controllers)            │
└───────────────────────────┬────────────────────────────┘
                            │
┌───────────────────────────▼────────────────────────────┐
│                    Service Layer                       │
│    GradeCalculator | SGPACalculator | FinalGradeService│
└───────────────────────────┬────────────────────────────┘
                            │
┌───────────────────────────▼────────────────────────────┐
│                      DAO Layer                         │
│  StudentDAO | CourseDAO | EnrollmentDAO | AttendanceDAO│
│  AssessmentDAO | GradeDAO | GradeResultDAO | Marksheet │
└───────────────────────────┬────────────────────────────┘
                            │ JDBC Connection Pool
┌───────────────────────────▼────────────────────────────┐
│                  MySQL Database Server                 │
│              Schema: kec_academics (Port 3306)         │
└────────────────────────────────────────────────────────┘
```

---

## ✨ Key Features & Modules

### 1. 👨‍🎓 Student Management
- **Add New Students**: Validates roll number, full name, department code, and semester (1–8).
- **View All Records**: Formatted tabular display of all registered students.
- **Search Student**: Real-time query by roll number.
- **Update Student**: Modify name, department, or current semester.
- **Delete Student**: Safe removal with explicit confirmation prompt.

### 2. 📚 Course Catalog Management
- **Course Types**:
  - `THEORY`: Standard lecture-based courses (ESE max: 60, min passing: 27).
  - `PRACTICAL`: Laboratory courses (ESE max: 40, min passing: 20).
  - `EMBEDDED`: Hybrid courses blending theory and lab exercises (ESE max: 50, min passing: 22.5).
- **Credit Allocation**: Configurable credit weights per course (1 to 5 credits) used in SGPA computations.
- **Search & Display**: Search courses by course code or list full catalog.

### 3. 📝 Enrollment System
- Enroll students into specific courses per semester.
- Links student roll numbers with course codes via foreign keys.
- Initializes attendance tracking records automatically upon enrollment.

### 4. 📅 Attendance & Medical Condonation Engine
- **Attendance Percentage Calculation**: $\text{Attendance \%} = \frac{\text{Attended Classes}}{\text{Total Classes}} \times 100$
- **Regulatory Rules**:
  - **$\ge 75\%$**: Fully eligible for End Semester Examination (ESE).
  - **$65\% \le \text{Attendance} < 75\%$**: Marked as **PENDING**; requires authorized medical condonation approval to permit exam appearance.
  - **$< 65\%$**: Debarred from exams; awarded `SA` (Shortage of Attendance) with 0 grade points.

### 5. 📊 Continuous Internal Assessment (CIA) & Evaluation
- Records individual assessment marks:
  - **CIA 1**, **CIA 2**, **CIA 3**
  - **Assignments**
  - **Quizzes**
  - **Lab Practical Scores**
  - **End Semester Exam (ESE)** with explicit absence tracking (`ese_absent`)
- Support for missing/optional components without breaking calculations.

### 6. ⚖️ Configurable Assessment Weightages
- Dynamic weightage configuration table in MySQL per course type.
- Ensures total component weights strictly sum to **100%**.

### 7. 🎓 KEC Autonomous Grading & SGPA Engine
- Calculates weighted aggregate score across all assessments.
- Checks minimum passing criteria:
  - Minimum ESE score attained for course type.
  - Total aggregate score $\ge 50\%$.
- Computes **SGPA** for any given semester:

$$\text{SGPA} = \frac{\sum_{i=1}^{n} (\text{Course Credits}_i \times \text{Grade Point}_i)}{\sum_{i=1}^{n} \text{Course Credits}_i}$$

### 8. 📜 Consolidated Marksheet Generation
- Detailed terminal marksheet displaying student demographics, course codes, internal marks, ESE marks, final aggregate, letter grade, result status, and SGPA summary.

---

## 📊 KEC Autonomous Grading Reference

| Marks Range (Aggregate) | Letter Grade | Grade Point | Performance Description | Passing Status |
|:---:|:---:|:---:|:---|:---:|
| **91 – 100** | **O** | **10** | Outstanding | PASS |
| **81 – 90** | **A+** | **9** | Excellent | PASS |
| **71 – 80** | **A** | **8** | Very Good | PASS |
| **61 – 70** | **B+** | **7** | Good | PASS |
| **50 – 60** | **B** | **6** | Average / Satisfactory | PASS |
| **< 50** | **RA** | **0** | Re-Appear / Failed | FAIL |
| **Attendance < 65%** | **SA** | **0** | Shortage of Attendance | DEBARRED |
| **Absent in ESE** | **AB** | **0** | Absent for Examination | FAIL |

### Course-Specific ESE Passing Criteria

| Course Type | Maximum ESE Marks | Minimum Passing ESE Marks | Minimum Aggregate Required |
|:---|:---:|:---:|:---:|
| **THEORY** | 60 | **27.0** (45%) | **50.0%** |
| **PRACTICAL** | 40 | **20.0** (50%) | **50.0%** |
| **EMBEDDED** | 50 | **22.5** (45%) | **50.0%** |

---

## 📸 System Screenshots & UI Walkthrough

All captured execution screenshots from the testing suite are available under the `OUTPUTS/` directory:

### 1. Main Navigation Menu
Interactive terminal dashboard offering streamlined access to all operational sub-systems:
<p align="center">
  <img src="OUTPUTS/MAIN%20MENU.png" width="750" alt="Main Navigation Menu" />
</p>

---

### 2. Student Management & Database Records
Adding students, validating inputs, and maintaining database integrity:
<p align="center">
  <img src="OUTPUTS/ADD%20STUDENT.png" width="750" alt="Add Student Screen" />
</p>
<p align="center">
  <img src="OUTPUTS/STUDENT%20DATABASE.png" width="750" alt="Student Database Table" />
</p>
<p align="center">
  <img src="OUTPUTS/STUDENT%20STRUCTURE.png" width="750" alt="Student Table Schema" />
</p>

---

### 3. Course Management & Schema Structure
Managing course catalogs, credit loads, and classifications:
<p align="center">
  <img src="OUTPUTS/COURSE%20MANAGEMENT.png" width="750" alt="Course Management Screen" />
</p>
<p align="center">
  <img src="OUTPUTS/COURSE%20DATABASE.png" width="750" alt="Course Database Records" />
</p>
<p align="center">
  <img src="OUTPUTS/COURSE%20STRUCTURE.png" width="750" alt="Course Table Schema" />
</p>

---

### 4. Enrollment & Attendance Verification
Tracking student-course links and evaluating regulatory attendance compliance:
<p align="center">
  <img src="OUTPUTS/ENROLLMENT%20MANAGEMENT.png" width="750" alt="Enrollment Management Screen" />
</p>
<p align="center">
  <img src="OUTPUTS/ENROLLEMNT%20DATABASE.png" width="750" alt="Enrollment Database Records" />
</p>
<p align="center">
  <img src="OUTPUTS/ATTANDENCE%20MANAGEMENT.png" width="750" alt="Attendance Management & Condonation" />
</p>

---

### 5. Assessment Marks & Marksheet Output
Detailed entry of CIA/ESE marks and comprehensive final marksheet generation:
<p align="center">
  <img src="OUTPUTS/ASSESSMENT%20MANAGEMENT.png" width="750" alt="Assessment Management" />
</p>
<p align="center">
  <img src="OUTPUTS/ASSESSMENT%20MARKS%20STRUCTURE.png" width="750" alt="Assessment Marks Table Schema" />
</p>
<p align="center">
  <img src="OUTPUTS/SUBJECT%20REPORT.png" width="750" alt="Subject Grade Report" />
</p>
<p align="center">
  <img src="OUTPUTS/MARKSHEET%20OF%20STUDENT.png" width="750" alt="Student Marksheet" />
</p>

---

## 🗄️ Database Schema & DDL

The database runs on **MySQL** under the catalog name `kec_academics`.

```sql
CREATE DATABASE IF NOT EXISTS kec_academics;
USE kec_academics;

-- 1. Student Master Table
CREATE TABLE IF NOT EXISTS students_record (
    roll_no VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    current_semester INT NOT NULL CHECK (current_semester BETWEEN 1 AND 8)
);

-- 2. Course Catalog Table
CREATE TABLE IF NOT EXISTS courses (
    course_code VARCHAR(20) PRIMARY KEY,
    course_title VARCHAR(150) NOT NULL,
    credits INT NOT NULL CHECK (credits > 0),
    course_type ENUM('THEORY', 'PRACTICAL', 'EMBEDDED') NOT NULL
);

-- 3. Course Enrollment Table
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_no VARCHAR(20) NOT NULL,
    course_code VARCHAR(20) NOT NULL,
    semester INT NOT NULL CHECK (semester BETWEEN 1 AND 8),
    total_classes INT DEFAULT 0,
    attended_classes INT DEFAULT 0,
    medical_condonation BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (roll_no) REFERENCES students_record(roll_no) ON DELETE CASCADE,
    FOREIGN KEY (course_code) REFERENCES courses(course_code) ON DELETE CASCADE,
    UNIQUE KEY uq_student_course (roll_no, course_code, semester)
);

-- 4. Assessment Marks Table
CREATE TABLE IF NOT EXISTS assessment_marks (
    enrollment_id INT PRIMARY KEY,
    cia1 DECIMAL(5, 2),
    cia2 DECIMAL(5, 2),
    cia3 DECIMAL(5, 2),
    assignment_marks DECIMAL(5, 2),
    quiz_marks DECIMAL(5, 2),
    lab_marks DECIMAL(5, 2),
    ese_marks DECIMAL(5, 2),
    ese_absent BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE
);

-- 5. Assessment Weightages Table
CREATE TABLE IF NOT EXISTS assessment_weightages (
    course_type VARCHAR(20) NOT NULL,
    component_name VARCHAR(50) NOT NULL,
    component_weight DECIMAL(5, 2) NOT NULL,
    PRIMARY KEY (course_type, component_name)
);

-- 6. Final Calculated Grades Table
CREATE TABLE IF NOT EXISTS final_grades (
    enrollment_id INT PRIMARY KEY,
    aggregate_marks DECIMAL(5, 2) NOT NULL,
    grade VARCHAR(5) NOT NULL,
    grade_point INT NOT NULL,
    result_status VARCHAR(20) NOT NULL,
    FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE
);
```

---

## 📂 Project Directory Structure

```
kec-academic-system/
│
├── pom.xml                                   # Maven project configuration & dependencies
├── KEC-Academic-System.bat                   # Windows batch launcher script
├── README.md                                 # Complete project documentation
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── kec/
│   │               └── academic/
│   │                   ├── MainApp.java                 # Interactive CLI Console Application
│   │                   ├── DBConnection.java            # JDBC Database connection manager
│   │                   ├── StudentDAO.java              # Student CRUD database operations
│   │                   ├── CourseDAO.java               # Course catalog management
│   │                   ├── EnrollmentDAO.java           # Student-course enrollment logic
│   │                   ├── AttendanceDAO.java           # Attendance tracking & condonations
│   │                   ├── AssessmentDAO.java           # Marks entry & storage
│   │                   ├── AssessmentWeightageDAO.java  # Configurable weightage parameters
│   │                   ├── AssessmentWeightageMenu.java # Weightage configuration CLI menu
│   │                   ├── FinalGradeService.java       # Grade computation coordinator
│   │                   ├── GradeCalculator.java         # Autonomous grading business logic
│   │                   ├── GradeDAO.java                # Grade report queries
│   │                   ├── GradeResultDAO.java          # Persistent final grade storage
│   │                   ├── OverallMarksheetDAO.java     # Consolidated student marksheet
│   │                   ├── SGPACalculator.java          # Semester GPA calculation engine
│   │                   └── App.java                     # Application bootstrap
│   └── test/
│       └── java/
│           └── com/
│               └── kec/
│                   └── academic/
│                       └── AppTest.java                 # JUnit 5 test suite
│
├── DOCUMENTS/                                # Assignment documents & marksheet files
│   ├── JAVA ASSIGNMENT- VEERAVIGNESHWAR K V 25ITR168.pdf
│   └── MARKSHEET OF STUDENT.png
│
└── OUTPUTS/                                  # Execution result screenshots
    ├── MAIN MENU.png
    ├── ADD STUDENT.png
    ├── STUDENT DATABASE.png
    ├── STUDENT STRUCTURE.png
    ├── COURSE MANAGEMENT.png
    ├── COURSE DATABASE.png
    ├── COURSE STRUCTURE.png
    ├── ENROLLMENT MANAGEMENT.png
    ├── ENROLLEMNT DATABASE.png
    ├── ENROLLMENT STRUCTURE.png
    ├── ATTANDENCE MANAGEMENT.png
    ├── ASSESSMENT MANAGEMENT.png
    ├── ASSESSMENT MARKS STRUCTURE.png
    ├── SUBJECT REPORT.png
    └── MARKSHEET OF STUDENT.png
```

---

## 🚀 Getting Started & Execution

### Prerequisites
1. **Java Development Kit (JDK 17+)** installed:
   ```bash
   java -version
   ```
2. **Apache Maven (3.8+)** installed:
   ```bash
   mvn -version
   ```
3. **MySQL Server (8.0+)** running on port `3306`.

### Database Setup
1. Log in to MySQL CLI or MySQL Workbench:
   ```bash
   mysql -u root -p
   ```
2. Execute the DDL schema provided in the [Database Schema](#️-database-schema--ddl) section.

### Environment Configuration
The database password is read securely from the environment to prevent hardcoding credentials:
```bash
# Windows Command Prompt
set KEC_DB_PASSWORD=your_mysql_password

# Windows PowerShell
$env:KEC_DB_PASSWORD="your_mysql_password"

# Linux / macOS
export KEC_DB_PASSWORD="your_mysql_password"
```

### Running the Application

#### Option 1: Using the Windows Batch Script
Simply double-click or run:
```cmd
KEC-Academic-System.bat
```
*(The script will securely prompt for your MySQL root password and launch the application)*

#### Option 2: Using Apache Maven
```bash
# Compile and package
mvn clean compile

# Execute the application
mvn exec:java -Dexec.mainClass="com.kec.academic.MainApp"
```

#### Option 3: Running Unit Tests
```bash
mvn test
```

---

## 🧑‍💻 Author Information

- **Student Name**: Veeravigneshwar K V
- **Roll Number**: 25ITR168
- **Institution**: [Kongu Engineering College (Autonomous)](https://www.kongu.ac.in/), Perundurai, Erode - 638060
- **Branch**: B.Tech Information Technology
- **Repository**: [Javaprogram-assingnment](https://github.com/veeravigneshwarkv/Javaprogram-assingnment.git)

---

## 📄 License & Attribution

This project is developed as part of the academic curriculum at **Kongu Engineering College**.  
All rights reserved © 2026 Veeravigneshwar K V.
