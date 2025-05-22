package com.vueones;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.vueones.mapper")
public class VuedemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VuedemoApplication.class, args);
    }

}
