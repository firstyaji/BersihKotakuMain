package com.bersih.kotaku.ui.notification;

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
import com.bersih.kotaku.adapter.NotificationController;
import com.bersih.kotaku.databinding.FragmentNotificationBinding;
import com.bersih.kotaku.firebase.model.Notification;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NotificationFragment extends Fragment {

    private NotificationViewModel mViewModel;
    private FragmentNotificationBinding viewBinding;
    private final NotificationController notificationController = new NotificationController();

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentNotificationBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        viewBinding.notificationHeader.headerNavigationButton4.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        viewBinding.notificationHeader.headerTitle4.setText(R.string.notification);

        notificationController.setOnClickListener(this::gotoHistory);

        viewBinding.notificationEpoxy.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        viewBinding.notificationEpoxy.setControllerAndBuildModels(notificationController);

        mViewModel.listenNotification().observe(getViewLifecycleOwner(), notificationController::setNotificationsList);

        viewBinding.notificationScrollUp.setOnClickListener(v -> {
            viewBinding.notificationEpoxy.scrollToPosition(0);
        });
    }

    void gotoHistory(Notification notification) {
        mViewModel.read(notification);
    }
}