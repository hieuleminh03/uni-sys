package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.AnnouncementTarget;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "announcements")
public class Announcement extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, columnDefinition = "text")
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnnouncementTarget target;

}
