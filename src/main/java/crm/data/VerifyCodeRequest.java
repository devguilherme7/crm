package crm.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyCodeRequest(@NotBlank @Pattern(regexp = "\\d{6}") String verificationCode) {

}
