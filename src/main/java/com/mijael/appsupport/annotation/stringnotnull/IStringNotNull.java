package com.mijael.appsupport.annotation.stringnotnull;

public interface IStringNotNull<T> {

    T getWithStringNotNull();

    default <T extends IStringNotNull> T getWithStringNotNull(T t){
        return StringNotNullWorker.getInstance().getDeleteStringNull(t);
    }

}
