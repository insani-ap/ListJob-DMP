package com.insani.listjobtest.util.exception;

import androidx.annotation.Nullable;

import java.io.IOException;

public class NoNetworkException extends IOException {
    @Nullable
    @Override
    public String getMessage() {
        return "No Internet Connection!";
    }
}
