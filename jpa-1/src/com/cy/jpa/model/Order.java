package com.cy.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author yun.chen
 * @create 2015-11-22 11:56
 */
@Entity
@Table(name="JPA_ORDER")
public class Order {

    private Integer id;

    private String orderName;

    private Customer customer;

    //单向 n-1,使用fetch来实现懒加载
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CUSTOMER_ID")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Column(name="ORDER_NAME")
    public String getOrderName() {

        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
