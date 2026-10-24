package com.kec.academic;

public class GradeCalculator {

    public static class GradeResult {
        private final String grade;
        private final int gradePoint;
        private final String status;

        public GradeResult(String grade, int gradePoint, String status) {
            this.grade = grade;
            this.gradePoint = gradePoint;
            this.status = status;
        }

        public String getGrade() {
            return grade;
        }

        public int getGradePoint() {
            return gradePoint;
        }

        public String getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return "Grade: " + grade
                    + " | Grade Point: " + gradePoint
                    + " | Status: " + status;
        }
    }

    public static GradeResult calculateGrade(
            String courseType,
            double aggregateMarks,
            double eseMarks,
            boolean eseAbsent,
            double attendancePercentage,
            boolean medicalCondonationApproved) {

        // 1. Validate the input.
        if (courseType == null) {
            throw new IllegalArgumentException(
                    "Course type cannot be null."
            );
        }

        courseType = courseType.trim().toUpperCase();

        double eseMaximum;
        double eseMinimum;

        // 2. Set the ESE maximum and minimum for each course type.
        switch (courseType) {
            case "THEORY":
                eseMaximum = 60;
                eseMinimum = 27;
                break;

            case "PRACTICAL":
                eseMaximum = 40;
                eseMinimum = 20;
                break;

            case "EMBEDDED":
                eseMaximum = 50;
                eseMinimum = 22.5;
                break;

            default:
                throw new IllegalArgumentException(
                        "Invalid course type: " + courseType
                );
        }

        if (Double.isNaN(aggregateMarks)
                || Double.isInfinite(aggregateMarks)
                || aggregateMarks < 0
                || aggregateMarks > 100) {

            throw new IllegalArgumentException(
                    "Aggregate marks must be between 0 and 100."
            );
        }

        if (Double.isNaN(attendancePercentage)
                || Double.isInfinite(attendancePercentage)
                || attendancePercentage < 0
                || attendancePercentage > 100) {

            throw new IllegalArgumentException(
                    "Attendance must be between 0 and 100."
            );
        }

        if (!eseAbsent
                && (Double.isNaN(eseMarks)
                || Double.isInfinite(eseMarks)
                || eseMarks < 0
                || eseMarks > eseMaximum)) {

            throw new IllegalArgumentException(
                    "ESE marks must be between 0 and " + eseMaximum
            );
        }

        // 3. Check attendance eligibility.
        if (attendancePercentage < 65) {
            return new GradeResult(
                    "SA", 0,
                    "Not eligible for ESE due to attendance below 65%."
            );
        }

        if (attendancePercentage < 75
                && !medicalCondonationApproved) {

            return new GradeResult(
                    "PENDING", 0,
                    "Attendance is 65% to below 75%. "
                    + "ESE eligibility requires approved condonation."
            );
        }

        // 4. Check whether the student was absent for ESE.
        if (eseAbsent) {
            return new GradeResult(
                    "AB", 0,
                    "Student was absent for ESE."
            );
        }

        // 5. Check the minimum ESE marks.
        if (eseMarks < eseMinimum) {
            return new GradeResult(
                    "RA", 0,
                    "Minimum ESE marks not achieved."
            );
        }

        // 6. Check the overall aggregate.
        if (aggregateMarks < 50) {
            return new GradeResult(
                    "RA", 0,
                    "Aggregate marks are below 50."
            );
        }

        // 7. Assign the grade based on aggregate marks.
        if (aggregateMarks >= 91) {
            return new GradeResult("O", 10, "PASS");
        } else if (aggregateMarks >= 81) {
            return new GradeResult("A+", 9, "PASS");
        } else if (aggregateMarks >= 71) {
            return new GradeResult("A", 8, "PASS");
        } else if (aggregateMarks >= 61) {
            return new GradeResult("B+", 7, "PASS");
        } else {
            return new GradeResult("B", 6, "PASS");
        }
    }
}