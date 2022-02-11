package main.api.response.authCheckResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class AuthCheckResponse {

    private boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AuthCheckUser user;

}
