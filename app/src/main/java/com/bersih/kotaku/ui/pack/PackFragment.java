package com.bersih.kotaku.ui.pack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bersih.kotaku.adapter.PackController;
import com.bersih.kotaku.databinding.FragmentPackBinding;
import com.bersih.kotaku.firebase.model.Pack;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PackFragment extends Fragment {

    private FragmentPackBinding viewBinding;
    private PackController packController;

    public static PackFragment newInstance() {
        return new PackFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentPackBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PackViewModel mViewModel = new ViewModelProvider(this).get(PackViewModel.class);

        packController = new PackController(requireContext());

        viewBinding.packEpoxy.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        viewBinding.packEpoxy.setControllerAndBuildModels(packController);

        packController.setItemPackOnClickListener(this::gotoPayment);
        packController.setHeaderbackhomeOnclick(this::requestBackhome);

        mViewModel.getPack().observe(getViewLifecycleOwner(), packController::setPackList);
        mViewModel.listenUser().observe(getViewLifecycleOwner(), packController::setUser);
        mViewModel.getCurrentActiveSubs().observe(getViewLifecycleOwner(), v -> {
            v.ifPresent(pack -> packController.setSubscription(pack));
        });
    }

    private void gotoPayment(Pack pack) {
        NavHostFragment.findNavController(this).navigate(PackFragmentDirections.actionPackFragmentToPaymentFragment(pack.id));
    }

    private void requestBackhome() {
        NavHostFragment.findNavController(this).navigateUp();

    }
}