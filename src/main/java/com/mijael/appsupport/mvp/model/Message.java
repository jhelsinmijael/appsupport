package com.mijael.appsupport.mvp.model;

import org.json.JSONObject;

public class Message<T> {

    public static final int OP_DEFAULT_VALUE = -100;
    public static final String OP_STR_DEFAULT_VALUE = "";

    private int op;
    private String opStr;
    private Status status;
    private T object;

    public Message(int op, String opStr, Status status, T object) {
        this.op = OP_DEFAULT_VALUE;
        this.op = op;
        this.opStr = opStr;
        this.status = status;
        this.object = object;
    }

    public Message(int op, Status status, T object) {
        this(op, OP_STR_DEFAULT_VALUE, status, object);
    }

    public Message(String opStr, Status status, T object) {
        this(OP_DEFAULT_VALUE, opStr, status, object);
    }

    public Message(int op, T object){
        this(op, OP_STR_DEFAULT_VALUE, Status.SUCCESS, object);
    }

    public int getOp() {
        return op;
    }

    public String getOpStr() {
        return opStr;
    }

    public Status getStatus() {
        return status;
    }

    public T getObject() {
        return object;
    }
}
