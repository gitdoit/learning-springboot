package org.seefly.springmongodb.entity;

import lombok.Data;
import org.seefly.springmongodb.entity.event.DomainEvent;
import org.seefly.springmongodb.enums.AuditStatusEnum;
import org.seefly.springmongodb.service.PersonService;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author liujianxin
 * @date 2021/7/15 21:09
 */
@Data
@Document
public class Person {
    
    @Id
    private String id;
    
    private String name;
    
    private Integer height;
    
    private Integer age;

    private AuditStatusEnum status;
    
    private List<String> hobbies;
    
    private List<Person> children;

    private List<Integer> scores;
    
    
    /**
     *
     * 一般用于DDD模式，使用对应的Repository增删改实体的时候，这个方法都会被调用
     * 并在标注了@TransactionalEventListener注解的地方被监听，如{@link PersonService#domainEventListener(org.seefly.springmongodb.entity.event.DomainEvent)}
     *
     * We need also remember that @DomainEvents feature works only when using Spring Data repositories.
     * If an exception occurs during events publication, the listeners will simply never get notified.
     *
     * Domain events are published using a simple ApplicationEventPublisher interface.
     * <strong>By default<strong/>, when using ApplicationEventPublisher, events are published and consumed in the same thread.
     *
     * 领域驱动模型的设计
     *      https://v2ex.com/t/665465
     *      https://www.baeldung.com/spring-data-ddd
     *      https://dev.to/peholmst/strategic-domain-driven-design-3e87
     *      https://www.infoq.cn/article/alibaba-freshhema-ddd-practice
     *      https://tech.meituan.com/2017/12/22/ddd-in-practice.html
     */
    @DomainEvents
    public Collection<DomainEvent> events() {
        return Collections.singletonList(new DomainEvent(this.id));
    }
    
    @AfterDomainEventPublication
    public void afterEvent(){
        // do something
    }
    
}
