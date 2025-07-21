package crm.repository;

import java.util.Optional;
import crm.data.RegistrationSession;

public interface RegistrationSessionRepository {

    void save(RegistrationSession session);

    Optional<RegistrationSession> findById(String sessionId);

    Optional<RegistrationSession> findByEmail(String email);

    void delete(String sessionId);
}
