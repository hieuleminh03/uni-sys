package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.ExaminationStatus;
import model.enums.ExaminationType;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "examinations")
public class Examination extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class classEntity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExaminationType type;

    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;
    
    @Column(columnDefinition = "json")
    private String notes;
    
    @OneToMany(mappedBy = "examination", cascade = CascadeType.REMOVE)
    private List<StudentExamination> studentExaminations;
}
