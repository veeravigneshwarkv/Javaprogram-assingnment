package com.kec.academic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AttendanceDAO {

    // Update attendance for a student's course enrollment
    public void updateAttendance(
            int enrollmentId,
            int totalClasses,
            int attendedClasses) throws SQLException {

        if (totalClasses < 0
                || attendedClasses < 0
                || attendedClasses > totalClasses) {

            System.out.println("Invalid attendance values.");
            System.out.println(
                    "Attended classes must be between 0 and total classes."
            );
            return;
        }

        String sql = "UPDATE enrollments "
                + "SET total_classes = ?, attended_classes = ? "
                + "WHERE enrollment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, totalClasses);
            ps.setInt(2, attendedClasses);
            ps.setInt(3, enrollmentId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Attendance updated successfully!");
            } else {
                System.out.println("Enrollment ID not found.");
            }
        }
    }

    // Record medical condonation approval
    public void updateMedicalCondonation(
            int enrollmentId,
            boolean approved) throws SQLException {

        String sql = "UPDATE enrollments "
                + "SET medical_condonation = ? "
                + "WHERE enrollment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, approved);
            ps.setInt(2, enrollmentId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println(
                        "Medical condonation status updated successfully!"
                );
            } else {
                System.out.println("Enrollment ID not found.");
            }
        }
    }

    // Display attendance and eligibility
    public void viewAttendance() throws SQLException {

        String sql = "SELECT e.enrollment_id, e.roll_no, "
                + "s.name, e.course_code, c.course_title, "
                + "e.total_classes, e.attended_classes, "
                + "e.medical_condonation "
                + "FROM enrollments e "
                + "JOIN students_record s ON e.roll_no = s.roll_no "
                + "JOIN courses c ON e.course_code = c.course_code "
                + "ORDER BY e.roll_no, e.course_code";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            System.out.println("\n========== ATTENDANCE REPORT ==========");

            while (rs.next()) {

                found = true;

                int total = rs.getInt("total_classes");
                int attended = rs.getInt("attended_classes");
                boolean condonation =
                        rs.getBoolean("medical_condonation");

                double percentage = total > 0
                        ? attended * 100.0 / total
                        : 0.0;

                String status = getEligibility(
                        total,
                        percentage,
                        condonation
                );

                System.out.println("\nEnrollment ID: "
                        + rs.getInt("enrollment_id"));

                System.out.println("Roll Number: "
                        + rs.getString("roll_no"));

                System.out.println("Student Name: "
                        + rs.getString("name"));

                System.out.println("Course: "
                        + rs.getString("course_code")
                        + " - "
                        + rs.getString("course_title"));

                System.out.println("Total Classes: " + total);

                System.out.println("Attended Classes: " + attended);

                System.out.printf(
                        "Attendance Percentage: %.2f%%%n",
                        percentage
                );

                System.out.println("Medical Condonation Approved: "
                        + (condonation ? "Yes" : "No"));

                System.out.println("ESE Eligibility: " + status);

                System.out.println("---------------------------------------");
            }

            if (!found) {
                System.out.println("No enrollments found.");
            }
        }
    }

    // Determine ESE eligibility
    private String getEligibility(
            int total,
            double percentage,
            boolean condonation) {

        if (total == 0) {
            return "Attendance not recorded";
        }

        if (percentage >= 75.0) {
            return "Eligible";
        }

        if (percentage >= 65.0 && condonation) {
            return "Eligible through approved condonation";
        }

        if (percentage >= 65.0) {
            return "Conditional - condonation required";
        }

        return "Not eligible - SA";
    }
public void updateCondonation(
        int enrollmentId,
        boolean approved) {

    String sql =
            "UPDATE enrollments " +
            "SET medical_condonation = ? " +
            "WHERE enrollment_id = ?";

    try (java.sql.Connection con = DBConnection.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setBoolean(1, approved);
        ps.setInt(2, enrollmentId);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println(
                    "Medical condonation updated successfully."
            );
        } else {
            System.out.println("Enrollment ID not found.");
        }

    } catch (java.sql.SQLException e) {
        System.out.println(
                "Error updating condonation: " + e.getMessage()
        );
    }
}
public void viewAttendanceReport() {

    String sql =
            "SELECT e.enrollment_id, s.roll_no, s.name, " +
            "c.course_code, e.total_classes, e.attended_classes, " +
            "e.medical_condonation " +
            "FROM enrollments e " +
            "JOIN students_record s ON e.roll_no = s.roll_no " +
            "JOIN courses c ON e.course_code = c.course_code " +
            "ORDER BY s.roll_no, c.course_code";

    try (java.sql.Connection con = DBConnection.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql);
         java.sql.ResultSet rs = ps.executeQuery()) {

        boolean found = false;

        System.out.println("\n========== ATTENDANCE REPORT ==========");

        while (rs.next()) {

            found = true;

            int total = rs.getInt("total_classes");
            int attended = rs.getInt("attended_classes");

            double percentage = total == 0
                    ? 0.0
                    : attended * 100.0 / total;

            System.out.println("\n--------------------------------");
            System.out.println(
                    "Enrollment ID: " + rs.getInt("enrollment_id")
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
            System.out.println("Total Classes: " + total);
            System.out.println("Attended Classes: " + attended);

            System.out.printf(
                    "Attendance: %.2f%%%n", percentage
            );

            System.out.println(
                    "Medical Condonation Approved: "
                            + rs.getBoolean("medical_condonation")
            );

            if (total == 0) {
                System.out.println("Status: Attendance not recorded");
            } else if (percentage >= 75) {
                System.out.println("Status: Eligible by attendance");
            } else if (percentage >= 65) {
                System.out.println(
                        "Status: Check medical condonation approval"
                );
            } else {
                System.out.println(
                        "Status: Below 65% - not eligible for ESE"
                );
            }
        }

        if (!found) {
            System.out.println("No attendance records found.");
        }

    } catch (java.sql.SQLException e) {
        System.out.println(
                "Error viewing attendance report: " + e.getMessage()
        );
    }
}
}