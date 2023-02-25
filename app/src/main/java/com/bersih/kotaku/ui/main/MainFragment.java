package com.bersih.kotaku.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bersih.kotaku.R;
import com.bersih.kotaku.adapter.HomeController;
import com.bersih.kotaku.databinding.FragmentMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private FragmentMainBinding viewBinding;
    private HomeController homeController;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentMainBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        homeController = new HomeController(requireContext());

        homeController.setOnClickListenerAction(this::gotoPackList);
        homeController.setOnClickHomeSubsInactive(this::gotoPackList);
        homeController.setOnClickRecommendItem(recommend -> gotoPackList());
        homeController.setNotificationClickListener(this::gotoNotification);

        viewBinding.mainEpoxy.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        viewBinding.mainEpoxy.setControllerAndBuildModels(homeController);

        mViewModel.getUser().observe(getViewLifecycleOwner(), homeController::setUser);
        mViewModel.getRecommendPack().observe(getViewLifecycleOwner(), homeController::setRecommends);
        mViewModel.getCurrentActiveSubs().observe(getViewLifecycleOwner(), value -> {
            value.ifPresent(homeController::setSubscription);
        });
    }

    private void gotoPackList() {
        NavHostFragment.findNavController(this).navigate(R.id.packFragment);
    }

    void gotoNotification() {
        NavHostFragment.findNavController(this).navigate(R.id.notificationFragment);
    }
}