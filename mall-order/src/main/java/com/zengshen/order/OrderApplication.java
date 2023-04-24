package com.zengshen.order;

import com.zengshen.common.utils.IdWorker;
import com.zengshen.rabbit.task.annotation.EnableElasticJob;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.HttpMessageConverter;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.stream.Collectors;

/**
 * @author word
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.zengshen.order.mapper"})
@ComponentScan(basePackages = {"com.zengshen.order.*"})
@EnableFeignClients
@EnableElasticJob
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public IdWorker idWorker() {
        // 雪花算法集成,每台服务器这个数字改一下-
        return new IdWorker(3, 2);
    }

    /**
     * 其实在spring-boot2.1.x版本是不用手动注入HttpMessageConverters的，
     * 因为可以自动配置的, 见HttpMessageConvertersAutoConfiguration。
     * 但是在spring-boot2.2.x版本HttpMessageConvertersAutoConfiguration有所改动，
     * 加了个@Conditional(NotReactiveWebApplicationCondition.class) ，
     * 因为gateway是ReactiveWeb，所以针对HttpMessageConverters的自动配置就不生效了，
     * 故需要手动注入HttpMessageConverters
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }
}
