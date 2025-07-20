package crm.repository;

import java.util.Optional;
import crm.data.RegistrationSession;

public interface RegistrationSessionRepository {

    void save(RegistrationSession session);

    Optional<RegistrationSession> findBySessionId(String sessionId);

    Optional<RegistrationSession> findByEmail(String email);
}
