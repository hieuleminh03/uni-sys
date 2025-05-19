package repository;

import model.Student;
import model.StudentInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentInformationRepository extends JpaRepository<StudentInformation, Long> {
    Optional<StudentInformation> findByStudent(Student student);
}
