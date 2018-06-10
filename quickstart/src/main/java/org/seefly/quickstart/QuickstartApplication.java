package org.seefly.quickstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication 标注这是一个springboot的程序入口，这个注解是一个组合注解
 *      @EnableAutoConfiguration 此注解被组合在@SpringBootApplication注解中，表示开启自动配置
 *          @AutoConfigurationPackage 此注解被组合在@EnableAutoConfiguration注解中
 *              @Import(AutoConfigurationPackages.Registrar.class) 此注解被组合在@AutoConfigurationPackage，为spring底层注解
 *                  Registrar.class->new PackageImport(metadata).getPackageName() 这个方法将返回，被该注解
 *                  所标注的类所处的位置，那么springboot将会根据这个位置进行扫描，将把该位置的所有组件及子包
 *                  下的所有组件注册到spring容器中
 *          @Import(AutoConfigurationImportSelector.class) 此注解被组合在@EnableAutoConfiguration中，他的作用是将自动
 *          选择这个应用所依赖的组件，并将他们配置好。
 */
@SpringBootApplication
public class QuickstartApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickstartApplication.class, args);
    }
}
