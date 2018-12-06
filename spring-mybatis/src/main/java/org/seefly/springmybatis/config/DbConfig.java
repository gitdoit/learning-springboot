package org.seefly.springmybatis.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * mybatis中文教程
 * 演示手动配置MyBatis
 * spring boot的自动配置在这里{@link MybatisAutoConfiguration}
 * 1、{@link MapperScan}是怎样工作的？
 * 2、接口和XML是怎样关联的？
 * 3、spring是如何接管Mybatis的事务的？
 *
 * @see <a href="http://www.mybatis.org/mybatis-3/zh/getting-started.html"/>文档
 * @author liujianxin
 * @date 2018-12-05 15:16
 */
@MapperScan(basePackages = "org.seefly.springmybatis.dao")
@Configuration
@PropertySource("classpath:privateConfig.properties")
@ConfigurationProperties(prefix = "datasource")
public class DbConfig {
    private String username;
    private String password;
    private String jdbcUrl;

    /**
     * 代替自动配置
     * @see DataSourceAutoConfiguration.PooledDataSourceConfiguration
     */
    @Bean
    public DataSource dataSource(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setJdbcUrl(jdbcUrl);
        return hikariDataSource;
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
     * @see DataSourceTransactionManagerAutoConfiguration.DataSourceTransactionManagerConfiguration
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataBoxDomainDataSource) {
        return new DataSourceTransactionManager(dataBoxDomainDataSource);
    }

}
