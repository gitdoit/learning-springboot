package org.seefly.springmongodb.extend;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

/**
 * @author liujianxin
 * @date 2021/8/4 19:51
 **/
public class TenantMongoRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends MongoRepositoryFactoryBean<T, S, ID> {
    
    public TenantMongoRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }
    
    @Override
    protected RepositoryFactorySupport getFactoryInstance(MongoOperations operations) {
        return new TenantMongoRepositoryFactory(operations);
    }

}
