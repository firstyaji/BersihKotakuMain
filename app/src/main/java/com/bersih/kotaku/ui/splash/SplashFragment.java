package com.bersih.kotaku.ui.splash;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bersih.kotaku.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashFragment extends Fragment {

    private SplashViewModel mViewModel;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SplashViewModel.class);

        //startPage(NavHostFragment.findNavController(this));
        new Thread(() -> {
            // startPage(NavHostFragment.findNavController(this));
            try {
                TimeUnit.SECONDS.sleep(5);
                getActivity().runOnUiThread(() -> {
                    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                        NavHostFragment.findNavController(this).navigate(R.id.login_nav,null,
                                new NavOptions.Builder()
                                        .setLaunchSingleTop(true)
                                        .setPopUpTo(R.id.login_nav, true)
                                        .build());
                    } else {
                        NavHostFragment.findNavController(this).navigate(R.id.nav_graph, null,
                                new NavOptions.Builder()
                                        .setLaunchSingleTop(true)
                                        .setPopUpTo(R.id.nav_graph, true)
                                        .build());
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}