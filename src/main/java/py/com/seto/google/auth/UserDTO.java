package py.com.seto.google.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserDTO {

    private String givenName;
    private String famiyName;
    private String email;
    private String profilePicture;


}
