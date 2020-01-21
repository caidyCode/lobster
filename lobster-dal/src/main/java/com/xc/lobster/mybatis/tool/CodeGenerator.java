package com.xc.lobster.mybatis.tool;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class CodeGenerator {
    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        // 全局配置
        com.baomidou.mybatisplus.generator.config.GlobalConfig gc = new com.baomidou.mybatisplus.generator.config.GlobalConfig();
        gc.setAuthor("caidy");
        gc.setOutputDir("D:\\work\\tc-life\\tmp");
        gc.setFileOverride(false);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(false);
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("Q2vq17K7Ld2LKjDm");
        dsc.setUrl("jdbc:mysql://47.99.106.249:3306/test?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setCapitalMode(true);
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setTablePrefix("tc_");
        strategy.setInclude(new String[] {"user"});
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.xc.lobster");
        pc.setMapper("mybatis.mapper");
        pc.setEntity("mybatis.model");
        mpg.setPackageInfo(pc);

        // 执行生成
        mpg.execute();
    }
}
