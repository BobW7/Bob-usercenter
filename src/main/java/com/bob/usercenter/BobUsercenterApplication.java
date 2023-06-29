package com.bob.usercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bob.usercenter.mapper")
public class BobUsercenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(BobUsercenterApplication.class, args);
    }

}
