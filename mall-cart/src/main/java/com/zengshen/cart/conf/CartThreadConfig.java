package com.zengshen.cart.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author word
 */
@Slf4j
@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
@Configuration
public class CartThreadConfig {

    @Bean
    public ThreadPoolExecutor poolExecutor(ThreadPoolConfigProperties properties) {
        return new ThreadPoolExecutor(
                properties.getCoreSize(),
                properties.getMaxSize(),
                properties.getKeepAliveTime(), TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(properties.getQueueSize()),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setName("cart_thread");
                        return t;
                    }
                },
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        log.error("错误拒绝, runnable: {}, executor: {}",
                                r, executor);
                    }
                }
        );
    }

}
