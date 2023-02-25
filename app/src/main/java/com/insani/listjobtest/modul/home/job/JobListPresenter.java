package com.insani.listjobtest.modul.home.job;

import com.google.android.gms.common.internal.Preconditions;
import com.insani.listjobtest.core.BaseCallback;
import com.insani.listjobtest.core.model.JobModel;
import com.insani.listjobtest.core.repo.service.job.JobRepo;

import java.util.HashMap;
import java.util.List;

public class JobListPresenter implements JobListPresenterContract {
    private final JobRepo mRepo;
    private final JobListViewContract mView;

    private JobListPresenter(JobRepo mRepo, JobListViewContract mView) {
        this.mRepo = Preconditions.checkNotNull(mRepo, "Repo is Empty");
        this.mView = Preconditions.checkNotNull(mView, "View is Empty");
    }

    public static JobListPresenterContract getInstance(JobRepo mRepo, JobListViewContract mView) {
        return new JobListPresenter(mRepo, mView);
    }

    @Override
    public void loadList(Integer page) {
        mRepo.doGetListJob(page, new BaseCallback() {
            @Override
            public void onEntityPosted(Object o) {
                mView.listLoaded((List<JobModel>) o);
            }

            @Override
            public void onErrorRequest(Throwable t) {
                mView.somethingError(t.getMessage(), t);
            }
        });
    }

    @Override
    public void findList(String description, boolean isFulltime, String location) {
        HashMap<String, Object> query = new HashMap<>();
        query.put("description", description);
        query.put("location", location);
        if (isFulltime) query.put("full_time", isFulltime);

        mRepo.doSearchJob(query, new BaseCallback() {
            @Override
            public void onEntityPosted(Object o) {
                mView.listFound((List<JobModel>) o);
            }

            @Override
            public void onErrorRequest(Throwable t) {
                mView.somethingError(t.getMessage(), t);
            }
        });
    }

    @Override
    public void openDetail(String id) {
        mView.listOpened(id);
    }
}
