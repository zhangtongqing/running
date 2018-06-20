package com.peipao.framework.datasource;

import org.apache.commons.lang3.ArrayUtils;
//import org.apache.ibatis.io.VFS;
//import org.apache.ibatis.jdbc.Null;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
//import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 方法名称：RunningDsConfig
 * 功能描述：RunningDsConfig
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/22 18:13
 * 修订记录：
 */
@Configuration
@MapperScan(basePackages = "com.peipao.qdl.*.dao", sqlSessionTemplateRef  = "runningSqlSessionTemplate")

public class RunningDsConfig {
    @Bean(name = "runningDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.running")
    @Primary
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "runningSqlSessionFactory")
    @Primary
    public SqlSessionFactory runningSqlSessionFactory(@Qualifier("runningDataSource") DataSource dataSource) throws Exception {
//        VFS.addImplClass(SpringBootVFS.class);
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
//        bean.setVfs(SpringBootVFS.class);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        Resource[] resources1 = resolver.getResources("classpath*:mybatis-config.xml");
        Resource[] resources2 = resolver.getResources("classpath*:mybatis-mapper/**/*.xml");
        Resource[] resources = (Resource[]) ArrayUtils.addAll(resources2);
        bean.setMapperLocations(resources);
//        bean.setTypeAliasesPackage("com.peipao.qdl.activity.model;com.peipao.qdl.sms.model");
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCallSettersOnNulls(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        bean.setConfiguration(configuration);

        return bean.getObject();
    }

    @Bean(name = "runningTransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("runningDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "runningSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("runningSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
/*

 */

}
