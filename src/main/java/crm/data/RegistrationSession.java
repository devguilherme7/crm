package crm.data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import jakarta.ws.rs.BadRequestException;

public class RegistrationSession {

    private final String id;
    private final String email;
    private boolean verified;
    private String verificationCode;
    private LocalDateTime codeExpiresAt;
    private LocalDateTime emailVerifiedAt;

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

    public Map<String, String> toHashMap() {
        Map<String, String> hash = new HashMap<>();
        hash.put("sessionId", id);
        hash.put("email", email);
        hash.put("verified", Boolean.toString(verified));
        hash.put("verificationCode", verificationCode);
        hash.put("codeExpiresAt", codeExpiresAt.toString());
        hash.put("emailVerifiedAt", String.valueOf(emailVerifiedAt));
        return hash;
    }

    public static RegistrationSession fromHashMap(Map<String, String> hash) {
        var session = new RegistrationSession(hash.get("sessionId"), hash.get("email"));
        session.verificationCode = hash.get("verificationCode");

        String codeExpiresAtStr = hash.get("codeExpiresAt");
        if (codeExpiresAtStr != null && !codeExpiresAtStr.equals("null")) {
            session.codeExpiresAt = LocalDateTime.parse(codeExpiresAtStr);
        }

        session.verified = Boolean.parseBoolean(hash.get("verified"));

        String emailVerifiedAtStr = hash.get("emailVerifiedAt");
        if (emailVerifiedAtStr != null && !emailVerifiedAtStr.equals("null")) {
            session.emailVerifiedAt = LocalDateTime.parse(emailVerifiedAtStr);
        }

        return session;
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

    public LocalDateTime getEmailVerifiedAt() {
        return emailVerifiedAt;
    }
}
