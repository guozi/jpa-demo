package com.cy.spring.jpa.service;

import com.cy.spring.jpa.dao.PersonDao;
import com.cy.spring.jpa.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yun.chen
 * @create 2015-11-22 21:04
 */
@Service
public class PersonService {

    @Autowired
    private PersonDao personDao;

    @Transactional
    public void save(Person p1,Person p2) {
        personDao.save(p1,p2);
    }
}
