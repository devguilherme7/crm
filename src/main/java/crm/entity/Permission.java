package crm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "permissions", uniqueConstraints = {
        @UniqueConstraint(name = "UK_permissions_code", columnNames = "code")
})
public class Permission extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String code;

    protected Permission() {
        //
    }
}
