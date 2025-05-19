package service.admin;

import dto.request.admin.CreateAnnouncementRequest;
import dto.request.admin.UpdateAnnouncementRequest;
import dto.response.BaseResponse;
import dto.response.admin.AnnouncementDetailResponse;
import dto.response.admin.AnnouncementListResponse;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Account;
import model.Admin;
import model.Announcement;
import model.User;
import model.enums.AnnouncementTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AccountRepository;
import repository.AdminRepository;
import repository.AnnouncementRepository;
import repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminAnnouncementServiceImpl implements AdminAnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<AnnouncementDetailResponse> getAnnouncementById(Long id) {
        try {
            Announcement announcement = announcementRepository.findByIdWithAdmin(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + id));
            
            AnnouncementDetailResponse response = mapToDetailResponse(announcement);
            return BaseResponse.ok(response, "Announcement retrieved successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Announcement not found", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to retrieve announcement", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to retrieve announcement", 
                e, 
                null
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<List<AnnouncementListResponse>> getAllAnnouncements(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Announcement> announcementPage = announcementRepository.findAll(pageable);
            
            if (announcementPage.isEmpty()) {
                return BaseResponse.ok(
                    List.of(),
                    "No announcements found",
                    new util.Paging(page, size, 0, 0)
                );
            }
            
            // Extract IDs for eager fetching
            List<Long> announcementIds = announcementPage.getContent().stream()
                .map(Announcement::getId)
                .collect(Collectors.toList());
            
            // Fetch full entities with eager loading
            List<Announcement> announcements = announcementRepository.findAllWithAdminByIds(announcementIds);
            
            List<AnnouncementListResponse> responseList = announcements.stream()
                .map(this::mapToListResponse)
                .collect(Collectors.toList());
            
            return BaseResponse.ok(
                responseList,
                "Announcements retrieved successfully",
                new util.Paging(
                    page,
                    size,
                    announcementPage.getTotalElements(),
                    announcementPage.getTotalPages()
                )
            );
        } catch (Exception e) {
            log.error("Failed to retrieve announcements", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to retrieve announcements", 
                e, 
                null
            );
        }
    }

    @Override
    @Transactional
    public BaseResponse<String> createAnnouncement(CreateAnnouncementRequest request) {
        try {
            // Get the authenticated admin
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = accountRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            
            User user = userRepository.findByAccount(account)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
            Admin admin = adminRepository.findByUser(user)
                    .orElseThrow(() -> new ResourceNotFoundException("Admin not found for authenticated user"));
            
            // Create new announcement
            Announcement announcement = Announcement.builder()
                .admin(admin)
                .title(request.getTitle())
                .content(request.getContent())
                .date(request.getDate())
                .target(request.getTarget())
                .tags(request.getTags())
                .build();
            
            announcementRepository.save(announcement);
            
            log.info("Created new announcement with title: {}, target: {}", request.getTitle(), request.getTarget());
            
            return BaseResponse.created(null, "Announcement created successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found when creating announcement", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to create announcement", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to create announcement", 
                e, 
                null
            );
        }
    }

    @Override
    @Transactional
    public BaseResponse<String> updateAnnouncement(UpdateAnnouncementRequest request) {
        try {
            // Find the announcement by ID
            Announcement announcement = announcementRepository.findById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + request.getId()));
            
            // Update the announcement fields
            announcement.setTitle(request.getTitle());
            announcement.setContent(request.getContent());
            announcement.setDate(request.getDate());
            announcement.setTarget(request.getTarget());
            announcement.setTags(request.getTags());
            
            announcementRepository.save(announcement);
            
            log.info("Updated announcement with id: {}", request.getId());
            
            return BaseResponse.accepted(null, "Announcement updated successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Announcement not found for update", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to update announcement", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to update announcement", 
                e, 
                null
            );
        }
    }

    @Override
    @Transactional
    public BaseResponse<String> deleteAnnouncement(Long id) {
        try {
            // Find the announcement by ID
            if (!announcementRepository.existsById(id)) {
                throw new ResourceNotFoundException("Announcement not found with id: " + id);
            }
            
            // Delete the announcement
            announcementRepository.deleteById(id);
            
            log.info("Deleted announcement with id: {}", id);
            
            return BaseResponse.accepted(null, "Announcement deleted successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Announcement not found for deletion", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to delete announcement", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to delete announcement", 
                e, 
                null
            );
        }
    }
    
    /**
     * Maps an Announcement entity to AnnouncementDetailResponse
     */
    private AnnouncementDetailResponse mapToDetailResponse(Announcement announcement) {
        return AnnouncementDetailResponse.builder()
            .id(announcement.getId())
            .title(announcement.getTitle())
            .content(announcement.getContent())
            .target(announcement.getTarget())
            .targetName(getTargetName(announcement.getTarget()))
            .tags(announcement.getTags())
            .date(announcement.getDate())
            .createdAt(announcement.getCreatedAt())
            .updatedAt(announcement.getUpdatedAt())
            .adminId(announcement.getAdmin().getId())
            .adminName(announcement.getAdmin().getUser().getFullName())
            .build();
    }
    
    /**
     * Maps an Announcement entity to AnnouncementListResponse with truncated content
     */
    private AnnouncementListResponse mapToListResponse(Announcement announcement) {
        String contentPreview = announcement.getContent();
        // Truncate content if it's too long for preview
        if (contentPreview != null && contentPreview.length() > 30) {
            contentPreview = contentPreview.substring(0, 30) + "...";
        }
        
        return AnnouncementListResponse.builder()
            .id(announcement.getId())
            .title(announcement.getTitle())
            .contentPreview(contentPreview)
            .target(announcement.getTarget())
            .targetName(getTargetName(announcement.getTarget()))
            .tags(announcement.getTags())
            .date(announcement.getDate())
            .adminName(announcement.getAdmin().getUser().getFullName())
            .build();
    }
    
    /**
     * Converts AnnouncementTarget enum to human-readable string
     */
    private String getTargetName(AnnouncementTarget target) {
        if (target == null) {
            return "";
        }
        
        switch (target) {
            case STUDENT:
                return "Students";
            case TEACHER:
                return "Teachers";
            case ADMIN:
                return "Administrators";
            default:
                return target.toString();
        }
    }
}