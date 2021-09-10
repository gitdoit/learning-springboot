package org.seefly.springmongodb.extend;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seefly.springmongodb.repository.PersonRepository;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author liujianxin
 * @date 2021/9/8 15:54
 */
@Slf4j
@Component
public class TenantRepository implements InitializingBean {
    private final PersonRepository repository;
    private  PersonRepository proxy;

    public TenantRepository(PersonRepository repository) {
        this.repository = repository;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        ProxyFactory result = new ProxyFactory();
        result.setTarget(repository);
        result.setInterfaces(PersonRepository.class);
        result.addAdvice(new MyII());
        proxy = (PersonRepository) result.getProxy();

    }

    public PersonRepository getProxy() {
        return this.proxy;
    }

    public static class MyII implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {

            ExecEvnContext.asTenant();
            try {
                return methodInvocation.proceed();
            } finally {
                ExecEvnContext.clear();
            }
        }
    }
}
