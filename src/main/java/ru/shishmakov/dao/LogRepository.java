package ru.shishmakov.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shishmakov.model.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

}
