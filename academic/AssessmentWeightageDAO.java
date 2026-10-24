package com.kec.academic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class AssessmentWeightageDAO {

    public Map<String, Double> getWeightages(String courseType)
            throws SQLException {

        String sql =
                "SELECT component_name, component_weight " +
                "FROM assessment_weightages " +
                "WHERE course_type = ? " +
                "ORDER BY component_name";

        Map<String, Double> weightages = new LinkedHashMap<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseType.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    weightages.put(
                            rs.getString("component_name"),
                            rs.getDouble("component_weight")
                    );
                }
            }
        }

        return weightages;
    }

    public void saveWeightage(
            String courseType,
            String componentName,
            double weight) throws SQLException {

        if (courseType == null || componentName == null
                || courseType.isBlank() || componentName.isBlank()) {
            throw new IllegalArgumentException(
                    "Course type and component name are required."
            );
        }

        if (!Double.isFinite(weight) || weight < 0 || weight > 100) {
            throw new IllegalArgumentException(
                    "Weightage must be between 0 and 100."
            );
        }

        String type = courseType.trim().toUpperCase();
        String component = componentName.trim().toUpperCase();

        if (!type.equals("THEORY")
                && !type.equals("PRACTICAL")
                && !type.equals("EMBEDDED")) {
            throw new IllegalArgumentException(
                    "Invalid course type: " + type
            );
        }

        String sql =
                "INSERT INTO assessment_weightages " +
                "(course_type, component_name, component_weight) " +
                "VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "component_weight = VALUES(component_weight)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, type);
            ps.setString(2, component);
            ps.setDouble(3, weight);

            ps.executeUpdate();
        }
    }

    public void displayWeightages(String courseType)
            throws SQLException {

        Map<String, Double> weightages = getWeightages(courseType);

        System.out.println(
                "\nAssessment Weightages for " + courseType.toUpperCase()
        );

        if (weightages.isEmpty()) {
            System.out.println("No weightages configured yet.");
            return;
        }

        double total = 0;

        for (Map.Entry<String, Double> entry : weightages.entrySet()) {
            System.out.printf(
                    "%-15s : %.2f%%%n",
                    entry.getKey(),
                    entry.getValue()
            );

            total += entry.getValue();
        }

        System.out.printf("Total configured: %.2f%%%n", total);

        if (Math.abs(total - 100.0) > 0.01) {
            System.out.println(
                    "WARNING: Component weightages must total 100%."
            );
        }
    }
}