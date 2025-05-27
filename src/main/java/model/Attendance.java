package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.AttendanceType;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendances")
public class Attendance extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class classEntity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceType type;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;
    
    @OneToMany(mappedBy = "attendance")
    private List<AttendanceHistory> attendanceHistories;
}
