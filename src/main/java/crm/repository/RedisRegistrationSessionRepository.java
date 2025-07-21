package crm.repository;

import java.util.Map;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import crm.data.RegistrationSession;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.redis.datasource.value.ValueCommands;

@ApplicationScoped
public class RedisRegistrationSessionRepository implements RegistrationSessionRepository {

    private static final String REGISTRATION_SESSION_KEY_PREFIX = "uac:session:";
    private static final String REGISTRATION_SESSION_EMAIL_KEY = "uac:email:";

    private final RedisDataSource redis;
    private final HashCommands<String, String, String> hash;
    private final ValueCommands<String, String> value;

    @Inject
    public RedisRegistrationSessionRepository(RedisDataSource redis) {
        this.redis = redis;
        this.hash = redis.hash(String.class);
        this.value = redis.value(String.class);
    }

    @Override
    public void save(RegistrationSession session) {
        String sessionKey = REGISTRATION_SESSION_KEY_PREFIX + session.getId();
        String emailIndexKey = REGISTRATION_SESSION_EMAIL_KEY + session.getEmail();

        hash.hset(sessionKey, session.toHashMap());
        value.set(emailIndexKey, session.getId());

        redis.key().expire(sessionKey, 3600);
        redis.key().expire(emailIndexKey, 3600);
    }

    @Override
    public Optional<RegistrationSession> findById(String sessionId) {
        String sessionKey = REGISTRATION_SESSION_KEY_PREFIX + sessionId;

        Map<String, String> data = hash.hgetall(sessionKey);
        if (data == null || data.isEmpty()) {
            return Optional.empty();
        }

        var session = RegistrationSession.fromHashMap(data);
        return Optional.of(session);
    }

    @Override
    public Optional<RegistrationSession> findByEmail(String email) {
        String emailIndexKey = REGISTRATION_SESSION_EMAIL_KEY + email;
        String sessionId = value.get(emailIndexKey);
        return findById(sessionId);
    }
}
