package org.seefly.springmongodb.extend;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;

import java.util.Optional;

/**
 * @author liujianxin
 * @date 2021/8/4 19:50
 **/
public class TenantMongoRepositoryFactory extends MongoRepositoryFactory {
    
    private final MongoOperations mongoOperations;
    
    public TenantMongoRepositoryFactory(MongoOperations mongoOperations) {
        super(mongoOperations);
        this.mongoOperations = mongoOperations;
    }
    
    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(QueryLookupStrategy.Key key,
            QueryMethodEvaluationContextProvider evaluationContextProvider) {
        Optional<QueryLookupStrategy> optStrategy = super.getQueryLookupStrategy(key,
                evaluationContextProvider);
        return optStrategy.map(this::createSoftDeleteQueryLookupStrategy);
    }
    
    private TenantMongoQueryLookupStrategy createSoftDeleteQueryLookupStrategy(QueryLookupStrategy strategy) {
        return new TenantMongoQueryLookupStrategy(strategy, mongoOperations);
    }

}
