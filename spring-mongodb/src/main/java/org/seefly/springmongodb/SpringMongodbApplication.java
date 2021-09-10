package org.seefly.springmongodb;

import org.seefly.springmongodb.extend.TenantMongoRepositoryFactoryBean;
import org.seefly.springmongodb.repository.impl.DynamicRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableMongoRepositories(/*repositoryFactoryBeanClass = TenantMongoRepositoryFactoryBean.class,*/repositoryBaseClass = DynamicRepositoryImpl.class)
public class SpringMongodbApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SpringMongodbApplication.class, args);
    }
    
}
