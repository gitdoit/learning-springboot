package org.seefly.springsecurity.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author liujianxin
 * @date 2018-12-13 20:15
 */
@Configuration
@MapperScan(basePackages = "org.seefly.springsecurity.mapper")
@PropertySource(value = "classpath:privateConfig.properties")
public class DbConfig {

    @Bean
    @ConfigurationProperties(prefix = "datasource")
    public DataSource dataSource(){
        return new DruidDataSource();
    }

    /**
     * 代替自动配置{@link MybatisAutoConfiguration#sqlSessionFactory(javax.sql.DataSource)}
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ResourcePatternResolver resolver) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        //驼峰命名
        configuration.setMapUnderscoreToCamelCase(true);
        // sql打印
        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        factory.setConfiguration(configuration);
        factory.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
        return factory.getObject();
    }

    /**
     * 代替自动配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataBoxDomainDataSource) {
        return new DataSourceTransactionManager(dataBoxDomainDataSource);
    }
}
