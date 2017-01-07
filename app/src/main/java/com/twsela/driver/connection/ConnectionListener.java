package com.twsela.driver.connection;

public interface ConnectionListener<T> {
    void onSuccess(T response, int statusCode, String tag);

    void onFail(Exception ex, int statusCode, String tag);
}
