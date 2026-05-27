package com.mistake.v2.infrastructure.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置 — 分页插件 + Mapper 扫描
 *
 * @author mistake-team
 */
@Configuration
@MapperScan("com.mistake.v2.infrastructure.persistence.mapper")
public class MybatisPlusConfig {

    /**
     * 注册 MyBatis-Plus 拦截器 (分页插件)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 分页插件 — 支持 MySQL 和 SQLite
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 溢出处理 (页码超过最大值时回到首页)
        paginationInterceptor.setOverflow(true);
        // 单页最大限制 500 条
        paginationInterceptor.setMaxLimit(500L);

        interceptor.addInnerInterceptor(paginationInterceptor);
        return interceptor;
    }
}
