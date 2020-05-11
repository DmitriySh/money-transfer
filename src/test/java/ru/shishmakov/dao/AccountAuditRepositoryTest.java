package ru.shishmakov.dao;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shishmakov.persistence.entity.AccountAudit;
import ru.shishmakov.persistence.repository.AccountAuditRepository;


import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test JPA layer without Web
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountAuditRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @Autowired
    private AccountAuditRepository accountAuditRepository;

    @Test
    public void saveShouldPersistLogRecordIfValid() {
        AccountAudit blankAccountAudit = AccountAudit.builder().toNumber(1001L).amount(new BigDecimal("1.0")).description("deposit").build();
        AccountAudit saved = accountAuditRepository.save(AccountAudit.builder().toNumber(1001L).amount(new BigDecimal("1.0")).description("deposit").build());

        assertThat(blankAccountAudit)
                .isNotNull()
                .matches(a -> isNull(a.getId()))
                .matches(a -> isNull(a.getCreatedTime()));
        assertThat(saved)
                .isNotNull()
                .isNotEqualTo(blankAccountAudit)
                .matches(a -> nonNull(a.getId()))
                .matches(a -> nonNull(a.getCreatedTime()));
    }

    @Test
    public void saveShouldNotPersistLogRecordIfNotValid() {
        assertThatThrownBy(() -> accountAuditRepository.save(AccountAudit.builder().toNumber(1001L).amount(new BigDecimal("-1.0")).description("deposit").build()))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must be greater than or equal to 0");
    }

    @Test
    public void findAllShouldReturnAllLogRecords() {
        accountAuditRepository.save(AccountAudit.builder().toNumber(1001L).amount(new BigDecimal("1.0")).description("deposit").build());
        accountAuditRepository.save(AccountAudit.builder().fromNumber(1001L).amount(new BigDecimal("1.0")).description("withdraw").build());
        List<AccountAudit> accountAudits = accountAuditRepository.findAll();

        assertThat(accountAudits)
                .isNotNull()
                .asList().isNotEmpty()
                .hasSize(2);
    }
}
