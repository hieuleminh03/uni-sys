package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.GradeStatus;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_examination")
public class StudentExamination extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examination_id", nullable = false)
    private Examination examination;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_student_id", nullable = false)
    private ClassStudent classStudent;

    @Column()
    private Double grade;

    @Column(nullable = false)
    private boolean isAbsent;
}
