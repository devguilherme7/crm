package crm.data;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class RegistrationSession {

    private final String id;
    private final String email;
    private String verificationCode;
    private Instant codeExpiresAt;

    public RegistrationSession(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public void setVerificationCode(String verificationCode, Instant expiresAt) {
        this.verificationCode = verificationCode;
        this.codeExpiresAt = expiresAt;
    }

    public Map<String, String> toHashMap() {
        Map<String, String> hash = new HashMap<>();
        hash.put("sessionId", id);
        hash.put("email", email);
        hash.put("verificationCode", verificationCode);
        hash.put("codeExpiresAt", codeExpiresAt.toString());
        return hash;
    }

    public static RegistrationSession fromHashMap(Map<String, String> hash) {
        var session = new RegistrationSession(hash.get("sessionId"), hash.get("email"));
        session.verificationCode = hash.get("verificationCode");
        session.codeExpiresAt = Instant.parse(hash.get("codeExpiresAt"));
        return session;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
