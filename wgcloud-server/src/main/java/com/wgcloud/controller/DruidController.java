package com.wgcloud.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidController {

    /*
    设置访问配置信息
     */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }

    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");   //设置druid的访问路径

        HashMap<String ,String > initParameters = new HashMap<>();
        initParameters.put("loginUsername","admin");    //设置用户名和密码
        initParameters.put("loginPassword","123456");

        initParameters.put("allow","localhost");  //允许那些访问

        bean.setInitParameters(initParameters);
        return bean;
    }

}


