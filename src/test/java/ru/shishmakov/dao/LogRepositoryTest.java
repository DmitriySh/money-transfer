package ru.shishmakov.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shishmakov.model.Log;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.List;

import static java.util.Objects.isNull;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test JPA layer without Web
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class LogRepositoryTest {
    @Autowired
    private LogRepository logRepository;

    @Test
    public void saveShouldPersistLogRecordIfValid() {
        Log blankLog = Log.builder().toNumber(1001L).amount(new BigDecimal("1.0")).description("deposit").build();
        Log saved = logRepository.save(Log.builder().toNumber(1001L).amount(new BigDecimal("1.0")).description("deposit").build());

        assertThat(blankLog)
                .isNotNull()
                .matches(a -> isNull(a.getId()))
                .matches(a -> isNull(a.getDate()));
        assertThat(saved)
                .isNotNull()
                .isNotEqualTo(blankLog)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    public void saveShouldNotPersistLogRecordIfNotValid() {
        assertThatThrownBy(() -> logRepository.save(Log.builder().toNumber(1001L).amount(new BigDecimal("-1.0")).description("deposit").build()))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must be greater than or equal to 0");
    }

    @Test
    public void findAllShouldReturnAllLogRecords() {
        logRepository.save(Log.builder().toNumber(1001L).amount(new BigDecimal("1.0")).description("deposit").build());
        logRepository.save(Log.builder().fromNumber(1001L).amount(new BigDecimal("1.0")).description("withdraw").build());
        List<Log> logs = logRepository.findAll();

        assertThat(logs)
                .isNotNull()
                .asList().isNotEmpty()
                .size().isEqualTo(2);
    }
}
