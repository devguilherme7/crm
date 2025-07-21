package crm.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(
        @NotBlank @Size(min = 3, max = 50) String firstName,
        @NotBlank @Size(min = 3, max = 50) String lastName,
        @NotBlank String password
) {

}
