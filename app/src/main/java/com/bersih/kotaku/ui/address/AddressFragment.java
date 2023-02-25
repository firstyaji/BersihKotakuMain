package com.bersih.kotaku.ui.address;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.FragmentAddressBinding;

import java.util.Objects;
import java.util.Optional;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddressFragment extends Fragment {

    private AddressViewModel mViewModel;
    private FragmentAddressBinding viewBinding;

    public static AddressFragment newInstance() {
        return new AddressFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentAddressBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        mViewModel.getUser().observe(getViewLifecycleOwner(), value -> {
            Optional.ofNullable(value).ifPresent( user -> {
                Optional.ofNullable(viewBinding.addressFragmentNameContent.getEditText()).ifPresent( editText -> {
                    editText.setText(user.AddressName);
                });
                Optional.ofNullable(viewBinding.addressFragmentDetailsContent.getEditText()).ifPresent( editText -> {
                    editText.setText(user.AddressDetails);
                });
            });
        });

        viewBinding.addressFragmentSave.setOnClickListener( button -> {
            String addressName = Objects.requireNonNull(viewBinding.addressFragmentNameContent.getEditText()).getText().toString();
            String addressDetails = Objects.requireNonNull(viewBinding.addressFragmentDetailsContent.getEditText()).getText().toString();
            mViewModel.updateAddress(addressName, addressDetails);
            Toast.makeText(requireContext(), R.string.address_updated, Toast.LENGTH_SHORT).show();
        });

        viewBinding.addressFragmentHeaderNavigation.setOnClickListener( button -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
    }
}