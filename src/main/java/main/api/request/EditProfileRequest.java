package main.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileRequest {
    private String photo;
    private boolean removePhoto;
    private String name;
    private String email;
    private String password;
}
