package com.kec.academic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GradeDAO {

    public void viewStudentGradeDetails(String rollNo) {

        String sql =
                "SELECT s.roll_no, s.name, " +
                "c.course_code, c.course_title, c.course_type, " +
                "e.total_classes, e.attended_classes, " +
                "e.medical_condonation, " +
                "a.cia1, a.cia2, a.cia3, " +
                "a.assignment_marks, a.quiz_marks, " +
                "a.lab_marks, a.ese_marks, a.ese_absent " +
                "FROM students_record s " +
                "JOIN enrollments e ON s.roll_no = e.roll_no " +
                "JOIN courses c ON e.course_code = c.course_code " +
                "LEFT JOIN assessment_marks a " +
                "ON e.enrollment_id = a.enrollment_id " +
                "WHERE s.roll_no = ? " +
                "ORDER BY c.course_code";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    int totalClasses = rs.getInt("total_classes");
                    int attendedClasses = rs.getInt("attended_classes");

                    double attendance = 0.0;

                    if (totalClasses > 0) {
                        attendance =
                                attendedClasses * 100.0 / totalClasses;
                    }

                    System.out.println();
                    System.out.println(
                            "========================================"
                    );

                    System.out.println(
                            "Student Roll No : " + rs.getString("roll_no")
                    );

                    System.out.println(
                            "Student Name    : " + rs.getString("name")
                    );

                    System.out.println(
                            "Course Code     : " + rs.getString("course_code")
                    );

                    System.out.println(
                            "Course Title    : " + rs.getString("course_title")
                    );

                    System.out.println(
                            "Course Type     : " + rs.getString("course_type")
                    );

                    System.out.println(
                            "Attendance      : "
                                    + String.format("%.2f", attendance) + "%"
                    );

                    System.out.println(
                            "Medical Condonation Approved: "
                                    + rs.getBoolean("medical_condonation")
                    );

                    System.out.println(
                            "CIA 1            : " + getMark(rs, "cia1")
                    );

                    System.out.println(
                            "CIA 2            : " + getMark(rs, "cia2")
                    );

                    System.out.println(
                            "CIA 3            : " + getMark(rs, "cia3")
                    );

                    System.out.println(
                            "Assignment Marks : "
                                    + getMark(rs, "assignment_marks")
                    );

                    System.out.println(
                            "Quiz Marks       : " + getMark(rs, "quiz_marks")
                    );

                    System.out.println(
                            "Lab Marks        : " + getMark(rs, "lab_marks")
                    );

                    System.out.println(
                            "ESE Marks        : " + getMark(rs, "ese_marks")
                    );

                    System.out.println(
                            "ESE Absent       : " + rs.getBoolean("ese_absent")
                    );

                    System.out.println(
                            "Final Grade      : Not calculated yet"
                    );

                    System.out.println(
                            "========================================"
                    );
                }

                if (!found) {
                    System.out.println(
                            "No enrollment records found for roll number: "
                                    + rollNo
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Could not retrieve grade details: " + e.getMessage()
            );
        }
    }

    private String getMark(ResultSet rs, String column)
            throws SQLException {

        double mark = rs.getDouble(column);

        if (rs.wasNull()) {
            return "Not entered";
        }

        return String.format("%.2f", mark);
    }
}