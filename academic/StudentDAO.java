package com.kec.academic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO {

    // 1. Add student
    public void addStudent(
            String rollNo, String name,
            String department, int semester) throws SQLException {

        String sql = "INSERT INTO students_record "
                + "(roll_no, name, department, current_semester) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);
            ps.setString(2, name);
            ps.setString(3, department);
            ps.setInt(4, semester);

            ps.executeUpdate();
            System.out.println("Student added successfully!");
        }
    }

    // 2. View all students
    public void viewAllStudents() throws SQLException {

        String sql = "SELECT * FROM students_record ORDER BY roll_no";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            System.out.println("\n========== STUDENT RECORDS ==========");

            while (rs.next()) {
                found = true;

                System.out.println("Roll Number: " + rs.getString("roll_no"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Department: " + rs.getString("department"));
                System.out.println("Semester: "
                        + rs.getInt("current_semester"));
                System.out.println("-------------------------------------");
            }

            if (!found) {
                System.out.println("No student records found.");
            }
        }
    }

    // 3. Search student
    public void searchStudent(String rollNo) throws SQLException {

        String sql = "SELECT * FROM students_record WHERE roll_no = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    System.out.println("\nStudent Found!");
                    System.out.println("Roll Number: "
                            + rs.getString("roll_no"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Department: "
                            + rs.getString("department"));
                    System.out.println("Semester: "
                            + rs.getInt("current_semester"));
                } else {
                    System.out.println("Student not found.");
                }
            }
        }
    }

    // 4. Update student
    public void updateStudent(
            String rollNo, String name,
            String department, int semester) throws SQLException {

        String sql = "UPDATE students_record "
                + "SET name = ?, department = ?, current_semester = ? "
                + "WHERE roll_no = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, department);
            ps.setInt(3, semester);
            ps.setString(4, rollNo);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student not found.");
            }
        }
    }

    // 5. Delete student
    public void deleteStudent(String rollNo) throws SQLException {

        String sql = "DELETE FROM students_record WHERE roll_no = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student not found.");
            }
        }
    }
}