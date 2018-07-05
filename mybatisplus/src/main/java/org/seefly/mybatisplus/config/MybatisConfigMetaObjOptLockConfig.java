package org.seefly.mybatisplus.config;


import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author liujianxin
 * @date 2018-06-28 23:00
 * 描述信息：mybatis-plus的配置文件
 * 可配置的属性到这里看：com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties
 * 跟Mybatis差不多一个样
 **/

@MapperScan("org.seefly.mybatisplus.mapper")
@Configuration
public class MybatisConfigMetaObjOptLockConfig {

    @Bean("mybatisSqlSession")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ResourcePatternResolver resolver, GlobalConfig globalConfiguration) throws Exception {
        System.out.println(dataSource.getClass()+"======================================");
        System.out.println(dataSource.getConnection().getMetaData().getURL()+"================================================");
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        //model设置别名，这样在写mapper.xml的时候就不用指定参数或者返回值类型的全限定名了
        sqlSessionFactory.setTypeAliasesPackage("org.seefly.myplus.model");
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);

        //开启输出执行的sql语句，输出到控制台。也可以指定其他的类，输出到文件或者数据库等地方
        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);

        //开启驼峰命名，将login_name -->loginName
        //configuration.setMapUnderscoreToCamelCase(true);

        sqlSessionFactory.setConfiguration(configuration);
        //这个应该就是mybatis的拦截器，在执行sql语句之前都会被拦截，自动分页加Limit和查询count就是靠这个
        sqlSessionFactory.setPlugins(new Interceptor[]{
                new OptimisticLockerInterceptor()
        });
        sqlSessionFactory.setGlobalConfig(globalConfiguration);
        // 获取mapper映射文件
        //ResourcePatternResolver resolver 这个可以解析指定路径下的多个配置文件
        sqlSessionFactory.setMapperLocations(resolver.getResources("classpath*:mybatis/mapper/*.xml"));
        TableInfoHelper.initSqlSessionFactory(sqlSessionFactory.getObject());
        return sqlSessionFactory.getObject();
    }

    /**
     * mybatisplus拓展配置
     * 太多了去这里看：com.baomidou.mybatisplus.core.config.GlobalConfig
     * @return
     */
    @Bean
    public GlobalConfig globalConfiguration() {
        GlobalConfig conf = new GlobalConfig();
        // 是否刷新mapper
        conf.setRefresh(true);
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        //主键类型
        //AUTO(0, "数据库ID自增"),
        //    INPUT(1, "用户输入ID"),
        //    ID_WORKER(2, "全局唯一ID"),
        //    UUID(3, "全局唯一ID"),
        //    NONE(4, "该类型为未设置主键类型"),
        //    ID_WORKER_STR(5, "字符串全局唯一ID");
        dbConfig.setIdType(IdType.AUTO);
        //字段判断
        //IGNORED(0, "忽略判断"),
        //NOT_NULL(1, "非 NULL 判断"),
        //NOT_EMPTY(2, "非空判断");
        dbConfig.setFieldStrategy(FieldStrategy.NOT_NULL);

        conf.setDbConfig(dbConfig);
        return conf;
    }
}
