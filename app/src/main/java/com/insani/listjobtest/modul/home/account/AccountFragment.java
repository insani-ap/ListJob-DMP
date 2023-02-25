package com.insani.listjobtest.modul.home.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.insani.listjobtest.BaseFragment;
import com.insani.listjobtest.R;
import com.insani.listjobtest.databinding.FragmentAccountBinding;
import com.insani.listjobtest.modul.login.LoginActivity;
import com.insani.listjobtest.util.Const;
import com.insani.listjobtest.util.helper.ImageHelper;

public class AccountFragment extends BaseFragment {
    private FragmentAccountBinding mBinding;

    public AccountFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAccountBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeData();
    }

    private void initializeData() {
        String name = requireActivity().getIntent().getStringExtra(Const.Extras.NAME);
        String image = requireActivity().getIntent().getStringExtra(Const.Extras.IMAGE);

        mBinding.name.setText(name);
        ImageHelper.loadImageViaGlide(requireContext(), image, mBinding.image, R.drawable.ic_person_circle);
        mBinding.logout.setOnClickListener((v) -> {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            } else if (GoogleSignIn.getLastSignedInAccount(requireContext()) != null) {
                GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), options);
                mGoogleSignInClient.signOut();
            }

            requireActivity().startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finishAffinity();
        });
    }
}
