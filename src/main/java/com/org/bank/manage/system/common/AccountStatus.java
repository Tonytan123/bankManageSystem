package com.org.bank.manage.system.common;

/**
 * @author tucker
 */

public enum AccountStatus {
    ACTIVE("正常"),
    FROZEN("冻结"),
    SUSPENDED("挂失"),
    DORMANT("睡眠"),
    CLOSED("销户"),
    DELETED("删除"),;
    private String displayName;
    AccountStatus(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
