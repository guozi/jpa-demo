package com.cy.spring.jpa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author yun.chen
 * @create 2015-11-28 15:59
 */
@Entity
@Table(name= "JPA_ADDRESS")
public class Address {

    @GeneratedValue
    @Id
    private Integer id;

    private String province;

    private String city;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
