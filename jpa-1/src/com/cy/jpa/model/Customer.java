package com.cy.jpa.model;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 客户实体类
 *
 * @author yun.chen
 * @create 2015-11-21 17:49
 */


@NamedQuery(name = "testNamedQuery",query = "from Customer a where a.age = ?1")
@Cacheable(true)
@Entity
@Table(name = "JPA_CUSTOMER")
public class Customer {

    private Integer id;

    private String lastName;

    private String email;

    private int age;

    private Date createdDate;

    private Date birth;

    private Set<Order> orderSet = new HashSet<Order>();

    public Customer() {
    }

    public Customer(int age, String lastName) {
        this.age = age;
        this.lastName = lastName;
    }

    //1-n
    //默认是懒加载方式，通过修改cascade 来修改删除策略
//    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
//    @JoinColumn(name = "CUSTOMER_ID")

    //双向1-n,由多的一端维护关联关系，1的一端不维护，通过mappedBy来设置，同时去掉JoinColumn
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE},mappedBy = "customer")
    public Set<Order> getOrderSet() {
        return orderSet;
    }

    public void setOrderSet(Set<Order> orderSet) {
        this.orderSet = orderSet;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //默认会加 @Basic注解
    @Basic
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Temporal(TemporalType.DATE)
    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    //不需要映射为数据库的一列，ORM将忽略该列
    @Transient
    public String getLastAndEmail() {
        return "lastName:" + this.lastName + ",email:" + email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createdDate=" + createdDate +
                ", birth=" + birth +
                '}';
    }
}
