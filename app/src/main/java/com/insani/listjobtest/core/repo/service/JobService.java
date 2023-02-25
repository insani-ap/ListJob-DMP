package com.insani.listjobtest.core.repo.service;

import com.insani.listjobtest.core.model.JobModel;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface JobService {
    @GET("recruitment/positions.json")
    Call<List<JobModel>> doGetOrFindJob(@QueryMap HashMap<String, Object> query);

    @GET("recruitment/positions/{id}")
    Call<JobModel> doGetJobDetail(@Path("id") String id);
}
