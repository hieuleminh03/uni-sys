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
@Table(name = "teachers")
public class Teacher extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private TeacherInformation teacherInformation;
    
    @OneToMany(mappedBy = "teacher")
    private List<Class> classes;
    
    @OneToOne(mappedBy = "teacher")
    private Homeroom homeroom;
}
