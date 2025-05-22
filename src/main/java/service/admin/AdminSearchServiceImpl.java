package service.admin;

import dto.response.admin.SearchTeacherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminSearchServiceImpl {

    private final UserRepository userRepository;

    public List<SearchTeacherResponse> searchTeacher(String query) {
        return userRepository.searchByFullNameAndEmail(query);
    }
}
