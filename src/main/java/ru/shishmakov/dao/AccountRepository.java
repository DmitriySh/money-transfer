package ru.shishmakov.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.model.Account;

import java.util.Optional;

import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

@Repository
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.accNumber = :accNumber")
    Optional<Account> findByAccNumberAndLock(@Param("accNumber") long accNumber);

    Optional<Account> findByAccNumber(long accNumber);
}
