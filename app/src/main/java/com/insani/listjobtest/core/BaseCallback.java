package com.insani.listjobtest.core;

public interface BaseCallback {
    void onEntityPosted(Object o);
    void onErrorRequest(Throwable t);
}
