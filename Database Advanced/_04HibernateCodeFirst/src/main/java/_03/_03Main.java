package _03;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

public class _03Main {
    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("CodeFirst");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Student student = new Student("Student", "Student",
                "0885", 6.00, 10);

        Teacher teacher = new Teacher("Teacher", "Teacher",
                "0885", 15.0);

        Course course = new Course("DB", "This is a DB course.",
                LocalDate.now(), LocalDate.now(), 30);

        student.addCourse(course);
        course.addStudent(student);
        course.setTeacher(teacher);
        teacher.addCourse(course);

        entityManager.persist(student);
        entityManager.persist(teacher);
        entityManager.persist(course);

        entityManager.getTransaction().commit();
        entityManager.close();

    }
}
