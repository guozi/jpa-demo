package com.cy.spring.jpa.test;

import com.cy.spring.jpa.model.Person;
import com.cy.spring.jpa.service.PersonService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;


/**
 * @author yun.chen
 * @create 2015-11-22 20:04
 */
public class JpaTest {

    private ApplicationContext applicationContext = null;

    private PersonService personService;

    {
        applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        personService = applicationContext.getBean(PersonService.class);
    }

    @Test
    public void testPersonSave() {
        Person p1 = new Person();
        p1.setEmail("AA@sohu.com");
        p1.setAge(20);
        p1.setLastName("AA");

        Person p2 = new Person();
        p2.setEmail("BB@sohu.com");
        p2.setAge(23);
        p2.setLastName("BB");

        personService.save(p1,p2);
    }
    @Test
    public void testDataSource() throws SQLException {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        System.out.println(dataSource.getConnection());
    }
}
