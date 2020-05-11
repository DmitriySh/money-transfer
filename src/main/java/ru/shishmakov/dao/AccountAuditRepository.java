package ru.shishmakov.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shishmakov.model.AccountAudit;

@Repository
public interface AccountAuditRepository extends JpaRepository<AccountAudit, Long> {

}
