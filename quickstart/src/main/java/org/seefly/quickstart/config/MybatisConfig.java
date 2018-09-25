package org.seefly.quickstart.config;


import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author liujianxin
 * @date 2018-06-28 23:00
 * 描述信息：mybatis-plus的配置文件
 **/
@MapperScan("org.seefly.quickstart.mapper")
@Configuration
public class MybatisConfig {

    @Autowired
    private Environment env;

    @Bean("mybatisSqlSession")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ResourcePatternResolver resolver) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setTypeAliasesPackage("org.seefly.quickstart.model");
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);

        //开启输出执行的sql语句，输出到控制台。也可以指定其他的类，输出到文件或者数据库等地方
        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        //开启驼峰命名，将login_name -->loginName
        configuration.setMapUnderscoreToCamelCase(true);

        sqlSessionFactory.setConfiguration(configuration);
        //这个应该就是mybatis的拦截器，在执行sql语句之前都会被拦截，自动分页加Limit和查询count就是靠这个
        sqlSessionFactory.setPlugins(new Interceptor[]{
                new OptimisticLockerInterceptor()
        });
        //sqlSessionFactory.setGlobalConfig(globalConfiguration);
        // 获取mapper映射文件
        //ResourcePatternResolver resolver 这个可以解析指定路径下的多个配置文件
        sqlSessionFactory.setMapperLocations(resolver.getResources("classpath*:mybatis/mapper/*.xml"));
        TableInfoHelper.initSqlSessionFactory(sqlSessionFactory.getObject());
        return sqlSessionFactory.getObject();
    }

    /**
     * 数据源，使用Hikari
     */
    @Bean
    public DataSource dataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(env.getProperty("database.username"));
        dataSource.setPassword(env.getProperty("database.password"));
        dataSource.setJdbcUrl(env.getProperty("database.url"));
        return dataSource;
    }


}
