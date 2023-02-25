package com.insani.listjobtest.modul.home.job.detail;

import com.insani.listjobtest.core.model.JobModel;

public interface JobDetailViewContract {
    void dataLoaded(JobModel job);

    void somethingError(String msg);
}
