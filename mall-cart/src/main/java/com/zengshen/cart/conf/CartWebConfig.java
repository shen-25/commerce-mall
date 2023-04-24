package com.zengshen.cart.conf;

import com.zengshen.cart.interceptor.CartInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author word
 */
@Configuration
public class CartWebConfig implements WebMvcConfigurer {

    /**
     * 将拦截器作为bean写入配置中
     */
    @Bean
    public CartInterceptor cartInterceptor(){
        return new CartInterceptor();
    }

    /**
     * 注意这里不要使用 new Interceptor() ，否则就会出现拦截器里无法注入service的问题
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cartInterceptor())
                .addPathPatterns("/**");
    }

}
