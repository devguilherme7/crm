package crm.repository;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import crm.data.RegistrationSession;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;

@ApplicationScoped
public class RedisRegistrationSessionRepository implements RegistrationSessionRepository {

    private static final String REGISTRATION_SESSION_KEY_PREFIX = "registration:session:";
    private static final String REGISTRATION_SESSION_EMAIL_KEY = "registration:email:";

    private final RedisDataSource redis;
    private final ValueCommands<String, RegistrationSession> sessionValueCommands;
    private final ValueCommands<String, String> emailIndexValueCommands;

    @Inject
    public RedisRegistrationSessionRepository(RedisDataSource redis) {
        this.redis = redis;
        this.emailIndexValueCommands = redis.value(String.class);
        this.sessionValueCommands = redis.value(RegistrationSession.class);
    }

    @Override
    public void save(RegistrationSession session) {
        String sessionKey = REGISTRATION_SESSION_KEY_PREFIX + session.getId();
        String emailIndexKey = REGISTRATION_SESSION_EMAIL_KEY + session.getEmail();

        sessionValueCommands.set(sessionKey, session);
        emailIndexValueCommands.set(emailIndexKey, session.getId());

        redis.key().expire(sessionKey, 3600);
        redis.key().expire(emailIndexKey, 3600);
    }

    @Override
    public Optional<RegistrationSession> findById(String sessionId) {
        String sessionKey = REGISTRATION_SESSION_KEY_PREFIX + sessionId;
        RegistrationSession session = sessionValueCommands.get(sessionKey);
        return Optional.ofNullable(session);
    }

    @Override
    public Optional<RegistrationSession> findByEmail(String email) {
        String emailIndexKey = REGISTRATION_SESSION_EMAIL_KEY + email;
        String sessionId = emailIndexValueCommands.get(emailIndexKey);
        if (sessionId == null) {
            return Optional.empty();
        }
        return findById(sessionId);
    }

    @Override
    public void delete(String sessionId) {
        String sessionKey = REGISTRATION_SESSION_KEY_PREFIX + sessionId;
        findById(sessionId).ifPresent(session -> {
            String emailIndexKey = REGISTRATION_SESSION_EMAIL_KEY + session.getEmail();
            redis.key().del(emailIndexKey);
            redis.key().del(sessionKey);
        });
    }
}
