package com.xc.lobster.mybatis.datasource;

public class DynamicDataSourceHolder {
    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static void putDataSource(DataSourceType dataSourceType) {
        holder.set(dataSourceType.getCode());
    }

    public static String getDataSource() {
        return holder.get();
    }

    public static void removeDataSource() {
        holder.remove();
    }
}
