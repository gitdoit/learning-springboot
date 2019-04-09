package org.seefly.springwebsocket;

import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SpringWebsocketApplicationTests {

    @Test
    public void contextLoads() {
        HashSet<Integer> ids = new HashSet<>();
        HashSet<Student> students = new HashSet<>();
        
        ids.add(1);
        ids.add(2);
        ids.add(3);
        students.add(new Student().setId(1));
        students.add(new Student().setId(2));
        ids.removeAll(students);

        System.out.println(ids.size());

    }

    @Data
    @Accessors(chain = true)
    public static class Student{
        private Integer id;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if(o == null) return false;
            if(o instanceof Integer){
                return this.id.equals(o);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.id.hashCode();
        }
    }

}

