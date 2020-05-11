package ru.shishmakov.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.shishmakov.persistence.entity.Account;


import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.accountNumber = :accNumber")
    Optional<Account> findByAccNumberAndLock(@Param("accNumber") long accountNumber);

    Optional<Account> findByAccountNumber(long accountNumber);
}
