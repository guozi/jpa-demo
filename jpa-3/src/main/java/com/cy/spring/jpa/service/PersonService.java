package com.cy.spring.jpa.service;

import com.cy.spring.jpa.dao.PersonDao;
import com.cy.spring.jpa.model.Person;
import com.cy.spring.jpa.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yun.chen
 * @create 2015-11-22 21:04
 */
@Service
@Transactional
public class PersonService {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private PersonRepository personRepository;

    public void save(Person p1,Person p2) {
        personDao.save(p1,p2);
    }

    public void updateEmailById(String email,Integer id) {
        personRepository.updateEmailById(id,email);
    }

    public void savePersons(List<Person> personList) {
        personRepository.save(personList);
    }
}
