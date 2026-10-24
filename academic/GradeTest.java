package com.kec.academic;

public class GradeTest {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println(" KEC POINT GRADING SYSTEM - TEST");
        System.out.println("======================================");

        // Test 1: Theory - O grade
        testGrade(
                "THEORY", 95, 45, false, 90, false
        );

        // Test 2: Theory - A+ grade
        testGrade(
                "THEORY", 85, 35, false, 90, false
        );

        // Test 3: Theory - ESE minimum not achieved
        testGrade(
                "THEORY", 70, 20, false, 90, false
        );

        // Test 4: Practical - A grade
        testGrade(
                "PRACTICAL", 75, 25, false, 90, false
        );

        // Test 5: Attendance below 65%
        testGrade(
                "EMBEDDED", 80, 30, false, 60, false
        );

        // Test 6: Student absent for ESE
        testGrade(
                "THEORY", 90, 0, true, 90, false
        );

        // Test 7: Attendance condonation approved
        testGrade(
                "THEORY", 85, 35, false, 70, true
        );

        System.out.println("======================================");
        System.out.println(" TESTING COMPLETED!");
        System.out.println("======================================");
    }

    private static void testGrade(
            String courseType,
            double aggregate,
            double eseMarks,
            boolean absent,
            double attendance,
            boolean condonationApproved) {

        GradeCalculator.GradeResult result =
                GradeCalculator.calculateGrade(
                        courseType,
                        aggregate,
                        eseMarks,
                        absent,
                        attendance,
                        condonationApproved
                );

        System.out.println();
        System.out.println("Course Type : " + courseType);
        System.out.println("Aggregate   : " + aggregate);
        System.out.println("ESE Marks   : "
                + (absent ? "ABSENT" : eseMarks));
        System.out.println("Attendance  : " + attendance + "%");
        System.out.println(result);
    }
}