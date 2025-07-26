package crm.data;

import java.time.LocalDateTime;
import jakarta.ws.rs.BadRequestException;

@SuppressWarnings("unused")
public class RegistrationSession {

    private final String id;
    private final String email;
    private boolean verified;
    private String verificationCode;
    private LocalDateTime codeExpiresAt;
    private LocalDateTime emailVerifiedAt;

    protected RegistrationSession() {
        this.id = null;
        this.email = null;
    }

    public RegistrationSession(String id, String email) {
        this.id = id;
        this.email = email;
        this.verified = false;
    }

    public void setVerificationCode(String verificationCode, LocalDateTime expiresAt) {
        this.verificationCode = verificationCode;
        this.codeExpiresAt = expiresAt;
    }

    public void verifyEmail(String providedCode) {
        if (isVerified()) {
            throw new BadRequestException("E-mail já verificado.");
        }
        if (verificationCode == null || isCodeExpired()) {
            throw new BadRequestException("Código de verificação expirado. Por favor, solicite um novo.");
        }
        if (!verificationCode.equals(providedCode)) {
            throw new BadRequestException("Código de verificação inválido. Por favor, tente novamente.");
        }

        this.verified = true;
        this.emailVerifiedAt = LocalDateTime.now();
    }

    public boolean isCodeExpired() {
        return LocalDateTime.now().isAfter(codeExpiresAt);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public LocalDateTime getCodeExpiresAt() {
        return codeExpiresAt;
    }

    public LocalDateTime getEmailVerifiedAt() {
        return emailVerifiedAt;
    }
}
