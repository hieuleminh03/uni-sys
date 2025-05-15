package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_information")
public class StudentInformation extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    private String ethicity;
    
    @Column(name = "id_card_number")
    private String idCardNumber;
    
    @Column(name = "id_card_place_of_issue")
    private String idCardPlaceOfIssue;
    
    private String residence;
    
    private String address;
    
    private String religion;
    
    @Column(name = "mother_name")
    private String motherName;
    
    @Column(name = "mother_yob")
    private Integer motherYob;
    
    @Column(name = "mother_phone")
    private String motherPhone;
    
    @Column(name = "mother_mail")
    private String motherMail;
    
    @Column(name = "mother_occupation")
    private String motherOccupation;
    
    @Column(name = "father_name")
    private String fatherName;
    
    @Column(name = "father_yob")
    private Integer fatherYob;
    
    @Column(name = "father_phone")
    private String fatherPhone;
    
    @Column(name = "father_mail")
    private String fatherMail;
    
    @Column(name = "father_occupation")
    private String fatherOccupation;
    
    @Column(columnDefinition = "json")
    private String notes;
}
