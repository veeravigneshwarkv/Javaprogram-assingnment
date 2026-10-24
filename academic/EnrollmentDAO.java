package com.kec.academic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnrollmentDAO {

    // Enroll a student in a course
    public void enrollStudent(
            String rollNo,
            String courseCode,
            int semester) throws SQLException {

        String sql = "INSERT INTO enrollments "
                + "(roll_no, course_code, semester, "
                + "total_classes, attended_classes) "
                + "VALUES (?, ?, ?, 0, 0)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);
            ps.setString(2, courseCode);
            ps.setInt(3, semester);

            ps.executeUpdate();

            System.out.println(
                    "Student enrolled successfully!"
            );
        }
    }

    // Display all enrollments
    public void viewAllEnrollments() throws SQLException {

        String sql = "SELECT e.enrollment_id, e.roll_no, "
                + "s.name, e.course_code, c.course_title, e.semester "
                + "FROM enrollments e "
                + "JOIN students_record s ON e.roll_no = s.roll_no "
                + "JOIN courses c ON e.course_code = c.course_code "
                + "ORDER BY e.semester, e.roll_no";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            System.out.println("\n========== STUDENT ENROLLMENTS ==========");

            while (rs.next()) {

                found = true;

                System.out.println(
                        "\nEnrollment ID: "
                                + rs.getInt("enrollment_id")
                );

                System.out.println(
                        "Roll Number: " + rs.getString("roll_no")
                );

                System.out.println(
                        "Student Name: " + rs.getString("name")
                );

                System.out.println(
                        "Course Code: " + rs.getString("course_code")
                );

                System.out.println(
                        "Course Title: " + rs.getString("course_title")
                );

                System.out.println(
                        "Semester: " + rs.getInt("semester")
                );

                System.out.println("-----------------------------------------");
            }

            if (!found) {
                System.out.println("No enrollments found.");
            }
        }
    }
public void viewEnrollments() {

    String sql =
            "SELECT e.enrollment_id, s.roll_no, s.name, " +
            "c.course_code, c.course_title, e.semester, " +
            "e.total_classes, e.attended_classes " +
            "FROM enrollments e " +
            "JOIN students_record s ON e.roll_no = s.roll_no " +
            "JOIN courses c ON e.course_code = c.course_code " +
            "ORDER BY s.roll_no, e.semester, c.course_code";

    try (java.sql.Connection con = DBConnection.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql);
         java.sql.ResultSet rs = ps.executeQuery()) {

        boolean found = false;

        while (rs.next()) {
            found = true;

            System.out.println("\n-------------------------------");
            System.out.println("Enrollment ID: "
                    + rs.getInt("enrollment_id"));
            System.out.println("Roll Number: "
                    + rs.getString("roll_no"));
            System.out.println("Student: "
                    + rs.getString("name"));
            System.out.println("Course Code: "
                    + rs.getString("course_code"));
            System.out.println("Course Title: "
                    + rs.getString("course_title"));
            System.out.println("Semester: "
                    + rs.getInt("semester"));
            System.out.println("Total Classes: "
                    + rs.getInt("total_classes"));
            System.out.println("Attended Classes: "
                    + rs.getInt("attended_classes"));
        }

        if (!found) {
            System.out.println("No enrollment records found.");
        }

    } catch (java.sql.SQLException e) {
        System.out.println(
                "Error viewing enrollments: " + e.getMessage()
        );
    }
}
}