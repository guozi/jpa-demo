package com.cy.jpa.run;

import com.cy.jpa.model.Customer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;

/**
 * @author yun.chen
 * @create 2015-11-21 17:56
 */
public class Main {

    public static void main(String[] args) {
        //1.创建 EntityManagerFactory
        String persistenceUnitName = "jpa-1";
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);

        //2.创建 EntityManager，类似于Hibernate的SessionFactory
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        //3.开启事物
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        //4.进行持久化操作
        Customer customer = new Customer();
        customer.setAge(18);
        customer.setLastName("Tom");
        customer.setEmail("tom@google.com");
        customer.setCreatedDate(new Date());
        customer.setBirth(new Date());

        entityManager.persist(customer);

        //5.提交事物
        transaction.commit();

        //6.关闭EntityManager
        entityManager.close();

        //7.关闭EntityManagerFactory
        entityManagerFactory.close();
    }
}
