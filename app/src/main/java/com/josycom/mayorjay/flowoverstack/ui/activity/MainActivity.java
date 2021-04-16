package com.josycom.mayorjay.flowoverstack.ui.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.josycom.mayorjay.flowoverstack.R;
import com.josycom.mayorjay.flowoverstack.databinding.ActivityMainBinding;
import com.josycom.mayorjay.flowoverstack.ui.fragment.QuestionsByActivityFragment;
import com.josycom.mayorjay.flowoverstack.ui.fragment.QuestionsByCreationFragment;
import com.josycom.mayorjay.flowoverstack.ui.fragment.QuestionsByHotFragment;
import com.josycom.mayorjay.flowoverstack.ui.fragment.QuestionsByVoteFragment;
import com.josycom.mayorjay.flowoverstack.util.AppConstants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class MainActivity extends AppCompatActivity implements HasAndroidInjector {

    @Inject
    DispatchingAndroidInjector<Object> dispatchingAndroidInjector;


    private FragmentTransaction mFragmentTransaction;
    private boolean isFragmentDisplayed = false;
    private boolean isFabOpen = false;
    private ActivityMainBinding mActivityMainBinding;
    private Animation fabOpen, fabClose;
    private AppUpdateManager mAppUpdateManager;
    private static final int APP_UPDATE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());
        setSupportActionBar(mActivityMainBinding.toolbar);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        checkForTablet();

        mActivityMainBinding.searchFab.setOnClickListener(view -> fabAction());

        mActivityMainBinding.scanToSearch.setOnClickListener(view -> {
            startActivity(new Intent(this, OcrActivity.class));
            hideFabActions();
        });
        mActivityMainBinding.typeToSearch.setOnClickListener(view -> {
            startActivity(new Intent(this, SearchActivity.class));
            hideFabActions();
        });

        if (savedInstanceState != null) {
            isFragmentDisplayed = savedInstanceState.getBoolean(AppConstants.FRAGMENT_STATE);
        }

        if (!isFragmentDisplayed) {
            if (findViewById(R.id.fragment_container) != null) {
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.add(R.id.fragment_container, new QuestionsByActivityFragment()).commit();
                isFragmentDisplayed = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForUpdate();
    }

    private void fabAction() {
        if (isFabOpen) {
            hideFabActions();
        } else {
            showFabActions();
        }
    }

    private void hideFabActions() {
        mActivityMainBinding.searchFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_search));
        mActivityMainBinding.scanToSearch.startAnimation(fabClose);
        mActivityMainBinding.scanToSearch.setClickable(false);
        mActivityMainBinding.scanToSearch.setVisibility(View.INVISIBLE);
        mActivityMainBinding.typeToSearch.startAnimation(fabClose);
        mActivityMainBinding.typeToSearch.setClickable(false);
        mActivityMainBinding.typeToSearch.setVisibility(View.INVISIBLE);
        isFabOpen = false;
    }

    private void showFabActions() {
        mActivityMainBinding.searchFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close));
        mActivityMainBinding.scanToSearch.startAnimation(fabOpen);
        mActivityMainBinding.scanToSearch.setClickable(true);
        mActivityMainBinding.scanToSearch.setVisibility(View.VISIBLE);
        mActivityMainBinding.typeToSearch.startAnimation(fabOpen);
        mActivityMainBinding.typeToSearch.setClickable(true);
        mActivityMainBinding.typeToSearch.setVisibility(View.VISIBLE);
        isFabOpen = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter_by_recency) {
            if (findViewById(R.id.fragment_container) != null && item.getTitle().equals(getString(R.string.action_filter_by_recency))) {
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, new QuestionsByCreationFragment()).commit();
                item.setTitle(R.string.action_filter_by_activity);
            } else if (findViewById(R.id.fragment_container) != null && item.getTitle().equals(getString(R.string.action_filter_by_activity))) {
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, new QuestionsByActivityFragment()).commit();
                item.setTitle(R.string.action_filter_by_recency);
            }
            return true;
        } else if (id == R.id.action_filter_by_hot) {
            if (findViewById(R.id.fragment_container) != null) {
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, new QuestionsByHotFragment()).commit();
            }
        } else if (id == R.id.action_filter_by_vote) {
            if (findViewById(R.id.fragment_container) != null) {
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, new QuestionsByVoteFragment()).commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AppConstants.FRAGMENT_STATE, isFragmentDisplayed);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return dispatchingAndroidInjector;
    }

    private void checkForTablet() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            mActivityMainBinding.scanToSearch.setTextSize(15F);
            mActivityMainBinding.typeToSearch.setTextSize(15F);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    private InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(@NonNull InstallState state) {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                if (mAppUpdateManager != null) {
                    mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                }
            } else {
                Log.i("UpdateInstaller", "InstallStateUpdatedListener >>>>> " + state.installStatus());
            }
        }
    };

    private void checkForUpdate() {
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        mAppUpdateManager.registerListener(installStateUpdatedListener);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, MainActivity.this, APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            } else {
                Log.e("UpdateInstaller", "checkForAppUpdateAvailability >>>>>" + appUpdateInfo.installStatus());
            }
        });
    }

    private void popupSnackBarForCompleteUpdate() {
        Snackbar snackbar = Snackbar.make(
                        findViewById(R.id.main_root_layout),
                        "An update has just been downloaded",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Restart", view -> {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimaryText));
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                checkForUpdate();
            }
        }
    }
}
