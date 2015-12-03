package com.cy.spring.jpa.repository;

import com.cy.spring.jpa.model.Person;
import com.cy.spring.jpa.repository.custom.PersonRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author yun.chen
 * @create 2015-11-28 11:45
 */
//@RepositoryDefinition(domainClass = Person.class,idClass = Integer.class)
public interface PersonRepository extends JpaRepository<Person, Integer>, PersonRepositoryCustom, JpaSpecificationExecutor<Person> {

    /**
     * 根据lastName 获取Person
     *
     * @param lastName
     * @return
     */
    Person findByLastName(String lastName);

    List<Person> getByLastNameStartingWithAndIdLessThan(String lastName, Integer id);

    //测试关联查询，如果Person里有addressId属性，那么此时如果要使用关联查询那么应该是getByAddress_IdGreaterThan
    List<Person> getByAddressIdGreaterThan(Integer addressId);

    //测试自定义查询
    @Query("select p from Person p where p.id = (select max(a.id) from Person a)")
    Person getMaxAgePerson();

    //使用占位符的方式传递参数
    @Query("select p from Person p where p.lastName = ?1 and p.email = ?2")
    List<Person> testQueryAnnotationParam1(String lastName, String email);

    //使用命名参数的方式传递参数
    @Query("select p from Person p where p.lastName = :lastName and p.email = :email")
    List<Person> testQueryAnnotationParam2(@Param("lastName") String lastName, @Param("email") String email);

    //测试Like,允许在占位符上添加%%，
    @Query("select p from Person p where p.lastName like ?1 and p.email like ?2%")
    List<Person> testQueryAnnotationLikeParam1(String lastName, String email);

    //使用原生SQL查询
    @Query(value = "select count(*) from jpa_person", nativeQuery = true)
    long getTotalCount();

    @Modifying
    @Query("update Person p set p.email = :email where p.id = :id")
    void updateEmailById(@Param("id") Integer id, @Param("email") String email);

}
