package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.ClassStudentStatus;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "class_student")
public class ClassStudent extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class classEntity;
    
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassStudentStatus status;

    @Column(name = "midterm_grade")
    private Double midtermGrade;

    @Column(name = "final_exam_grade")
    private Double finalExamGrade;

    @Column(name = "final_grade")
    private Double finalGrade;

    @OneToOne(mappedBy = "classStudent" ,cascade = CascadeType.ALL)
    private TuitionRecord tuitionRecord;

    @OneToMany(mappedBy = "classStudent" , cascade = CascadeType.ALL)
    private List<StudentExamination> studentExaminations;

    @OneToMany(mappedBy = "classStudent" , cascade = CascadeType.ALL)
    private List<AttendanceHistory> attendanceHistories;
}
