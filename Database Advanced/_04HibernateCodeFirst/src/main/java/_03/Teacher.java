package _03;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "_03_teachers")
public class Teacher extends Person {

    @Column(name = "salary_per_hour", nullable = false)
    private double salaryPerHour;

    @OneToMany(mappedBy = "teacher", targetEntity = Course.class)
    private Set<Course> courses;

    public Teacher(String firstName, String lastName, String phoneNumber, double salaryPerHour) {
        super(firstName, lastName, phoneNumber);
        this.salaryPerHour = salaryPerHour;
        this.courses = new HashSet<>();
    }

    public Teacher() {}

    public Set<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public double getSalaryPerHour() {
        return salaryPerHour;
    }

    public void setSalaryPerHour(double salaryPerHour) {
        this.salaryPerHour = salaryPerHour;
    }
}
