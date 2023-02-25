package com.insani.listjobtest.core.repo.service.job;

import com.insani.listjobtest.core.BaseCallback;

import java.util.HashMap;

public interface JobRepo {
    void doGetListJob(Integer page, BaseCallback mCallback);
    void doSearchJob(HashMap<String, Object> query, BaseCallback mCallback);
    void doGetJobDetail(String id, BaseCallback mCallback);
}
