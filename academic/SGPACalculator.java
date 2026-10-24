package com.kec.academic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SGPACalculator {

    public void calculateSGPA(String rollNo, int semester) {

        String sql =
                "SELECT c.course_code, c.credits, " +
                "f.grade_point, f.result_status " +
                "FROM enrollments e " +
                "JOIN courses c ON e.course_code = c.course_code " +
                "LEFT JOIN final_grades f " +
                "ON e.enrollment_id = f.enrollment_id " +
                "WHERE e.roll_no = ? AND e.semester = ?";

        double totalCreditPoints = 0.0;
        int totalCredits = 0;
        int courseCount = 0;
        boolean incomplete = false;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);
            ps.setInt(2, semester);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    courseCount++;

                    String courseCode =
                            rs.getString("course_code");

                    int credits = rs.getInt("credits");

                    String status =
                            rs.getString("result_status");

                    Object gradePointValue =
                            rs.getObject("grade_point");

                    // Check whether the grade has been calculated.
                    if (status == null || gradePointValue == null) {

                        System.out.println(
                                "Grade not calculated for course: "
                                        + courseCode
                        );

                        incomplete = true;
                        continue;
                    }

                    // Do not calculate SGPA using pending results.
                    if ("PENDING".equalsIgnoreCase(status)) {

                        System.out.println(
                                "Result pending for course: "
                                        + courseCode
                        );

                        incomplete = true;
                        continue;
                    }

                    // Validate the stored result status.
                    if (!"PASS".equalsIgnoreCase(status)
                            && !"RA".equalsIgnoreCase(status)
                            && !"SA".equalsIgnoreCase(status)
                            && !"AB".equalsIgnoreCase(status)) {

                        System.out.println(
                                "Unknown result status for course: "
                                        + courseCode
                        );

                        incomplete = true;
                        continue;
                    }

                    int gradePoint =
                            ((Number) gradePointValue).intValue();

                    if (credits <= 0) {
                        System.out.println(
                                "Invalid credits for course: "
                                        + courseCode
                        );

                        incomplete = true;
                        continue;
                    }

                    if (gradePoint < 0 || gradePoint > 10) {
                        System.out.println(
                                "Invalid grade point for course: "
                                        + courseCode
                        );

                        incomplete = true;
                        continue;
                    }

                    totalCreditPoints += credits * gradePoint;
                    totalCredits += credits;
                }
            }

            // Check whether the student has any courses.
            if (courseCount == 0) {
                System.out.println(
                        "No enrollments found for " + rollNo
                                + " in semester " + semester
                );
                return;
            }

            // Do not produce a misleading SGPA.
            if (incomplete) {
                System.out.println(
                        "\nSGPA cannot be calculated. "
                                + "Resolve incomplete or invalid results."
                );
                return;
            }

            if (totalCredits == 0) {
                System.out.println(
                        "SGPA cannot be calculated because "
                                + "total credits are zero."
                );
                return;
            }

            // SGPA = Sum(Credits * Grade Points) / Sum(Credits)
            double sgpa = totalCreditPoints / totalCredits;

            // Equivalent percentage = SGPA * 10
            double percentage = sgpa * 10;

            System.out.println(
                    "\n=========================================="
            );
            System.out.println(
                    "          SEMESTER SGPA REPORT"
            );
            System.out.println(
                    "=========================================="
            );

            System.out.println("Roll Number : " + rollNo);
            System.out.println("Semester    : " + semester);

            System.out.printf(
                    "Total Credits: %d%n",
                    totalCredits
            );

            System.out.printf(
                    "SGPA        : %.2f%n",
                    sgpa
            );

            System.out.printf(
                    "Equivalent Percentage: %.2f%%%n",
                    percentage
            );

            System.out.println(
                    "=========================================="
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error calculating SGPA: " + e.getMessage()
            );
        }
    }
}