package org.seefly.springmongodb.extend.querys;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.ConvertingParameterAccessor;
import org.springframework.data.mongodb.repository.query.MongoQueryMethod;
import org.springframework.data.mongodb.repository.query.StringBasedMongoQuery;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.expression.ExpressionParser;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author liujianxin
 * @date 2021/8/23 11:02
 */
public class TenantStringBasedMongoQuery  extends StringBasedMongoQuery {

    public TenantStringBasedMongoQuery(MongoQueryMethod method, MongoOperations mongoOperations, ExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {
        super(method, mongoOperations, expressionParser, evaluationContextProvider);
    }

    @Override
    protected Query createQuery(ConvertingParameterAccessor accessor) {
        return super.createQuery(accessor).addCriteria(where("tenantId").is(40));
    }
}
