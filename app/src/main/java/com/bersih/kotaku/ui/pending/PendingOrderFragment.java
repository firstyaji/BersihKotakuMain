package com.bersih.kotaku.ui.pending;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bersih.kotaku.R;
import com.bersih.kotaku.adapter.ConfirmationController;
import com.bersih.kotaku.databinding.FragmentPendingOrderBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PendingOrderFragment extends Fragment {

    private FragmentPendingOrderBinding viewBinding;

    public static PendingOrderFragment newInstance() {
        return new PendingOrderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentPendingOrderBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PendingOrderViewModel mViewModel = new ViewModelProvider(this).get(PendingOrderViewModel.class);

        ConfirmationController confirmationController = new ConfirmationController(requireContext());

        confirmationController.setOnClickListener(this::gotoHome);

        viewBinding.pendingOrderEpoxy.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        viewBinding.pendingOrderEpoxy.setControllerAndBuildModels(confirmationController);
    }

    private void gotoHome() {
        NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build();
        NavHostFragment.findNavController(this).navigate(R.id.homeFragment, null, options);
    }
}