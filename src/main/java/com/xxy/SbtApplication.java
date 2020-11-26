package com.xxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.xxy.mapper")
public class SbtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbtApplication.class, args);
	}

}
