package com.xc.lobster.mybatis.aspect;

import com.xc.lobster.mybatis.datasource.DataSourceType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface DataSourceAnno {
    /**
     * 数据库路由
     */
    DataSourceType value() default DataSourceType.MASTER;
}
