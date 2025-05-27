package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.AttendanceStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendance_hist")
public class AttendanceHistory extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", nullable = false)
    private Attendance attendance;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_student_id", nullable = false)
    private ClassStudent classStudent;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;
}
