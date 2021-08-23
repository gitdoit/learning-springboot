package org.seefly.springmongodb.extend.querys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.repository.query.ConvertingParameterAccessor;
import org.springframework.data.mongodb.repository.query.MongoQueryMethod;
import org.springframework.data.mongodb.repository.query.StringBasedAggregation;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.expression.ExpressionParser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author liujianxin
 * @date 2021/8/23 11:03
 */
@Slf4j
public class TenantStringBasedAggregation  extends StringBasedAggregation {
    private final Method rawMethod;
    private final RepositoryMetadata metadata;
    private final ProjectionFactory factory;
    private final MongoOperations mongoOperations;

    public TenantStringBasedAggregation(RepositoryMetadata metadata, MongoQueryMethod method, MongoOperations mongoOperations, ExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider, Method method1, ProjectionFactory factory) {
        super(method, mongoOperations, expressionParser, evaluationContextProvider);
        this.rawMethod = method1;
        this.metadata = metadata;
        this.factory = factory;
        this.mongoOperations = mongoOperations;
    }


    @Override
    protected Object doExecute(MongoQueryMethod method, ResultProcessor resultProcessor, ConvertingParameterAccessor accessor, Class<?> typeToRead) {
        return super.doExecute(new MyQueryMethod(rawMethod,metadata,factory,mongoOperations.getConverter().getMappingContext()), resultProcessor, accessor, typeToRead);
    }


    private class MyQueryMethod extends MongoQueryMethod{

        @Override
        public String[] getAnnotatedAggregation() {
            String[] annotatedAggregation = super.getAnnotatedAggregation();
            ArrayList<String> list = new ArrayList<>();
            list.add("{$match: {tenantId:40} }}");
            list.addAll(Arrays.asList(annotatedAggregation));
            // "{$match: {age:{$gt:?0}} }}",
            // list.add("{$match: {age:{$gt:?0}} }}");
            return list.toArray(new String[]{});
        }

        public MyQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory projectionFactory, MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext) {
            super(method, metadata, projectionFactory, mappingContext);
        }

    }



}
