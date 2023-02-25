package com.insani.listjobtest.modul.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.insani.listjobtest.BaseActivity;
import com.insani.listjobtest.databinding.ActivityLoginBinding;
import com.insani.listjobtest.modul.home.HomeActivity;
import com.insani.listjobtest.util.Const;

import java.util.Collections;
import java.util.Objects;

public class LoginActivity extends BaseActivity implements LoginViewContract {
    private ActivityLoginBinding mBinding;

    private GoogleSignInClient mGoogleSignInClient;
    private final ActivityResultLauncher<Intent> mResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_CANCELED) return;

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            loginSuccess(account);
        } catch (ApiException e) {
            loginFailed(e.getMessage());
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initializeGoogle();
        initializeFacebook();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            loginSuccess(Profile.getCurrentProfile());
        } else if (GoogleSignIn.getLastSignedInAccount(getApplicationContext()) != null) {
            loginSuccess(GoogleSignIn.getLastSignedInAccount(getApplicationContext()));
        }
    }

    private void initializeGoogle() {
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build());
        mBinding.googleLogin.setOnClickListener(v -> mResult.launch(mGoogleSignInClient.getSignInIntent()));
    }

    private void initializeFacebook() {
        mBinding.facebookLogin.setPermissions(Collections.singletonList("public_profile"));
        mBinding.facebookLogin.registerCallback(CallbackManager.Factory.create(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ProfileTracker profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        Profile.setCurrentProfile(currentProfile);
                        this.stopTracking();
                        loginSuccess(Profile.getCurrentProfile());
                    }
                };
                profileTracker.startTracking();
            }

            @Override
            public void onCancel() {
                //Do nothing
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                loginFailed(e.getMessage());
            }
        });
    }

    @Override
    public void loginSuccess(Object o) {
        Intent mIntent = new Intent(getApplicationContext(), HomeActivity.class);
        if (o instanceof GoogleSignInAccount) {
            GoogleSignInAccount account = (GoogleSignInAccount) o;
            mIntent.putExtra(Const.Extras.NAME, account.getDisplayName());
            mIntent.putExtra(Const.Extras.IMAGE, Objects.requireNonNull(account.getPhotoUrl()).toString());
        } else if (o instanceof Profile) {
            Profile profile = (Profile) o;
            mIntent.putExtra(Const.Extras.NAME, profile.getName());
            mIntent.putExtra(Const.Extras.IMAGE, profile.getProfilePictureUri(100, 100).toString());
        }

        startActivity(mIntent);
        finish();
    }

    @Override
    public void loginFailed(String msg) {
        Snackbar.make(mBinding.getRoot(), msg, BaseTransientBottomBar.LENGTH_LONG).show();
    }
}
