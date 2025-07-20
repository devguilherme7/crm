package crm.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record StartRegistrationRequest(@NotBlank @Email String email) {

}
