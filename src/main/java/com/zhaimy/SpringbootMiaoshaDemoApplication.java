package com.zhaimy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhaimy.dao")
public class SpringbootMiaoshaDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMiaoshaDemoApplication.class, args);
    }

}
