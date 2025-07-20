package crm.repository;

import crm.entity.User;

public interface UserRepository {

    void save(User user);

    boolean existsByEmail(String email);
}
