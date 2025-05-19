package service.teacher;

import dto.response.BaseResponse;
import dto.response.teacher.TeacherAnnouncementDetailResponse;
import dto.response.teacher.TeacherAnnouncementListResponse;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AnnouncementRepository;
import util.Paging;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherAnnouncementService {
    
    private final AnnouncementRepository announcementRepository;
    
    /**
     * Get announcements for the current week
     * 
     * @return List of announcements for the current week
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<TeacherAnnouncementListResponse>> getAnnouncementsForCurrentWeek() {
        try {
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            Date startDate = calendar.getTime();

            calendar.add(Calendar.DAY_OF_WEEK, 6);
            Date endDate = calendar.getTime();
            List<Announcement> announcements = announcementRepository.findByWeekForTeachers(startDate, endDate);
            
            if (announcements.isEmpty()) {
                return BaseResponse.ok(List.of(), "No announcements found for this week");
            }
            
            List<TeacherAnnouncementListResponse> responseList = announcements.stream()
                .map(this::mapToListResponse)
                .collect(Collectors.toList());
            
            return BaseResponse.ok(responseList, "Weekly announcements retrieved successfully");
        } catch (Exception e) {
            log.error("Failed to retrieve weekly announcements for teachers", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to retrieve weekly announcements", 
                e, 
                null
            );
        }
    }
    
    /**
     * Get all announcements for teachers with pagination
     * 
     * @param page Page number
     * @param size Page size
     * @return Paginated list of announcements
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<TeacherAnnouncementListResponse>> getAllAnnouncements(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Announcement> announcementPage = announcementRepository.findAllForTeachers(pageable);
            
            if (announcementPage.isEmpty()) {
                return BaseResponse.ok(
                    List.of(),
                    "No announcements found",
                    new Paging(page, size, 0, 0)
                );
            }
            
            // Extract IDs for eager fetching
            List<Long> announcementIds = announcementPage.getContent().stream()
                .map(Announcement::getId)
                .collect(Collectors.toList());
            
            // Fetch full entities with eager loading
            List<Announcement> announcements = announcementRepository.findAllWithAdminByIds(announcementIds);
            
            List<TeacherAnnouncementListResponse> responseList = announcements.stream()
                .map(this::mapToListResponse)
                .collect(Collectors.toList());
            
            return BaseResponse.ok(
                responseList, 
                "Announcements retrieved successfully", 
                new Paging(
                    page, 
                    size, 
                    announcementPage.getTotalElements(), 
                    announcementPage.getTotalPages()
                )
            );
        } catch (Exception e) {
            log.error("Failed to retrieve announcements for teachers", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to retrieve announcements", 
                e, 
                null
            );
        }
    }
    
    /**
     * Get announcement details by ID
     * 
     * @param id Announcement ID
     * @return Announcement details
     */
    @Transactional(readOnly = true)
    public BaseResponse<TeacherAnnouncementDetailResponse> getAnnouncementById(Long id) {
        try {
            Announcement announcement = announcementRepository.findByIdForTeachers(id)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + id));
            
            announcement = announcementRepository.findByIdWithAdmin(id)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + id));
            
            TeacherAnnouncementDetailResponse response = mapToDetailResponse(announcement);
            return BaseResponse.ok(response, "Announcement retrieved successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Announcement not found", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to retrieve announcement for teachers", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to retrieve announcement", 
                e, 
                null
            );
        }
    }
    
    /**
     * Maps an Announcement entity to TeacherAnnouncementDetailResponse
     */
    private TeacherAnnouncementDetailResponse mapToDetailResponse(Announcement announcement) {
        return TeacherAnnouncementDetailResponse.builder()
            .id(announcement.getId())
            .title(announcement.getTitle())
            .content(announcement.getContent())
            .date(announcement.getDate())
            .createdAt(announcement.getCreatedAt())
            .updatedAt(announcement.getUpdatedAt())
            .adminName(announcement.getAdmin().getUser().getFullName())
            .tags(announcement.getTags())
            .build();
    }
    
    /**
     * Maps an Announcement entity to TeacherAnnouncementListResponse with truncated content
     */
    private TeacherAnnouncementListResponse mapToListResponse(Announcement announcement) {
        String contentPreview = announcement.getContent();
        // Truncate content if it's too long for preview
        if (contentPreview != null && contentPreview.length() > 30) {
            contentPreview = contentPreview.substring(0, 30) + "...";
        }
        
        return TeacherAnnouncementListResponse.builder()
            .id(announcement.getId())
            .title(announcement.getTitle())
            .contentPreview(contentPreview)
            .date(announcement.getDate())
            .createdAt(announcement.getCreatedAt())
            .adminName(announcement.getAdmin().getUser().getFullName())
            .tags(announcement.getTags())
            .build();
    }
}