package service.admin;

import dto.request.admin.SearchSubjectResponse;
import dto.response.admin.SearchStudentResponse;
import dto.response.admin.SearchTeacherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Subject;
import org.springframework.stereotype.Service;
import repository.SubjectRepository;
import repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminSearchServiceImpl {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public List<SearchTeacherResponse> searchTeacher(String query) {
        return userRepository.searchTeacherByFullNameAndEmail(query);
    }

    public List<SearchStudentResponse> searchStudent(String query) {
        return userRepository.searchStudentByFullNameAndEmail(query);
    }

    public List<SearchSubjectResponse> searchSubject(String query) {
        List<Subject> subjects=subjectRepository.findByNameContains(query);
        return subjects.stream().map(
                subject -> SearchSubjectResponse.builder()
                        .subjectId(subject.getId())
                        .subjectName(subject.getName())
                        .subjectCode(subject.getCode())
                        .build()
        ).toList();
    }
}
