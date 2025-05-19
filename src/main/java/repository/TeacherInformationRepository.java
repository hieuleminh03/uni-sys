package repository;

import model.Teacher;
import model.TeacherInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherInformationRepository extends JpaRepository<TeacherInformation, Long> {
    Optional<TeacherInformation> findByTeacher(Teacher teacher);
}
