package org.seefly.springmongodb.extend;

import org.seefly.springmongodb.extend.querys.TenantPartTreeMongoQuery;
import org.seefly.springmongodb.extend.querys.TenantStringBasedAggregation;
import org.seefly.springmongodb.extend.querys.TenantStringBasedMongoQuery;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.*;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

/**
 * Let's customize query with default field
 *
 * 多租户模式下，根据注解决定是否额外查询租户字段
 * @author liujianxin
 * @date 2021/8/4 19:35
 **/
public class TenantMongoQueryLookupStrategy implements QueryLookupStrategy {
    
    private final QueryLookupStrategy strategy;
    private final MongoOperations mongoOperations;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    public TenantMongoQueryLookupStrategy(QueryLookupStrategy strategy,
                                          MongoOperations mongoOperations) {
        this.strategy = strategy;
        this.mongoOperations = mongoOperations;
    }
    
    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory,
            NamedQueries namedQueries) {
        RepositoryQuery repositoryQuery = strategy.resolveQuery(method, metadata, factory, namedQueries);

        // 基础语法查询
        if(repositoryQuery instanceof PartTreeMongoQuery) {
            return new TenantPartTreeMongoQuery(((PartTreeMongoQuery) repositoryQuery).getQueryMethod(),mongoOperations,parser,QueryMethodEvaluationContextProvider.DEFAULT);
        }
        // 基础string
        if(repositoryQuery instanceof StringBasedMongoQuery) {
            return new TenantStringBasedMongoQuery(((StringBasedMongoQuery) repositoryQuery).getQueryMethod(),mongoOperations,parser,QueryMethodEvaluationContextProvider.DEFAULT);
        }

        if(repositoryQuery instanceof StringBasedAggregation) {
            return new TenantStringBasedAggregation(metadata,((StringBasedAggregation) repositoryQuery).getQueryMethod(),mongoOperations,parser,QueryMethodEvaluationContextProvider.DEFAULT, method, factory);
        }
        return repositoryQuery;
    }

    

}
