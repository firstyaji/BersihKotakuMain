package com.bersih.kotaku.ui.history;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bersih.kotaku.adapter.HistoryController;
import com.bersih.kotaku.databinding.FragmentHistoryBinding;
import com.bersih.kotaku.firebase.model.Picker;
import com.bersih.kotaku.firebase.model.Subscription;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HistoryFragment extends Fragment {

    private HistoryViewModel mViewModel;
    private FragmentHistoryBinding viewBinding;
    private HistoryController historyController;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentHistoryBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        historyController  = new HistoryController(requireContext());

        historyController.setPickOnClickListener(picker -> {});
        historyController.setSubsOnClickListener(subscription -> {});
        historyController.setTabOnClickListener(this::setSelected);

        viewBinding.historyEpoxy.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        viewBinding.historyEpoxy.setControllerAndBuildModels(historyController);

        mViewModel.listSubs().observe(getViewLifecycleOwner(), historyController::setSubscriptionList);
        mViewModel.listPicker().observe(getViewLifecycleOwner(), historyController::setPickerList);
        mViewModel.selected.observe(getViewLifecycleOwner(), historyController::setSelected);
        mViewModel.menuList.observe(getViewLifecycleOwner(), historyController::setTabMenu);
    }

    void setSelected(String value) {
        mViewModel.setSelected(value);
    }

    void deletePicker(Picker picker) {

    }

    void deleteSubs(Subscription subscription) {

    }
}