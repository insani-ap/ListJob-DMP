package com.insani.listjobtest.modul.home.job.detail;

import com.google.android.gms.common.internal.Preconditions;
import com.insani.listjobtest.core.BaseCallback;
import com.insani.listjobtest.core.model.JobModel;
import com.insani.listjobtest.core.repo.service.job.JobRepo;

public class JobDetailPresenter implements JobDetailPresenterContract {
    private final JobRepo mRepo;
    private final JobDetailViewContract mView;

    private JobDetailPresenter(JobRepo mRepo, JobDetailViewContract mView) {
        this.mRepo = Preconditions.checkNotNull(mRepo, "Repo is Empty");
        this.mView = Preconditions.checkNotNull(mView, "View is Empty");
    }

    public static JobDetailPresenterContract getInstance(JobRepo mRepo, JobDetailViewContract mView) {
        return new JobDetailPresenter(mRepo, mView);
    }

    @Override
    public void loadDetail(String id) {
        mRepo.doGetJobDetail(id, new BaseCallback() {
            @Override
            public void onEntityPosted(Object o) {
                mView.dataLoaded((JobModel) o);
            }

            @Override
            public void onErrorRequest(Throwable t) {
                mView.somethingError(t.getMessage());
            }
        });
    }
}
