package com.zengshen.usercenter;

import com.zengshen.common.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author word
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.zengshen.usercenter.mapper"})
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }
    @Bean
    public IdWorker idWorker() {
        // 雪花算法集成,每台服务器这个数字改一下-
        return new IdWorker(2, 1);
    }
}
