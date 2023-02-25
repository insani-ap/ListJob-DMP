package com.insani.listjobtest.core.repo.service.job;

import android.content.Context;

import androidx.annotation.NonNull;

import com.insani.listjobtest.core.BaseCallback;
import com.insani.listjobtest.core.RetrofitCaller;
import com.insani.listjobtest.core.model.JobModel;
import com.insani.listjobtest.core.repo.service.JobService;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class JobRepoImpl implements JobRepo {
    private final JobService jobService;

    public JobRepoImpl(Context mContext) {
        this.jobService = RetrofitCaller.getInstance(mContext).create(JobService.class);
    }

    @Override
    public void doGetListJob(Integer page, BaseCallback mCallback) {
        HashMap<String, Object> query = new HashMap<>();
        query.put("page", page);

        Call<List<JobModel>> request = jobService.doGetOrFindJob(query);
        request.enqueue(new Callback<List<JobModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<JobModel>> call, @NonNull Response<List<JobModel>> response) {
                if (response.isSuccessful())
                    mCallback.onEntityPosted(response.body());
                else
                    mCallback.onErrorRequest(new HttpException(response));
            }

            @Override
            public void onFailure(@NonNull Call<List<JobModel>> call, @NonNull Throwable t) {
                mCallback.onErrorRequest(t);
            }
        });
    }

    @Override
    public void doSearchJob(HashMap<String, Object> query, BaseCallback mCallback) {
        Call<List<JobModel>> request = jobService.doGetOrFindJob(query);
        request.enqueue(new Callback<List<JobModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<JobModel>> call, @NonNull Response<List<JobModel>> response) {
                if (response.isSuccessful())
                    mCallback.onEntityPosted(response.body());
                else
                    mCallback.onErrorRequest(new HttpException(response));
            }

            @Override
            public void onFailure(@NonNull Call<List<JobModel>> call, @NonNull Throwable t) {
                mCallback.onErrorRequest(t);
            }
        });
    }

    @Override
    public void doGetJobDetail(String id, BaseCallback mCallback) {
        Call<JobModel> request = jobService.doGetJobDetail(id);
        request.enqueue(new Callback<JobModel>() {
            @Override
            public void onResponse(@NonNull Call<JobModel> call, @NonNull Response<JobModel> response) {
                if (response.isSuccessful())
                    mCallback.onEntityPosted(response.body());
                else
                    mCallback.onErrorRequest(new HttpException(response));
            }

            @Override
            public void onFailure(@NonNull Call<JobModel> call, @NonNull Throwable t) {
                mCallback.onErrorRequest(t);
            }
        });
    }
}
