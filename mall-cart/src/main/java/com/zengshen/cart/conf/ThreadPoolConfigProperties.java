package com.zengshen.cart.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author word
 */
@ConfigurationProperties(prefix = "cart.thread")
@Data
public class ThreadPoolConfigProperties {
    private Integer coreSize = 5;
    private Integer maxSize = 10;
    private Integer keepAliveTime = 20;
    private Integer queueSize = 10;
}
