package com.insani.listjobtest.modul.home;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.insani.listjobtest.BaseActivity;
import com.insani.listjobtest.R;
import com.insani.listjobtest.databinding.ActivityHomeBinding;
import com.insani.listjobtest.modul.home.adapter.HomeAdapter;

import java.util.Objects;

public class HomeActivity extends BaseActivity {
    private ActivityHomeBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initializeLayout();
        initializeToolbar(1);
        initializeTab();
    }

    private void initializeLayout() {
        HomeAdapter mAdapter = new HomeAdapter(getSupportFragmentManager());
        mBinding.vPager.setAdapter(mAdapter);
        mBinding.vPager.setCurrentItem(1);

        mBinding.tLayout.setupWithViewPager(mBinding.vPager);
        mBinding.tLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0 || tab.getPosition() == 1) {
                    initializeToolbar(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void initializeToolbar(int pos) {
        switch (pos) {
            case 0:
                mBinding.toolbar.setTitle(getString(R.string.jobListTitle));
                break;
            case 1:
                mBinding.toolbar.setTitle(getString(R.string.accountTitle));
                break;
            default:
                break;
        }

        setSupportActionBar(mBinding.toolbar);
    }

    private void initializeTab() {
        Objects.requireNonNull(mBinding.tLayout.getTabAt(0)).setText(getString(R.string.jobListLabel));
        Objects.requireNonNull(mBinding.tLayout.getTabAt(0)).setIcon(R.drawable.ic_home);
        Objects.requireNonNull(mBinding.tLayout.getTabAt(1)).setText(getString(R.string.accountLabel));
        Objects.requireNonNull(mBinding.tLayout.getTabAt(1)).setIcon(R.drawable.ic_person);
    }
}
