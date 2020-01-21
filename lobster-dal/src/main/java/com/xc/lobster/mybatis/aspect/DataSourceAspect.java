package com.xc.lobster.mybatis.aspect;

import com.xc.lobster.mybatis.datasource.DynamicDataSourceHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Order(0)
public class DataSourceAspect {

    @Before(value = "@annotation(dataSourceAnno)")
    public void dataSourcePoint(JoinPoint joinPoint, DataSourceAnno dataSourceAnno) {
        DynamicDataSourceHolder.putDataSource(dataSourceAnno.value());
    }

    @After("@annotation(dataSourceAnno)")
    public void afterSwitchDS(JoinPoint point, DataSourceAnno dataSourceAnno){
        DynamicDataSourceHolder.removeDataSource();
    }
}
