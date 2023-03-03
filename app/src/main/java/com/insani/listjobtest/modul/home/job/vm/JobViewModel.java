package com.insani.listjobtest.modul.home.job.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.insani.listjobtest.core.BaseCallback;
import com.insani.listjobtest.core.model.JobModel;
import com.insani.listjobtest.core.repo.service.job.JobRepo;

import java.util.List;

public class JobViewModel extends ViewModel {
    private JobRepo mRepo;

    private final MutableLiveData<List<JobModel>> jobList = new MutableLiveData<>();

    public LiveData<List<JobModel>> getJobList() {
        return jobList;
    }

    public void setmRepo(JobRepo mRepo) {
        this.mRepo = mRepo;
    }


    public void getData(int page) {
        mRepo.doGetListJob(page, new BaseCallback() {
            @Override
            public void onEntityPosted(Object o) {
                jobList.postValue((List<JobModel>) o);
            }

            @Override
            public void onErrorRequest(Throwable t) {

            }
        });
    }
}
