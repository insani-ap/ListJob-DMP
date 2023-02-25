package com.insani.listjobtest.util;

import android.content.Context;

import com.insani.listjobtest.core.repo.service.job.JobRepo;
import com.insani.listjobtest.core.repo.service.job.JobRepoImpl;

public class Injection {
    public static JobRepo provideJobRepo(Context mContext) {
        return new JobRepoImpl(mContext);
    }
}
