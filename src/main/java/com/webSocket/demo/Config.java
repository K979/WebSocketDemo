package com.webSocket.demo;

import com.webSocket.demo.filter.SensitiveWordFilter;
import com.webSocket.demo.interceptor.SendsitiveWordInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    /**
     * 注册过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean getFilterRegistrationBean(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setOrder(1);  //设置优先级
        registration.setFilter(new SensitiveWordFilter());  //设置过滤器
        registration.setName("SensitiveWordFilter");  //设置姓名
        registration.addUrlPatterns("/ws/*");  //设置过滤路径
        return registration;
    }

    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sendsitiveWordInterceptor()).addPathPatterns("/*");
    }

    /**
     * 加载拦截器bean
     * @return
     */
    @Bean
    public SendsitiveWordInterceptor sendsitiveWordInterceptor(){
        return new SendsitiveWordInterceptor();
    }

}
