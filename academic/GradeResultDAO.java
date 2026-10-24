package com.kec.academic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GradeResultDAO {

    // Save or update a student's calculated result.
    // Save or update a student's calculated result.
public void saveGrade(
        int enrollmentId,
        Double aggregateMarks,
        GradeCalculator.GradeResult result) throws SQLException {

    String sql =
            "INSERT INTO final_grades " +
            "(enrollment_id, aggregate_marks, grade, " +
            "grade_point, result_status) " +
            "VALUES (?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "aggregate_marks = VALUES(aggregate_marks), " +
            "grade = VALUES(grade), " +
            "grade_point = VALUES(grade_point), " +
            "result_status = VALUES(result_status)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, enrollmentId);

        if (aggregateMarks == null) {
            ps.setNull(2, java.sql.Types.DECIMAL);
        } else {
            ps.setDouble(2, aggregateMarks);
        }

        ps.setString(3, result.getGrade());
        ps.setInt(4, result.getGradePoint());
        ps.setString(5, result.getStatus());

        ps.executeUpdate();

        System.out.println("Grade result saved successfully!");
    }
}

    // Display all saved results for one student.
    public void viewGrades(String rollNo) {

        String sql =
                "SELECT s.roll_no, s.name, " +
                "c.course_code, c.course_title, c.credits, " +
                "f.aggregate_marks, f.grade, " +
                "f.grade_point, f.result_status " +
                "FROM final_grades f " +
                "JOIN enrollments e " +
                "ON f.enrollment_id = e.enrollment_id " +
                "JOIN students_record s " +
                "ON e.roll_no = s.roll_no " +
                "JOIN courses c " +
                "ON e.course_code = c.course_code " +
                "WHERE s.roll_no = ? " +
                "ORDER BY c.course_code";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                System.out.println("\n========== STUDENT GRADE REPORT ==========");

                while (rs.next()) {
                    found = true;

                    System.out.println(
                            "\nRoll Number : " + rs.getString("roll_no")
                    );

                    System.out.println(
                            "Name        : " + rs.getString("name")
                    );

                    System.out.println(
                            "Course Code : " + rs.getString("course_code")
                    );

                    System.out.println(
                            "Course Title: " + rs.getString("course_title")
                    );

                    System.out.println(
                            "Credits     : " + rs.getInt("credits")
                    );

                    Object marks = rs.getObject("aggregate_marks");

                    System.out.println(
                            "Aggregate   : "
                                    + (marks == null
                                    ? "Not calculated"
                                    : marks + "/100")
                    );

                    System.out.println(
                            "Grade       : " + rs.getString("grade")
                    );

                    System.out.println(
                            "Grade Point : " + rs.getInt("grade_point")
                    );

                    System.out.println(
                            "Status      : " + rs.getString("result_status")
                    );

                    System.out.println("------------------------------------------");
                }

                if (!found) {
                    System.out.println(
                            "No saved grades found for roll number: " + rollNo
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error retrieving grades: " + e.getMessage()
            );
        }
    }
}