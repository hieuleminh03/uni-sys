package service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.TuitionRecord;
import model.enums.TuitionStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import repository.TuitionRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TuitionServiceImpl {
    private final TuitionRepository tuitionRepo;
    @Scheduled(cron = "0 0 0 * * *")
    public void updateOverdueTuitions() {
        List<TuitionRecord> overdueTuitions = tuitionRepo.findByTuitionDateBeforeAndTuitionStatusProcessing(
                new Date(), TuitionStatus.PROCESSING
        );
        for (TuitionRecord record : overdueTuitions) {
            record.setStatus(TuitionStatus.UNPAID);
        }
        tuitionRepo.saveAll(overdueTuitions);
    }
}
