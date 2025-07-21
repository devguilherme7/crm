package crm.data;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import jakarta.ws.rs.BadRequestException;

public class RegistrationSession {

    private final String id;
    private final String email;
    private boolean verified;
    private String verificationCode;
    private Instant codeExpiresAt;

    public RegistrationSession(String id, String email) {
        this.id = id;
        this.email = email;
        this.verified = false;
    }

    public void setVerificationCode(String verificationCode, Instant expiresAt) {
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
    }

    public Map<String, String> toHashMap() {
        Map<String, String> hash = new HashMap<>();
        hash.put("sessionId", id);
        hash.put("email", email);
        hash.put("verified", Boolean.toString(verified));
        hash.put("verificationCode", verificationCode);
        hash.put("codeExpiresAt", codeExpiresAt.toString());
        return hash;
    }

    public static RegistrationSession fromHashMap(Map<String, String> hash) {
        var session = new RegistrationSession(hash.get("sessionId"), hash.get("email"));
        session.verificationCode = hash.get("verificationCode");
        session.codeExpiresAt = Instant.parse(hash.get("codeExpiresAt"));
        session.verified = Boolean.parseBoolean(hash.get("verified"));
        return session;
    }

    public boolean isCodeExpired() {
        return Instant.now().isAfter(codeExpiresAt);
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
}
