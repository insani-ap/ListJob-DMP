package com.insani.listjobtest.modul.home.job.detail;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.insani.listjobtest.BaseActivity;
import com.insani.listjobtest.R;
import com.insani.listjobtest.core.model.JobModel;
import com.insani.listjobtest.databinding.ActivityJobDetailBinding;
import com.insani.listjobtest.util.Const;
import com.insani.listjobtest.util.Injection;
import com.insani.listjobtest.util.helper.ImageHelper;

public class JobDetailActivity extends BaseActivity implements JobDetailViewContract {
    private ActivityJobDetailBinding mBinding;
    private JobDetailPresenterContract mPresenter;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityJobDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initializeToolbar();
        initializeData();
    }

    private void initializeToolbar() {
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setTitle(getString(R.string.jobDetail));
        mBinding.toolbar.setNavigationOnClickListener((v) -> onBackPressed());
    }

    private void initializeData() {
        id = getIntent().getStringExtra(Const.Extras.ID);
        mPresenter = JobDetailPresenter.getInstance(Injection.provideJobRepo(getApplicationContext()), this);
        mPresenter.loadDetail(id);
    }

    @Override
    public void dataLoaded(JobModel job) {
        ImageHelper.loadImageViaGlide(getApplicationContext(), job.getCompanyLogo(), mBinding.logo, R.drawable.ic_no_image);
        mBinding.companyName.setText(job.getCompany());
        mBinding.location.setText(job.getLocation());

        Spannable mSpan = new SpannableString("Go to Website");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW);
                mIntent.setData(Uri.parse(job.getCompanyUrl()));
                startActivity(mIntent);
            }
        };
        mSpan.setSpan(clickableSpan, 0, mSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 0, mSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mBinding.website.setText(mSpan);
        mBinding.website.setMovementMethod(LinkMovementMethod.getInstance());

        mBinding.titleJob.setText(job.getTitle());
        if (job.getType().equalsIgnoreCase("Full Time"))
            mBinding.fulltimeJob.setText("Yes");
        else
            mBinding.fulltimeJob.setText("No");
        mBinding.descriptionJob.setText(Html.fromHtml(job.getDescription()));

    }

    @Override
    public void somethingError(String msg) {
        Snackbar.make(mBinding.getRoot(), msg, BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}
