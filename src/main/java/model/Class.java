package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "classes")
public class Class extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
    
    @Column(name = "class_name", nullable = false)
    private String className;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;
    
    private String description;
    
    @Column(name = "study_materials", columnDefinition = "json")
    private String studyMaterials;
    
    private BigDecimal tuition;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "tuition_due_date")
    private Date tuitionDueDate;

    @Column(name = "final_term_weight")
    private Float finalTermWeight;
    
    @Column(name = "absence_warning_threshold")
    private Integer absenceWarningThreshold;
    
    @Column(name = "absence_limit")
    private Integer absenceLimit;
    
    @OneToOne(mappedBy = "classEntity")
    private ClassSchedule schedules;
    
    @OneToMany(mappedBy = "classEntity")
    private List<ClassStudent> students;
    
    @OneToMany(mappedBy = "classEntity")
    private List<Attendance> attendances;
    
    @OneToMany(mappedBy = "classEntity")
    private List<Examination> examinations;
}
