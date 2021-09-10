package org.seefly.springmongodb.repository.impl;

import org.seefly.springmongodb.repository.DynamicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * @author liujianxin
 * @date 2021/9/8 13:55
 */
public class DynamicRepositoryImpl <T, I extends Serializable> extends SimpleMongoRepository<T, I> implements DynamicRepository<T, I> {
    private final MongoOperations mongoOperations;
    private final MongoEntityInformation<T,I> entityInformation;

    public DynamicRepositoryImpl(final MongoEntityInformation<T,I> entityInformation, final MongoOperations mongoOperations) {
        super(entityInformation, mongoOperations);
        this.entityInformation = entityInformation;
        this.mongoOperations = mongoOperations;
    }


    @Override
    public Page<T> findAll(Query query, Pageable pageable) {
        Assert.notNull(query, "Query must not be null!");

        return new PageImpl<T>(
                mongoOperations.find(query.with(pageable), entityInformation.getJavaType(), entityInformation.getCollectionName()),
                pageable,
                mongoOperations.count(query, entityInformation.getJavaType(), entityInformation.getCollectionName())
        );
    }
}
