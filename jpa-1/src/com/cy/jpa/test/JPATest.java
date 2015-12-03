package com.cy.jpa.test;

import com.cy.jpa.model.Customer;
import com.cy.jpa.model.Order;
import org.hibernate.jpa.QueryHints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.QueryHint;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author yun.chen
 * @create 2015-11-21 22:16
 */
public class JPATest {

    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    private EntityTransaction transaction;

    @Before
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-1");
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();
    }

    @After
    public void destroy() {
        transaction.commit();
        entityManager.close();
        entityManagerFactory.close();
    }

    //like hibernate session get
    @Test
    public void testFind() {
        Customer customer = entityManager.find(Customer.class, 1);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(customer);
    }

    //like hibernate session load
    @Test
    public void testReference() {
        Customer customer = entityManager.getReference(Customer.class, 1);
        System.out.println(customer.getClass().getName());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(customer);
    }

    //like Hibernate save，使对象由临时状态变为持久化状态
    @Test
    public void testPersistence() {
        Customer customer = new Customer();
        customer.setBirth(new Date());
        customer.setAge(20);
        customer.setCreatedDate(new Date());
        customer.setEmail("guozi@google.com");
        customer.setLastName("guozi");

        entityManager.persist(customer);
        System.out.println(customer.getId());
    }

    //like hibernate delete,只能移除持久化的对象
    @Test
    public void testRemove() {
//        Customer customer = new Customer();
//        customer.setId(2);

        Customer customer = entityManager.find(Customer.class, 2);

        entityManager.remove(customer);
    }

    //like hibernate saveOrUpdate
    @Test
    public void testMerge1() {
        Customer customer = new Customer();
        customer.setCreatedDate(new Date());
        customer.setAge(10);
        customer.setBirth(new Date());
        customer.setLastName("Jake");
        customer.setEmail("jake@sohu.com");

        Customer customer1 = entityManager.merge(customer);

        System.out.println("custome#id:" + customer.getId());
        System.out.println("custome1#id:" + customer1.getId());
    }

    @Test
    public void testMerge2() {
        Customer customer = new Customer();
        customer.setCreatedDate(new Date());
        customer.setAge(25);
        customer.setBirth(new Date());
        customer.setLastName("Lily");
        customer.setEmail("Lily@sohu.com");
        customer.setId(20);

        Customer customer1 = entityManager.merge(customer);

        System.out.println("customer#id:" + customer.getId());
        System.out.println("customer1#id:" + customer1.getId());
    }

    @Test
    public void testMerge3() {
        Customer customer = new Customer();
        customer.setCreatedDate(new Date());
        customer.setAge(25);
        customer.setBirth(new Date());
        customer.setLastName("Mike");
        customer.setEmail("Mike@sohu.com");
        customer.setId(4);

        Customer customer1 = entityManager.merge(customer);

        System.out.println("customer#id:" + customer.getId());
        System.out.println("customer1#id:" + customer1.getId());
    }

    @Test
    public void testMerge4() {
        Customer customer = new Customer();
        customer.setCreatedDate(new Date());
        customer.setAge(25);
        customer.setBirth(new Date());
        customer.setLastName("Mikasa");
        customer.setEmail("Mikasa@sohu.com");
        customer.setId(4);

        Customer customer1 = entityManager.find(Customer.class, 4);
        entityManager.merge(customer);

        System.out.println("customer#id:" + customer.getId());
        System.out.println("customer1#id:" + customer1.getId());
    }

    /*
    @Test
    public void testManyToOnePersist() {
        Customer customer = new Customer();
        customer.setCreatedDate(new Date());
        customer.setAge(30);
        customer.setBirth(new Date());
        customer.setLastName("Amo");
        customer.setEmail("Amo@sohu.com");

        Order order1 = new Order();
        order1.setOrderName("O-FF-1");

        Order order2 = new Order();
        order2.setOrderName("O-FF-2");

        order1.setCustomer(customer);
        order2.setCustomer(customer);

        //先保存1的一端，再保存n的一端
        entityManager.persist(customer);
        entityManager.persist(order1);
        entityManager.persist(order2);
    }

    @Test
    public void testManyToOneFind() {
        Order order = entityManager.find(Order.class, 3);
        System.out.println("===>>>"+order.getOrderName());
        System.out.println("===>>>"+order.getCustomer().getLastName());
    }

    @Test
    public void testManyToOneRemove() {
        Order order = entityManager.find(Order.class, 3);
        entityManager.remove(order);

        //如果删除有外键关联的1的一端会报错
    }
    */

    @Test
    public void testOneToMany() {
        Customer customer = new Customer();
        customer.setCreatedDate(new Date());
        customer.setAge(30);
        customer.setBirth(new Date());
        customer.setLastName("Jake");
        customer.setEmail("Jake@sohu.com");

        Order order1 = new Order();
        order1.setOrderName("O-DD-1");

        Order order2 = new Order();
        order2.setOrderName("O-DD-2");

        order1.setCustomer(customer);
        order2.setCustomer(customer);

        customer.getOrderSet().add(order1);
        customer.getOrderSet().add(order2);

        entityManager.persist(customer);
        entityManager.persist(order1);
        entityManager.persist(order2);

    }

    //测试二级缓存
    @Test
    public void testSecondLevelCache() {
        Customer customer1 = entityManager.find(Customer.class, 1);

        transaction.commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();

        Customer customer2 = entityManager.find(Customer.class, 1);

    }


    //Test JPQL

    @Test
    public void testHelloJPQL() {
        String jpql = "FROM Customer a WHERE a.id > ?1";
        Query query = entityManager.createQuery(jpql);
        query.setParameter( 1, 1 );
        List resultList = query.getResultList();
        System.out.println(resultList);
    }

    @Test
    public void testPartlyProperties() {
//        String jpql = "SELECT a.age,a.lastName FROM Customer a WHERE a.id > ?1";
        String jpql = "SELECT new Customer(a.age,a.lastName) FROM Customer a WHERE a.id > ?1";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,3);
        List resultList = query.getResultList();
        System.out.println(resultList);
    }


    @Test
    public void testNamedQuery() {
        Query namedQuery = entityManager.createNamedQuery("testNamedQuery");

        namedQuery.setParameter(1,18);

        Customer customer = (Customer) namedQuery.getSingleResult();
        System.out.println(customer);
    }

    @Test
    public void testNativeQuery() {
        String jpql = "select a.email from jpa_customer a where a.age = ?1";
        Query query = entityManager.createNativeQuery(jpql).setParameter(1,18);
        String email = (String) query.getSingleResult();
        System.out.println(email);
    }

    @Test
    public void testQueryCache() {
        String jpql = "FROM Customer a";
        Query query = entityManager.createQuery(jpql);
        query.setHint(QueryHints.HINT_CACHEABLE,true);

        List resultList = query.getResultList();
        System.out.println(resultList);

        List resultList1 = query.getResultList();
        System.out.println(resultList1);
    }

    //注意这里使用FETCH和不使用的区别，建议加上FETCH
    @Test
    public void testLeftOutJoinFetch() {
        String jpql = "FROM Customer c LEFT OUTER JOIN FETCH c.orderSet where c.id = ?1";
        Customer customer = (Customer) entityManager.createQuery(jpql).setParameter(1, 8).getSingleResult();

        System.out.println(customer.getLastAndEmail());
        System.out.println(customer.getOrderSet().size());
    }


    @Test
    public void testLike() {
        String jpql = "FROM Customer a where a.email like :email";
        List resultList = entityManager.createQuery(jpql).setParameter("email", "tom%").getResultList();
        System.out.println(resultList);
    }

    @Test
    public void testIn() {
        String jpql = "FROM Customer a where a.age in :ages";
        List resultList = entityManager.createQuery(jpql).setParameter("ages", Arrays.asList(10,18)).getResultList();
        System.out.println(resultList);
    }

    @Test
    public void testUpdate() {
        String jpql = "update Customer a set a.email = 'mushi@sohu.com' where a.age = :age";
        entityManager.createQuery(jpql).setParameter("age", 10).executeUpdate();
    }
}
