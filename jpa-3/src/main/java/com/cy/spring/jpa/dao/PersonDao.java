package com.cy.spring.jpa.dao;

import com.cy.spring.jpa.model.Person;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author yun.chen
 * @create 2015-11-22 21:01
 */
@Repository
public class PersonDao {

    //获取和当前事物关联的EntityManager
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 保存对象
     *
     * @param p1
     * @param p2
     */
    public void save(Person p1,Person p2) {
        entityManager.persist(p1);
        entityManager.persist(p2);
    }
}
