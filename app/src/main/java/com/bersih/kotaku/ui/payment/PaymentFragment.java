package com.bersih.kotaku.ui.payment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.bersih.kotaku.adapter.PaymentController;
import com.bersih.kotaku.databinding.FragmentPaymentBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PaymentFragment extends Fragment {

    private PaymentViewModel mViewModel;
    private FragmentPaymentBinding viewBinding;
    private PaymentController paymentController;
    private final ActivityResultLauncher<Intent> startImagePicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Intent data = result.getData();

        if (result.getResultCode() == Activity.RESULT_OK && data != null) {
            Uri fileUrl = data.getData();
            mViewModel.setCurrentReceipt(fileUrl);
        }
    });

    public static PaymentFragment newInstance() {
        return new PaymentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentPaymentBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);

        String packID = PaymentFragmentArgs.fromBundle(getArguments()).getPackID();

        paymentController = new PaymentController(requireContext());

        paymentController.setPaymentOnClick(v -> setPayment(v.id));
        paymentController.setButtonPayOnClick(this::orderSubs);
        paymentController.setAddAddressOnClick(this::gotoAddAddress);
        paymentController.setHeaderModelOnClick(this::requestBackPress);
        paymentController.setRequestReceiptOnClick(this::requestImagePicker);

        viewBinding.paymentEpoxy.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        viewBinding.paymentEpoxy.setControllerAndBuildModels(paymentController);

        mViewModel.listenUser().observe(getViewLifecycleOwner(), paymentController::setUser);
        mViewModel.listenReceipt().observe(getViewLifecycleOwner(), paymentController::setPaymentReceipt);
        mViewModel.listenPayment().observe(getViewLifecycleOwner(), paymentController::setPaymentList);
        mViewModel.isFilled().observe(getViewLifecycleOwner(), paymentController::setFilled);
        mViewModel.listenPack().observe(getViewLifecycleOwner(), value -> value.ifPresent(paymentController::setPack) );
        mViewModel.listenNavigation().observe(getViewLifecycleOwner(), this::gotoPending);

        mViewModel.setPack(packID);
    }

    private void gotoPending(Boolean value) {
        if (value) {
            mViewModel.consumeNavigate();
            NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build();
            NavHostFragment.findNavController(this).navigate(R.id.pendingOrderFragment, null, options);
        }
    }

    private void orderSubs() {
        mViewModel.orderSubs();
    }

    private void requestBackPress() {
        NavHostFragment.findNavController(this).navigateUp();
    }

    private void gotoAddAddress() {
        NavHostFragment.findNavController(this).navigate(R.id.addressFragment);
    }

    private void setPayment(String paymentID) {
        mViewModel.setSelectedPayment(paymentID);
    }

    private void requestImagePicker() {
        ImagePicker.with(this)
                .compress(1024)
                .createIntent(intent -> {
                    startImagePicker.launch(intent);
                    return null;
                });
    }
}