package ru.shishmakov.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.model.Log;

@Repository
@Transactional(readOnly = true)
public interface LogRepository extends JpaRepository<Log, Long> {

}
