package uk.edu.glos.s1909632.ct6013.backend;

import java.util.List;

public class CourseStats {
    public static final class ModuleStats {
        private final String moduleId;
        private final String moduleName;
        private final Double averageMark;
        private final Long numberOfStudents;

        public ModuleStats(String moduleId, String moduleName, Double averageMark,
                           Long numberOfStudents) {
            this.moduleId = moduleId;
            this.moduleName = moduleName;
            this.averageMark = averageMark;
            this.numberOfStudents = numberOfStudents;
        }

        public String getModuleId() {
            return moduleId;
        }

        public String getModuleName() {
            return moduleName;
        }

        public Double getAverageMark() {
            return averageMark;
        }

        public Long getNumberOfStudents() {
            return numberOfStudents;
        }
    }

    private final String courseId;
    private final String courseName;
    private final String courseYear;
    private final List<ModuleStats> moduleStats;
    private final double averageMark;
    private final Long firstGrades;
    private final Long twoOneGrades;
    private final Long twoTwoGrades;
    private final Long thirdGrades;
    private final Long failGrades;

    public CourseStats(String courseId, String courseName, String courseYear, List<ModuleStats> moduleStats,
                       double averageMark, Long firstGrades, Long twoOneGrades,
                       Long twoTwoGrades, Long thirdGrades, Long failGrades) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseYear = courseYear;
        this.moduleStats = moduleStats;
        this.averageMark = averageMark;
        this.firstGrades = firstGrades;
        this.twoOneGrades = twoOneGrades;
        this.twoTwoGrades = twoTwoGrades;
        this.thirdGrades = thirdGrades;
        this.failGrades = failGrades;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseYear() {
        return courseYear;
    }

    public List<ModuleStats> getModuleStats() {
        return moduleStats;
    }

    public double getAverageMark() {
        return averageMark;
    }

    public Long getFirstGrades() {
        return firstGrades;
    }

    public Long getTwoOneGrades() {
        return twoOneGrades;
    }

    public Long getTwoTwoGrades() {
        return twoTwoGrades;
    }

    public Long getThirdGrades() {
        return thirdGrades;
    }

    public Long getFailGrades() {
        return failGrades;
    }
}
