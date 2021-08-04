package org.seefly.springmongodb;

import org.seefly.springmongodb.extend.SoftDeleteMongoRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableMongoRepositories(repositoryFactoryBeanClass = SoftDeleteMongoRepositoryFactoryBean.class)
public class SpringMongodbApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SpringMongodbApplication.class, args);
    }
    
}
