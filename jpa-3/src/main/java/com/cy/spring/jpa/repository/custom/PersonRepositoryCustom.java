package com.cy.spring.jpa.repository.custom;

import com.cy.spring.jpa.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Person的自定义的Repository
 *
 * Created by yun.chen on 2015/11/28.
 */
public interface PersonRepositoryCustom {

    Person test(Integer id);

    void reindex() throws InterruptedException;

    List<Person> search(String text);
}
