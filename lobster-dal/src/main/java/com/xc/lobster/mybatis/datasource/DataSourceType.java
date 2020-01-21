package com.xc.lobster.mybatis.datasource;

public enum DataSourceType {

    MASTER("master","主库"),
    SLAVE("slave","从库");


    private String code;
    private String desc;

    DataSourceType(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
