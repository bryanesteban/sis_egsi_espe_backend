package com.espe.ListoEgsi.enums;


public enum StatusEnum {
    ACTIVE("ACTIVE", "the process is active"),
    INACTIVE("INACTIVE", "the process is inactive"),
    DELETED("DELETED", "the process is deleted"),
    BLOCKED("BLOCKED", "the process is blocked"),
    COMPLETED("COMPLETED", "the process is completed");

    private final String stateName;
    private final String description;

    StatusEnum(String stateName, String description) {
        this.stateName = stateName;
        this.description = description;
    }

    public String getStateName() {
        return stateName;
    }

    public String getDescription() {
        return description;
    }
}