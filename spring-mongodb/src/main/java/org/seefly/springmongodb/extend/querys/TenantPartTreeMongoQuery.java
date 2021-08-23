package org.seefly.springmongodb.extend.querys;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.ConvertingParameterAccessor;
import org.springframework.data.mongodb.repository.query.MongoQueryMethod;
import org.springframework.data.mongodb.repository.query.PartTreeMongoQuery;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.expression.ExpressionParser;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author liujianxin
 * @date 2021/8/23 11:00
 */
public class TenantPartTreeMongoQuery extends PartTreeMongoQuery {


    public TenantPartTreeMongoQuery(MongoQueryMethod method, MongoOperations mongoOperations, ExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {
        super(method, mongoOperations, expressionParser, evaluationContextProvider);
    }

    @Override
    protected Query createQuery(ConvertingParameterAccessor accessor) {
        return super.createQuery(accessor).addCriteria(where("tenantId").is(40));
    }

    @Override
    protected Query createCountQuery(ConvertingParameterAccessor accessor) {
        return super.createCountQuery(accessor).addCriteria(where("tenantId").is(40));
    }
}
