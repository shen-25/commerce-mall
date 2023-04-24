package com.zengshen.goods;

import com.zengshen.common.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author word
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.zengshen.goods.mapper"})
public class MallGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallGoodsApplication.class, args);
    }
    @Bean
    public IdWorker idWorker() {
        // 雪花算法集成,每台服务器这个数字改一下-
        return new IdWorker(3, 1);
    }
}
