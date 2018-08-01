package ru.shishmakov.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shishmakov.model.Account;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test JPA layer without Web
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void findAllShouldReturnAllAccounts() {
        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts)
                .isNotNull()
                .asList().isNotEmpty()
                .size().isEqualTo(3);
    }

    @Test
    public void findByAccNumberShouldReturnAccountIfAvailable() {
        Optional<Account> account = accountRepository.findByAccNumber(1001L);

        assertThat(account)
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    public void findByAccNumberShouldNotReturnAccountIfNotAvailable() {
        Optional<Account> account = accountRepository.findByAccNumber(1999L);

        assertThat(account)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void findByAccNumberAndLockShouldReturnAccountIfAvailable() {
        Optional<Account> account = accountRepository.findByAccNumberAndLock(1001L);

        assertThat(account)
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    public void findByAccNumberAndLockShouldNotReturnAccountIfNotAvailable() {
        Optional<Account> account = accountRepository.findByAccNumberAndLock(1999L);

        assertThat(account)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void saveShouldPersistAccountObjectIfValid() {
        Account blankAccount = Account.builder().accNumber(1999L).amount(new BigDecimal("1.0")).build();
        Account saved = accountRepository.save(Account.builder().accNumber(1999L).amount(new BigDecimal("1.0")).build());

        assertThat(blankAccount)
                .isNotNull()
                .matches(a -> isNull(a.getId()));
        assertThat(saved)
                .isNotNull()
                .isNotEqualTo(blankAccount)
                .matches(a -> nonNull(a.getId()));
    }

    @Test
    public void saveShouldNotPersistAccountObjectIfNotValid() {
        assertThatThrownBy(() -> accountRepository.save(Account.builder().accNumber(1999L).amount(new BigDecimal("-1.0")).build()))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must be greater than or equal to 0");
    }
}
