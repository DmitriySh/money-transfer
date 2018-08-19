package ru.shishmakov.dao;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shishmakov.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test JPA layer without Web
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveShouldPersistUser() {
        User blankUser = User.builder().username("user 1").password("password").roles(Set.of("ROLE_USER", "ROLE_ADMIN")).build();
        User saved = userRepository.save(User.builder().username("user 2").password("password").roles(Set.of("ROLE_USER", "ROLE_ADMIN")).build());

        assertThat(blankUser)
                .isNotNull()
                .matches(a -> isNull(a.getId()))
                .matches(a -> isNull(a.getCreateDate()));
        assertThat(saved)
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    public void findAllShouldReturnAllUsers() {
        List<User> users = userRepository.findAll();

        assertThat(users)
                .isNotNull()
                .asList().isNotEmpty()
                .hasSize(2);
    }

    @Test
    public void findByUsernameShouldReturnUser() {
        Optional<User> admin = userRepository.findByUsername("admin");

        assertThat(admin)
                .isNotNull()
                .isPresent();
    }
}
