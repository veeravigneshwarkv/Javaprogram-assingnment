package com.kec.academic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseDAO {

    // 1. Add a new course
    public void addCourse(
            String courseCode,
            String courseTitle,
            int credits,
            String courseType) throws SQLException {

        String sql = "INSERT INTO courses "
                + "(course_code, course_title, credits, course_type) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseCode);
            ps.setString(2, courseTitle);
            ps.setInt(3, credits);
            ps.setString(4, courseType);

            ps.executeUpdate();

            System.out.println("\nCourse added successfully!");
        }
    }

    // 2. Display all courses
    public void viewAllCourses() throws SQLException {

        String sql = "SELECT course_code, course_title, credits, course_type "
                + "FROM courses ORDER BY course_code";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            System.out.println("\n========== COURSE CATALOG ==========");

            while (rs.next()) {

                found = true;

                System.out.println("\nCourse Code: "
                        + rs.getString("course_code"));

                System.out.println("Course Title: "
                        + rs.getString("course_title"));

                System.out.println("Credits: "
                        + rs.getInt("credits"));

                System.out.println("Course Type: "
                        + rs.getString("course_type"));

                System.out.println("------------------------------------");
            }

            if (!found) {
                System.out.println("No courses registered yet.");
            }
        }
    }

    // 3. Search course by code
    public void searchCourse(String courseCode) throws SQLException {

        String sql = "SELECT course_code, course_title, credits, course_type "
                + "FROM courses WHERE course_code = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseCode);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    System.out.println("\n========== COURSE FOUND ==========");

                    System.out.println("Course Code: "
                            + rs.getString("course_code"));

                    System.out.println("Course Title: "
                            + rs.getString("course_title"));

                    System.out.println("Credits: "
                            + rs.getInt("credits"));

                    System.out.println("Course Type: "
                            + rs.getString("course_type"));

                } else {
                    System.out.println("\nCourse not found.");
                }
            }
        }
    }
}