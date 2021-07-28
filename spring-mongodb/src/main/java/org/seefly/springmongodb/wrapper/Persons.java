package org.seefly.springmongodb.wrapper;

import lombok.RequiredArgsConstructor;
import org.seefly.springmongodb.entity.Person;
import org.springframework.data.util.Streamable;

import java.util.Iterator;

/**
 * 对于Streamable<Person>的包装
 * 可以编写业务逻辑，很方便
 * @author liujianxin
 * @date 2021/7/28 19:49
 **/
@RequiredArgsConstructor(staticName = "of")
public class Persons implements Streamable<Person> {
    private final Streamable<Person> streamable;
    
    public Integer totalAge(){
        return streamable.stream().mapToInt(Person::getAge).sum();
    }
    
    @Override
    public Iterator<Person> iterator() {
        return streamable.iterator();
    }
}
