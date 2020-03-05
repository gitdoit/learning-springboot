package top.seefly.springmybatisplusds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.seefly.springmybatisplusds.mapper")
public class SpringMybatisplusDsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMybatisplusDsApplication.class, args);
    }

}
