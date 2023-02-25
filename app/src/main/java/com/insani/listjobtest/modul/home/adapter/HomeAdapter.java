package com.insani.listjobtest.modul.home.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.insani.listjobtest.modul.home.account.AccountFragment;
import com.insani.listjobtest.modul.home.job.JobListFragment;

public class HomeAdapter extends FragmentPagerAdapter {
    public HomeAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment mFragment;
        switch (position) {
            case 0:
                mFragment = new JobListFragment();
                break;
            case 1:
                mFragment = new AccountFragment();
                break;
            default:
                return new Fragment();
        }

        return mFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
