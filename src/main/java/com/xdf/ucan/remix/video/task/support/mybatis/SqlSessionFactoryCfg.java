package com.xdf.ucan.remix.video.task.support.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.xdf.ucan.remix.video.task.dao")
public class SqlSessionFactoryCfg {
}
