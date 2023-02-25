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
import com.insani.listjobtest.databinding.AdapterProgressBinding;
import com.insani.listjobtest.modul.home.job.JobListPresenterContract;
import com.insani.listjobtest.util.helper.ImageHelper;

import java.util.Collections;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private final List<JobModel> jobs;
    private final Context mContext;
    private final JobListPresenterContract mPresenter;

    public JobAdapter(List<JobModel> jobs, Context mContext, JobListPresenterContract mPresenter) {
        this.jobs = jobs;
        this.mContext = mContext;
        this.mPresenter = mPresenter;
    }

    @Override
    public int getItemViewType(int position) {
        return jobs.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_job, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_progress, parent, false);
            return new ProgressViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            configureData(((ViewHolder) holder).mBinding, position);
        } else if (holder instanceof ProgressViewHolder) {
            configureLoading(((ProgressViewHolder) holder).mBinding);
        }
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public void add(List<JobModel> jobs) {
        int index = this.jobs.size();
        this.jobs.removeAll(Collections.singletonList(null));
        this.jobs.addAll(jobs);
        notifyItemRangeInserted(index, jobs.size());
    }

    public void addLoading() {
        this.jobs.add(null);
        notifyItemInserted(this.jobs.size() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterJobBinding mBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = AdapterJobBinding.bind(itemView);
        }
    }

    private void configureData(AdapterJobBinding mBinding, int position) {
        JobModel job = jobs.get(position);
        if (job != null) {
            mBinding.getRoot().setOnClickListener(view -> mPresenter.openDetail(job.getId()));

            ImageHelper.loadImageViaGlide(mContext, job.getCompanyLogo(), mBinding.logo, R.drawable.ic_no_image);
            mBinding.company.setText(job.getCompany());
            mBinding.location.setText(job.getLocation());
            mBinding.role.setText(job.getTitle());
        } else {
            mBinding.getRoot().setVisibility(View.GONE);
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        private final AdapterProgressBinding mBinding;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = AdapterProgressBinding.bind(itemView);
        }
    }

    private void configureLoading(AdapterProgressBinding mBinding) {
        mBinding.progressBar.setVisibility(View.VISIBLE);
    }
}
