package py.com.seto.google.auth;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Table(name = "users")
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String profilePicture;

}