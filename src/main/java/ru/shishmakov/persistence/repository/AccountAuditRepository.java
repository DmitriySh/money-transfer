package ru.shishmakov.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shishmakov.persistence.entity.AccountAudit;

@Repository
public interface AccountAuditRepository extends JpaRepository<AccountAudit, Long> {

}
