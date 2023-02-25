package com.insani.listjobtest.modul.home.job;

import com.insani.listjobtest.core.model.JobModel;

import java.util.List;

public interface JobListViewContract {
    void listLoaded(List<JobModel> jobs);
    void listFound(List<JobModel> jobs);
    void listOpened(String id);

    void somethingError(String msg, Throwable t);
}
