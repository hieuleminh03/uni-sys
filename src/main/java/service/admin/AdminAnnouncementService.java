package service.admin;

import dto.request.admin.CreateAnnouncementRequest;
import dto.request.admin.UpdateAnnouncementRequest;
import dto.response.BaseResponse;
import dto.response.admin.AnnouncementDetailResponse;
import dto.response.admin.AnnouncementListResponse;

import java.util.List;

public interface AdminAnnouncementService {
    
    /**
     * Get announcement by ID
     * @param id Announcement ID
     * @return Announcement detail response
     */
    BaseResponse<AnnouncementDetailResponse> getAnnouncementById(Long id);
    
    /**
     * Get all announcements with pagination
     * @param page Page number
     * @param size Page size
     * @return List of announcement list responses
     */
    BaseResponse<List<AnnouncementListResponse>> getAllAnnouncements(int page, int size);
    
    /**
     * Create a new announcement
     * @param request Create announcement request
     * @return Success message
     */
    BaseResponse<String> createAnnouncement(CreateAnnouncementRequest request);
    
    /**
     * Update an existing announcement
     * @param request Update announcement request
     * @return Success message
     */
    BaseResponse<String> updateAnnouncement(UpdateAnnouncementRequest request);
    
    /**
     * Delete an announcement by ID
     * @param id Announcement ID
     * @return Success message
     */
    BaseResponse<String> deleteAnnouncement(Long id);
}