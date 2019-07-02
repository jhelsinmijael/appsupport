package com.mijael.appsupport.mvp.model;

public enum Status {

    SUCCESS(99),
    FAIl(0);

    Status(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }

    public static Status getStatus(int value) {
        if (value == 99)
            return SUCCESS;
        else
            return FAIl;

    }

}