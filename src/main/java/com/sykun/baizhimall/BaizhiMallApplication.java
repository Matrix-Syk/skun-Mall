package com.sykun.baizhimall;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@MapperScan("com.sykun.baizhimall.dao")
@SpringBootApplication
public class BaizhiMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaizhiMallApplication.class, args);
    }

    // 配置Mybatisplus的分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        return page;
    }
    // 配置srpingsecurity的密码加密处理类
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
