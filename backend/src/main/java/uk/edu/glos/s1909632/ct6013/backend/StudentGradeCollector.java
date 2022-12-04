package uk.edu.glos.s1909632.ct6013.backend;

import uk.edu.glos.s1909632.ct6013.backend.persistence.StudentModule;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class StudentGradeCollector implements Collector<
        StudentModule,
        StudentGradeCollector.StudentGradeAccumulator,
        StudentGradeCollector.StudentGradeResult> {

    public static final class StudentGradeResult {
        private final double averageMark;
        private final Grade grade;

        public StudentGradeResult(double averageMark, Grade grade) {
            this.averageMark = averageMark;
            this.grade = grade;
        }

        public double getAverageMark() {
            return averageMark;
        }

        public Optional<Grade> getGrade() {
            return Optional.ofNullable(grade);
        }
    }

    protected static final class StudentGradeAccumulator {
        private final Set<StudentModule> modules;
        private int gradesAdded = 0;
        private int gradeSum = 0;

        public StudentGradeAccumulator() {
            modules = new HashSet<>();
        }

        public StudentGradeAccumulator(
                int gradesAdded, int gradeSum, Set<StudentModule> modules) {
            this.modules = modules;
            this.gradesAdded = gradesAdded;
            this.gradeSum = gradeSum;
        }

        private Grade calculateGrade(double averageMark) {
            Grade grade;

            if (averageMark > 70) {
                grade = Grade.FIRST;
            } else if (averageMark > 60) {
                grade = Grade.TWO_ONE;
            } else if (averageMark > 50) {
                grade = Grade.TWO_TWO;
            } else if (averageMark > 40) {
                grade = Grade.THIRD;
            } else {
                grade = Grade.FAIL;
            }
            return grade;
        }

        public void add(StudentModule module) {
            modules.add(module);
            if (module.getMark() != null) gradeSum += module.getMark();
            ++gradesAdded;
        }

        public StudentGradeAccumulator combine(StudentGradeAccumulator accumulator) {
            HashSet<StudentModule> newModules = new HashSet<>(
                    (modules.size() + accumulator.modules.size()) * 2);
            newModules.addAll(modules);
            newModules.addAll(accumulator.modules);
            int newGradesAdded = gradesAdded + accumulator.gradesAdded;
            int newGradeSum = gradeSum + accumulator.gradeSum;

            return new StudentGradeAccumulator(newGradesAdded, newGradeSum, newModules);
        }

        public StudentGradeResult build() {
            double averageMark = (double) gradeSum / gradesAdded;
            Grade grade = gradesAdded == modules.size()
                    ? calculateGrade(averageMark)
                    : null;

            return new StudentGradeResult(averageMark, grade);
        }
    }

    @Override
    public Supplier<StudentGradeAccumulator> supplier() {
        return StudentGradeAccumulator::new;
    }

    @Override
    public BiConsumer<StudentGradeAccumulator, StudentModule> accumulator() {
        return StudentGradeAccumulator::add;
    }

    @Override
    public BinaryOperator<StudentGradeAccumulator> combiner() {
        return StudentGradeAccumulator::combine;
    }

    @Override
    public Function<StudentGradeAccumulator, StudentGradeResult> finisher() {
        return StudentGradeAccumulator::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }
}
