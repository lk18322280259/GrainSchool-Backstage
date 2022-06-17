package com.atguigu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Hello world!
 *
 */

@EnableSwagger2
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class CosApplication {

    public static void main(String[] args) {
        SpringApplication.run(CosApplication.class, args);
    }
}
