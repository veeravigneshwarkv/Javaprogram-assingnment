package com.kec.academic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class FinalGradeService {

    private final AssessmentWeightageDAO weightageDAO =
            new AssessmentWeightageDAO();

    private final GradeResultDAO gradeResultDAO =
            new GradeResultDAO();

    public void calculateGradesForStudent(String rollNo) {

        String sql = """
            SELECT
                e.enrollment_id,
                e.roll_no,
                e.course_code,
                e.total_classes,
                e.attended_classes,
                e.medical_condonation,
                c.course_type,
                a.cia1,
                a.cia2,
                a.cia3,
                a.assignment_marks,
                a.quiz_marks,
                a.lab_marks,
                a.ese_marks,
                a.ese_absent
            FROM enrollments e
            JOIN courses c ON e.course_code = c.course_code
            LEFT JOIN assessment_marks a
                ON e.enrollment_id = a.enrollment_id
            WHERE e.roll_no = ?
            ORDER BY e.course_code
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    int enrollmentId = rs.getInt("enrollment_id");
                    String courseCode = rs.getString("course_code");
                    String courseType = rs.getString("course_type");

                    int totalClasses = rs.getInt("total_classes");
                    int attendedClasses = rs.getInt("attended_classes");

                    boolean condonation =
                            rs.getBoolean("medical_condonation");

                    boolean eseAbsent = rs.getBoolean("ese_absent");

                    if (totalClasses <= 0) {
                        System.out.println(
                                courseCode + ": Attendance not recorded.");
                        continue;
                    }

                    double attendance =
                            attendedClasses * 100.0 / totalClasses;

                    if (attendance < 65.0) {
                        GradeCalculator.GradeResult result =
                                GradeCalculator.calculateGrade(
                                        courseType, 0, 0, false,
                                        attendance, condonation);

                        gradeResultDAO.saveGrade(
                                enrollmentId, 0.0, result);

                        System.out.println(
                                courseCode + ": SA - attendance below 65%.");
                        continue;
                    }

                    if (attendance < 75.0 && !condonation) {
                        System.out.println(
                                courseCode
                                + ": Pending - attendance requires condonation.");
                        continue;
                    }

                    if (eseAbsent) {
                        GradeCalculator.GradeResult result =
                                GradeCalculator.calculateGrade(
                                        courseType, 0, 0, true,
                                        attendance, condonation);

                        gradeResultDAO.saveGrade(
                                enrollmentId, 0.0, result);

                        System.out.println(courseCode + ": AB");
                        continue;
                    }

                    Double cia1 = getMark(rs, "cia1");
                    Double cia2 = getMark(rs, "cia2");
                    Double cia3 = getMark(rs, "cia3");
                    Double assignment = getMark(rs, "assignment_marks");
                    Double quiz = getMark(rs, "quiz_marks");
                    Double lab = getMark(rs, "lab_marks");
                    Double ese = getMark(rs, "ese_marks");

                    if (cia1 == null || cia2 == null || cia3 == null
                            || assignment == null || quiz == null
                            || lab == null || ese == null) {

                        System.out.println(
                                courseCode + ": Required marks are missing.");
                        continue;
                    }

                    Map<String, Double> weights =
                            weightageDAO.getWeightages(courseType);

                    String[] components = {
                        "CIA1", "CIA2", "CIA3",
                        "ASSIGNMENT", "QUIZ", "LAB"
                    };

                    double weightTotal = 0.0;

                    for (String component : components) {
                        Double weight = weights.get(component);

                        if (weight == null) {
                            weightTotal = -1.0;
                            break;
                        }

                        weightTotal += weight;
                    }

                    if (weightTotal < 0 || Math.abs(weightTotal - 100.0) > 0.01) {
                        System.out.println(
                                courseCode
                                + ": Weightages are missing or do not total 100%.");
                        continue;
                    }

                    double internalScore =
                            cia1 * weights.get("CIA1") / 100.0
                          + cia2 * weights.get("CIA2") / 100.0
                          + cia3 * weights.get("CIA3") / 100.0
                          + assignment * weights.get("ASSIGNMENT") / 100.0
                          + quiz * weights.get("QUIZ") / 100.0
                          + lab * weights.get("LAB") / 100.0;

                    double internalWeight;
                    double eseWeight;
                    double eseMaximum;

                    switch (courseType.toUpperCase()) {
                        case "THEORY":
                            internalWeight = 40.0;
                            eseWeight = 60.0;
                            eseMaximum = 60.0;
                            break;

                        case "PRACTICAL":
                            internalWeight = 60.0;
                            eseWeight = 40.0;
                            eseMaximum = 40.0;
                            break;

                        case "EMBEDDED":
                            internalWeight = 50.0;
                            eseWeight = 50.0;
                            eseMaximum = 50.0;
                            break;

                        default:
                            System.out.println(
                                    courseCode + ": Unknown course type.");
                            continue;
                    }

                    if (ese < 0 || ese > eseMaximum) {
                        System.out.println(
                                courseCode + ": ESE must be between 0 and "
                                + eseMaximum + ".");
                        continue;
                    }

                    double aggregate =
                            internalScore * internalWeight / 100.0
                          + ese * eseWeight / eseMaximum;

                    aggregate = Math.max(0.0, Math.min(100.0, aggregate));

                    GradeCalculator.GradeResult result =
                            GradeCalculator.calculateGrade(
                                    courseType,
                                    aggregate,
                                    ese,
                                    false,
                                    attendance,
                                    condonation);

                    gradeResultDAO.saveGrade(
                            enrollmentId, aggregate, result);

                    System.out.printf(
                            "%s | Internal: %.2f | ESE: %.2f | "
                            + "Aggregate: %.2f | %s%n",
                            courseCode,
                            internalScore,
                            ese,
                            aggregate,
                            result);
                }

                if (!found) {
                    System.out.println(
                            "No enrollments found for student " + rollNo);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Unable to calculate grades: " + e.getMessage());
        }
    }

    private Double getMark(ResultSet rs, String column)
            throws SQLException {

        double value = rs.getDouble(column);

        if (rs.wasNull()) {
            return null;
        }

        return value;
    }
}