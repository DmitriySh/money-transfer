package ru.shishmakov.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EntityScan("ru.shishmakov.persistence")
@EnableJpaAuditing
public class JpaConfiguration {

}
