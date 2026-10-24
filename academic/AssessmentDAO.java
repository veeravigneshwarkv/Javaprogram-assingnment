package com.kec.academic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssessmentDAO {

    public void saveAssessment(
            int enrollmentId,
            Double cia1,
            Double cia2,
            Double cia3,
            Double assignment,
            Double quiz,
            Double lab,
            Double ese,
            boolean eseAbsent) {

        String sql =
                "INSERT INTO assessment_marks " +
                "(enrollment_id, cia1, cia2, cia3, assignment_marks, " +
                "quiz_marks, lab_marks, ese_marks, ese_absent) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "cia1 = VALUES(cia1), " +
                "cia2 = VALUES(cia2), " +
                "cia3 = VALUES(cia3), " +
                "assignment_marks = VALUES(assignment_marks), " +
                "quiz_marks = VALUES(quiz_marks), " +
                "lab_marks = VALUES(lab_marks), " +
                "ese_marks = VALUES(ese_marks), " +
                "ese_absent = VALUES(ese_absent)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);

            setNullableMark(ps, 2, cia1);
            setNullableMark(ps, 3, cia2);
            setNullableMark(ps, 4, cia3);
            setNullableMark(ps, 5, assignment);
            setNullableMark(ps, 6, quiz);
            setNullableMark(ps, 7, lab);
            setNullableMark(ps, 8, ese);

            ps.setBoolean(9, eseAbsent);

            ps.executeUpdate();

            System.out.println("Assessment marks saved successfully!");

        } catch (SQLException e) {
            System.out.println(
                    "Error saving assessment marks: " + e.getMessage()
            );
        }
    }

    private void setNullableMark(
            PreparedStatement ps, int index, Double mark)
            throws SQLException {

        if (mark == null) {
            ps.setNull(index, java.sql.Types.DECIMAL);
        } else {
            ps.setDouble(index, mark);
        }
    }

    public void viewAssessments() {

        String sql =
                "SELECT enrollment_id, cia1, cia2, cia3, " +
                "assignment_marks, quiz_marks, lab_marks, " +
                "ese_marks, ese_absent " +
                "FROM assessment_marks ORDER BY enrollment_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            System.out.println("\n========== ASSESSMENT RECORDS ==========");

            while (rs.next()) {

                found = true;

                System.out.println("\n-----------------------------------");
                System.out.println(
                        "Enrollment ID: " + rs.getInt("enrollment_id")
                );
                System.out.println("CIA 1: " + displayMark(rs, "cia1"));
                System.out.println("CIA 2: " + displayMark(rs, "cia2"));
                System.out.println("CIA 3: " + displayMark(rs, "cia3"));

                System.out.println(
                        "Assignment: " + displayMark(rs, "assignment_marks")
                );
                System.out.println(
                        "Quiz: " + displayMark(rs, "quiz_marks")
                );
                System.out.println(
                        "Lab: " + displayMark(rs, "lab_marks")
                );
                System.out.println(
                        "ESE: " + displayMark(rs, "ese_marks")
                );
                System.out.println(
                        "ESE Absent: " + rs.getBoolean("ese_absent")
                );
            }

            if (!found) {
                System.out.println("No assessment records found.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error viewing assessments: " + e.getMessage()
            );
        }
    }

    private String displayMark(ResultSet rs, String column)
            throws SQLException {

        double mark = rs.getDouble(column);

        if (rs.wasNull()) {
            return "Not entered";
        }

        return String.valueOf(mark);
    }
}