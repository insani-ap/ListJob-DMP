package com.insani.listjobtest.modul.home.job.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.insani.listjobtest.R;
import com.insani.listjobtest.core.model.JobModel;
import com.insani.listjobtest.databinding.AdapterJobBinding;
import com.insani.listjobtest.modul.home.job.JobListPresenterContract;
import com.insani.listjobtest.util.helper.ImageHelper;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {
    private final List<JobModel> jobs;
    private final Context mContext;
    private final JobListPresenterContract mPresenter;

    public JobAdapter(List<JobModel> jobs, Context mContext, JobListPresenterContract mPresenter) {
        this.jobs = jobs;
        this.mContext = mContext;
        this.mPresenter = mPresenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobModel job = jobs.get(position);

        if (job != null) {
            holder.mBinding.getRoot().setOnClickListener(view -> mPresenter.openDetail(job.getId()));

            ImageHelper.loadImageViaGlide(mContext, job.getCompanyLogo(), holder.mBinding.logo, R.drawable.ic_no_image);
            holder.mBinding.company.setText(job.getCompany());
            holder.mBinding.location.setText(job.getLocation());
            holder.mBinding.role.setText(job.getTitle());
        } else {
            holder.mBinding.getRoot().setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public void add(List<JobModel> jobs) {
        int index = this.jobs.size();
        this.jobs.addAll(jobs);
        notifyItemRangeInserted(index, jobs.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterJobBinding mBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = AdapterJobBinding.bind(itemView);
        }
    }
}
