package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.PaymentMethod;
import model.enums.TuitionStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tuition_records")
public class TuitionRecord extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_student_id", nullable = false)
    private ClassStudent classStudent;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TuitionStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) default 'CASH'")
    private PaymentMethod method;
}
