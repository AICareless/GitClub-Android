package com.i502tech.gitclub.base.mvvm;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * description $desc$
 * created by jerry on 2019/4/18.
 */
public class Resource<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable public final String msg;
    private Resource(@NonNull Status status, @Nullable T data,
                     @Nullable String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public boolean isSuccess(){
        return status == Status.SUCCESS;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg) {
        return new Resource<>(Status.ERROR, null, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }

    public enum Status { SUCCESS, ERROR, LOADING }
}
