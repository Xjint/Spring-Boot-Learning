package com.bjt.xint;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.bjt.xint.mapper")
public class XintApplication {

    public static void main(String[] args) {
        SpringApplication.run(XintApplication.class, args);
    }

}
