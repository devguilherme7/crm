package crm.repository;

import jakarta.enterprise.context.ApplicationScoped;
import crm.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class JpaUserRepository implements UserRepository, PanacheRepository<User> {

    @Override
    public void save(User user) {
        persist(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }
}
