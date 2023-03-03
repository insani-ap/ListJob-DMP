package com.insani.listjobtest.modul.home.job;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.insani.listjobtest.BaseFragment;
import com.insani.listjobtest.R;
import com.insani.listjobtest.core.model.JobModel;
import com.insani.listjobtest.databinding.FragmentJobListBinding;
import com.insani.listjobtest.modul.home.job.adapter.JobAdapter;
import com.insani.listjobtest.modul.home.job.detail.JobDetailActivity;
import com.insani.listjobtest.modul.home.job.vm.JobViewModel;
import com.insani.listjobtest.util.Const;
import com.insani.listjobtest.util.Injection;

import java.util.Collections;
import java.util.List;

import retrofit2.HttpException;

public class JobListFragment extends BaseFragment implements JobListViewContract {
    private FragmentJobListBinding mBinding;
    private JobListPresenterContract mPresenter;

    private JobAdapter allAdapter;
    private JobAdapter searchAdapter;

    private int page = 1;
    private boolean isDone = false;

    private JobViewModel jobViewModel;

    public JobListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentJobListBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeLayout();
        initializeData();
    }

    private void initializeLayout() {
        mBinding.expand.setOnClickListener(v -> {
            if (mBinding.findRv.getVisibility() == View.VISIBLE) {
                Animation rotation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_icon_chevron_up_to_down);
                rotation.setRepeatCount(Animation.INFINITE);
                mBinding.expand.startAnimation(rotation);
                mBinding.expand.setImageResource(R.drawable.ic_chevron_down);
                mBinding.findRv.setVisibility(View.GONE);

                mBinding.search.setQuery("", false);
                mBinding.switchFulltime.setChecked(false);
                mBinding.etLocation.setText("");
                mBinding.rvJobs.setAdapter(allAdapter);
            } else {
                Animation rotation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_icon_chevron_down_to_up);
                rotation.setRepeatCount(Animation.INFINITE);
                mBinding.expand.startAnimation(rotation);
                mBinding.expand.setImageResource(R.drawable.ic_chevron_up);
                mBinding.findRv.setVisibility(View.VISIBLE);
            }
        });
        mBinding.find.setOnClickListener((v) -> {
            mPresenter.findList(mBinding.search.getQuery().toString(), mBinding.switchFulltime.isChecked(), mBinding.etLocation.getText().toString());
        });
    }

    private void initializeData() {
        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);
        jobViewModel.setmRepo(Injection.provideJobRepo(requireContext()));
        jobViewModel.getData(page);
        jobViewModel.getJobList().observe(getViewLifecycleOwner(), this::listLoaded);

        mPresenter = JobListPresenter.getInstance(Injection.provideJobRepo(requireContext()), this);
    }

    @Override
    public void listLoaded(List<JobModel> jobs) {
        if (jobs.isEmpty()) return;

        jobs.removeAll(Collections.singletonList(null));
        if (allAdapter == null) {
            allAdapter = new JobAdapter(jobs, requireContext(), mPresenter);
            mBinding.rvJobs.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
            mBinding.rvJobs.setAdapter(allAdapter);
            mBinding.rvJobs.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (mBinding.findRv.getVisibility() == View.VISIBLE)
                        return;

                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (isDone) {
                            Snackbar.make(mBinding.getRoot(), "All data already loaded", Snackbar.LENGTH_LONG).show();
                        } else {
                            allAdapter.addLoading();
                            new Handler().postDelayed(() -> mPresenter.loadList(++page), 2000);
                        }
                    }
                }
            });
        } else {
            allAdapter.add(jobs);
        }
    }

    @Override
    public void listFound(List<JobModel> jobs) {
        searchAdapter = new JobAdapter(jobs, requireContext(), mPresenter);
        mBinding.rvJobs.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.rvJobs.setAdapter(searchAdapter);
    }

    @Override
    public void listOpened(String id) {
        Intent mIntent = new Intent(requireContext(), JobDetailActivity.class);
        mIntent.putExtra(Const.Extras.ID, id);
        startActivity(mIntent);
    }

    @Override
    public void somethingError(String msg, Throwable t) {
        if (t instanceof HttpException) {
            HttpException exception = (HttpException) t;
            if (exception.code() == 500) isDone = true;
            //Patched due to 500 Err
            Snackbar.make(mBinding.getRoot(), "All data already loaded", Snackbar.LENGTH_LONG).show();
            return;
        }

        Snackbar.make(mBinding.getRoot(), msg, BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}
