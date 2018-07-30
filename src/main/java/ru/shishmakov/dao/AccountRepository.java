package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.model.Account;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class AccountRepository {
    @PersistenceContext
    private EntityManager em;

    public Account findById(long id) {
        return em.find(Account.class, id);
    }

    public List<Account> findAll() {
        return em.createQuery("SELECT a FROM Account a", Account.class)
                .getResultList();
    }
}
