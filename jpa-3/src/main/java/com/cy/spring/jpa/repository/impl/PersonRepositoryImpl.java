package com.cy.spring.jpa.repository.impl;

import com.cy.spring.jpa.model.Person;
import com.cy.spring.jpa.repository.custom.PersonRepositoryCustom;
import org.apache.commons.lang.StringUtils;
import org.hibernate.CacheMode;
import org.hibernate.search.impl.SimpleIndexingProgressMonitor;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试 HibernateSearch，主要为了跟JPA+queryDSL的语法做对比
 * @author yun.chen
 * @create 2015-11-28 21:24
 */

//
public class PersonRepositoryImpl implements PersonRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Person test(Integer id) {
        return entityManager.find(Person.class, id);
    }

    @Override
    public void reindex() throws InterruptedException {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        fullTextEntityManager
                .createIndexer(Person.class)
                .batchSizeToLoadObjects(25)
                .cacheMode(CacheMode.NORMAL)
                .idFetchSize(150)
                .threadsToLoadObjects(1)
                .progressMonitor(new SimpleIndexingProgressMonitor(1000))
                .startAndWait();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> search(String text) {
        if (StringUtils.isEmpty(text))
            return new ArrayList();

        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(Person.class).get();

        // a very basic query by keywords
        org.apache.lucene.search.Query query =
                queryBuilder
                        .keyword()
                        .onFields("lastName", "email")
                        .matching(text)
                        .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, Person.class);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List results = jpaQuery.<Person>getResultList();
        if (results == null) {
            return new ArrayList();
        }
        return results;
    }

}
