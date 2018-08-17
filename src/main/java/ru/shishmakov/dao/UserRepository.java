package ru.shishmakov.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shishmakov.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
