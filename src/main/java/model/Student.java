package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private StudentInformation studentInformation;
    
    @OneToMany(mappedBy = "student")
    private List<ClassStudent> classStudents;
    
    @OneToMany(mappedBy = "student")
    private List<HomeroomStudent> homeroomStudents;
}
