package com.kec.academic;

import java.sql.SQLException;
import java.util.Scanner;

public class MainApp {

    private static final Scanner scanner = new Scanner(System.in);

    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO courseDAO = new CourseDAO();
    private static final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private static final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private static final AssessmentDAO assessmentDAO = new AssessmentDAO();
    private static final GradeDAO gradeDAO = new GradeDAO();
    private static final GradeResultDAO gradeResultDAO = new GradeResultDAO();
    private static final SGPACalculator sgpaCalculator = new SGPACalculator();
    private static final FinalGradeService finalGradeService = new FinalGradeService();
private static final AssessmentWeightageDAO weightageDAO =
        new AssessmentWeightageDAO();

private static final AssessmentWeightageMenu weightageMenu =
        new AssessmentWeightageMenu();

    public static void main(String[] args) {

        System.out.println("============================================");
        System.out.println(" KEC ACADEMIC PERFORMANCE MANAGEMENT SYSTEM");
        System.out.println("============================================");

        while (true) {

            displayMenu();

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {

                    case "1":
                        studentMenu();
                        break;

                    case "2":
                        courseMenu();
                        break;

                    case "3":
                        enrollmentMenu();
                        break;

                    case "4":
                        attendanceMenu();
                        break;

                    case "5":
                        assessmentMenu();
                        break;

                    case "6":
                        gradeReportMenu();
                        break;

                    case "7":
                        calculateSGPA();
                        break;
case "8":
    weightageMenu.showMenu(scanner);
    break;

                    case "0":
                        System.out.println(
                                "\nThank you for using KEC Academic System!"
                        );
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (SQLException e) {
                System.out.println("\nDatabase operation failed.");
                System.out.println("Reason: " + e.getMessage());
                System.out.println(
                        "Check your database connection and table structure."
                );
            } catch (Exception e) {
                System.out.println("\nOperation failed.");
                System.out.println("Reason: " + e.getMessage());
            }
        }
    }

    private static void displayMenu() {

        System.out.println("\n============================================");
        System.out.println("                 MAIN MENU");
        System.out.println("============================================");
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Attendance Management");
        System.out.println("5. Assessment Management");
        System.out.println("6. View Grade Report");
        System.out.println("7. Calculate SGPA");
System.out.println("8. Assessment Weightage Configuration");
        System.out.println("0. Exit");
        System.out.println("============================================");
    }

    // ==================================================
    // STUDENT MANAGEMENT
    // ==================================================

    private static void studentMenu() throws SQLException {

        while (true) {

            System.out.println("\n---------- STUDENT MANAGEMENT ----------");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("0. Back to Main Menu");

            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    addStudent();
                    break;

                case "2":
                    studentDAO.viewAllStudents();
                    break;

                case "3":
                    System.out.print("Enter roll number: ");
                    studentDAO.searchStudent(
                            scanner.nextLine().trim().toUpperCase()
                    );
                    break;

                case "4":
                    updateStudent();
                    break;

                case "5":
                    deleteStudent();
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void addStudent() throws SQLException {

        System.out.print("Enter roll number: ");
        String rollNo = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter department: ");
        String department = scanner.nextLine().trim().toUpperCase();

        int semester = readInteger("Enter current semester: ");

        if (rollNo.isEmpty() || name.isEmpty() || department.isEmpty()) {
            System.out.println("Student details cannot be empty.");
            return;
        }

        if (semester < 1 || semester > 8) {
            System.out.println("Semester must be between 1 and 8.");
            return;
        }

        studentDAO.addStudent(rollNo, name, department, semester);
    }

    private static void updateStudent() throws SQLException {

        System.out.print("Enter roll number of student to update: ");
        String rollNo = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter new name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter new department: ");
        String department = scanner.nextLine().trim().toUpperCase();

        int semester = readInteger("Enter new current semester: ");

        if (rollNo.isEmpty() || name.isEmpty() || department.isEmpty()
                || semester < 1 || semester > 8) {

            System.out.println("Invalid student details.");
            return;
        }

        studentDAO.updateStudent(rollNo, name, department, semester);
    }

    private static void deleteStudent() throws SQLException {

        System.out.print("Enter roll number to delete: ");
        String rollNo = scanner.nextLine().trim().toUpperCase();

        if (rollNo.isEmpty()) {
            System.out.println("Roll number cannot be empty.");
            return;
        }

        System.out.print("Are you sure you want to delete this student? (yes/no): ");
        String confirmation = scanner.nextLine().trim();

        if (confirmation.equalsIgnoreCase("yes")) {
            studentDAO.deleteStudent(rollNo);
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // ==================================================
    // COURSE MANAGEMENT
    // ==================================================

    private static void courseMenu() throws SQLException {

        while (true) {

            System.out.println("\n---------- COURSE MANAGEMENT ----------");
            System.out.println("1. Add Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Search Course");
            System.out.println("0. Back to Main Menu");

            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    addCourse();
                    break;

                case "2":
                    courseDAO.viewAllCourses();
                    break;

                case "3":
                    System.out.print("Enter course code: ");
                    courseDAO.searchCourse(
                            scanner.nextLine().trim().toUpperCase()
                    );
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void addCourse() throws SQLException {

        System.out.print("Enter course code: ");
        String code = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter course title: ");
        String title = scanner.nextLine().trim();

        int credits = readInteger("Enter course credits: ");

        System.out.println("Course Types: THEORY, PRACTICAL, EMBEDDED");
        System.out.print("Enter course type: ");
        String type = scanner.nextLine().trim().toUpperCase();

        if (code.isEmpty() || title.isEmpty() || credits <= 0) {
            System.out.println("Invalid course details.");
            return;
        }

        if (!type.equals("THEORY")
                && !type.equals("PRACTICAL")
                && !type.equals("EMBEDDED")) {

            System.out.println("Invalid course type.");
            return;
        }

        courseDAO.addCourse(code, title, credits, type);
    }

    // ==================================================
    // ENROLLMENT MANAGEMENT
    // ==================================================

    private static void enrollmentMenu() throws SQLException {

        while (true) {

            System.out.println("\n---------- ENROLLMENT MANAGEMENT ----------");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. View Enrollment Records");
            System.out.println("0. Back to Main Menu");

            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    enrollStudent();
                    break;

                case "2":
                    enrollmentDAO.viewEnrollments();
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void enrollStudent() throws SQLException {

        System.out.print("Enter student roll number: ");
        String rollNo = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine().trim().toUpperCase();

        int semester = readInteger("Enter semester: ");

        if (rollNo.isEmpty() || courseCode.isEmpty()
                || semester < 1 || semester > 8) {

            System.out.println("Invalid enrollment details.");
            return;
        }

        enrollmentDAO.enrollStudent(rollNo, courseCode, semester);
    }

    // ==================================================
    // ATTENDANCE MANAGEMENT
    // ==================================================

    private static void attendanceMenu() throws SQLException {

        while (true) {

            System.out.println("\n---------- ATTENDANCE MANAGEMENT ----------");
            System.out.println("1. Update Attendance");
            System.out.println("2. Update Medical Condonation");
            System.out.println("3. View Attendance Report");
            System.out.println("0. Back to Main Menu");

            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    updateAttendance();
                    break;

                case "2":
                    updateCondonation();
                    break;

                case "3":
                    attendanceDAO.viewAttendanceReport();
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void updateAttendance() throws SQLException {

        int enrollmentId = readInteger("Enter enrollment ID: ");
        int totalClasses = readInteger("Enter total classes conducted: ");
        int attendedClasses = readInteger("Enter classes attended: ");

        if (enrollmentId <= 0 || totalClasses < 0
                || attendedClasses < 0
                || attendedClasses > totalClasses) {

            System.out.println("Invalid attendance details.");
            return;
        }

        attendanceDAO.updateAttendance(
                enrollmentId, totalClasses, attendedClasses
        );
    }

    private static void updateCondonation() throws SQLException {

        int enrollmentId = readInteger("Enter enrollment ID: ");

        if (enrollmentId <= 0) {
            System.out.println("Invalid enrollment ID.");
            return;
        }

        System.out.print("Has medical condonation been approved? (yes/no): ");
        String answer = scanner.nextLine().trim();

        if (!answer.equalsIgnoreCase("yes")
                && !answer.equalsIgnoreCase("no")) {

            System.out.println("Please enter yes or no.");
            return;
        }

        attendanceDAO.updateCondonation(
                enrollmentId, answer.equalsIgnoreCase("yes")
        );
    }

    // ==================================================
    // ASSESSMENT MANAGEMENT
    // ==================================================

    private static void assessmentMenu() throws SQLException {

        while (true) {

            System.out.println("\n---------- ASSESSMENT MANAGEMENT ----------");
            System.out.println("1. Enter or Update Assessment Marks");
            System.out.println("2. View Assessment Records");
            System.out.println("0. Back to Main Menu");

            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    enterAssessmentMarks();
                    break;

                case "2":
                    assessmentDAO.viewAssessments();
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void enterAssessmentMarks() throws SQLException {

        int enrollmentId = readInteger("Enter enrollment ID: ");

        if (enrollmentId <= 0) {
            System.out.println("Enrollment ID must be positive.");
            return;
        }

        System.out.println(
                "Enter marks from 0 to 100. Leave blank if not entered."
        );

        Double cia1 = readOptionalMark("CIA 1 marks: ");
        Double cia2 = readOptionalMark("CIA 2 marks: ");
        Double cia3 = readOptionalMark("CIA 3 marks: ");
        Double assignment = readOptionalMark("Assignment marks: ");
        Double quiz = readOptionalMark("Quiz marks: ");
        Double lab = readOptionalMark("Lab marks: ");
        Double ese = readOptionalMark("ESE marks: ");

        System.out.print("Was the student absent for ESE? (yes/no): ");
        String answer = scanner.nextLine().trim();

        if (!answer.equalsIgnoreCase("yes")
                && !answer.equalsIgnoreCase("no")) {

            System.out.println("Please enter yes or no.");
            return;
        }

        boolean absent = answer.equalsIgnoreCase("yes");

        if (!validMark(cia1) || !validMark(cia2) || !validMark(cia3)
                || !validMark(assignment) || !validMark(quiz)
                || !validMark(lab) || !validMark(ese)) {

            System.out.println("Marks must be numbers between 0 and 100.");
            return;
        }

        assessmentDAO.saveAssessment(
                enrollmentId, cia1, cia2, cia3,
                assignment, quiz, lab, ese, absent
        );
    }

    private static Double readOptionalMark(String prompt) {

        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            return null;
        }

        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    private static boolean validMark(Double mark) {

        return mark == null
                || (Double.isFinite(mark) && mark >= 0 && mark <= 100);
    }

    // ==================================================
    // GRADE REPORT
    // ==================================================

    private static void gradeReportMenu() throws SQLException {

    while (true) {

        System.out.println("\n---------- GRADE REPORT ----------");
        System.out.println("1. View Student Grade Details");
        System.out.println("2. View Saved Final Grades");
        System.out.println("3. Calculate and Save Final Grades");
        System.out.println("0. Back to Main Menu");

        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {

            case "1":
                System.out.print("Enter student roll number: ");
                String rollNo1 =
                        scanner.nextLine().trim().toUpperCase();

                if (rollNo1.isEmpty()) {
                    System.out.println("Roll number cannot be empty.");
                    break;
                }

                gradeDAO.viewStudentGradeDetails(rollNo1);
                break;

            case "2":
                System.out.print("Enter student roll number: ");
                String rollNo2 =
                        scanner.nextLine().trim().toUpperCase();

                if (rollNo2.isEmpty()) {
                    System.out.println("Roll number cannot be empty.");
                    break;
                }

                gradeResultDAO.viewGrades(rollNo2);
                break;

            case "3":
    System.out.print("Enter student roll number: ");

    String rollNo3 =
            scanner.nextLine().trim().toUpperCase();

    if (rollNo3.isEmpty()) {
        System.out.println("Roll number cannot be empty.");
        break;
    }

    finalGradeService.calculateGradesForStudent(rollNo3);
    break;

            case "0":
                return;

            default:
                System.out.println("Invalid choice!");
        }
    }
}

    // ==================================================
    // SGPA CALCULATION
    // ==================================================

    private static void calculateSGPA() throws SQLException {

        System.out.println("\n---------- SGPA CALCULATOR ----------");

        System.out.print("Enter student roll number: ");
        String rollNo = scanner.nextLine().trim().toUpperCase();

        int semester = readInteger("Enter semester: ");

        if (rollNo.isEmpty()) {
            System.out.println("Roll number cannot be empty.");
            return;
        }

        if (semester < 1 || semester > 8) {
            System.out.println("Semester must be between 1 and 8.");
            return;
        }

        sgpaCalculator.calculateSGPA(rollNo, semester);
    }

    // ==================================================
    // COMMON INPUT METHOD
    // ==================================================

    private static int readInteger(String prompt) {

        while (true) {

            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }
}