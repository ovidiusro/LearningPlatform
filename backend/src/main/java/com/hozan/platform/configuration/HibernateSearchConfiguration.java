package com.hozan.platform.configuration;


import com.hozan.platform.service.HibernateSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Configuration
public class HibernateSearchConfiguration {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    HibernateSearchService hibernateSearchService() {
        HibernateSearchService hibernateSearchService = new HibernateSearchService(entityManager);
        hibernateSearchService.initializeHibernateSearch();
        return hibernateSearchService;
    }
}
