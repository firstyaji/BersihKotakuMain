package com.bersih.kotaku.ui.profile.show;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.FragmentShowProfileBinding;
import com.bersih.kotaku.firebase.model.User;
import com.bersih.kotaku.utils.GlideApp;

import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ShowProfileFragment extends Fragment {

    private FragmentShowProfileBinding viewBinding;

    public static ShowProfileFragment newInstance() {
        return new ShowProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentShowProfileBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ShowProfileViewModel mViewModel = new ViewModelProvider(this).get(ShowProfileViewModel.class);

        viewBinding.showProfileHeader.headerTitle3.setText(R.string.account_information);
        viewBinding.showProfileHeader.headerNavigationButton3.setOnClickListener(v -> gotoUp());
        mViewModel.listenUser().observe(getViewLifecycleOwner(), value -> {
            if (value != null) {
                bindUser(value);
            }
        });
    }

    void bindUser(User user) {
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_name).circleCrop().into(viewBinding.showProfileName.viewProfileComponentImage);
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_email).into(viewBinding.showProfileEmail.viewProfileComponentImage);
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_date_birth).into(viewBinding.showProfileDate.viewProfileComponentImage);
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_gender).into(viewBinding.showProfileGender.viewProfileComponentImage);
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_phone).into(viewBinding.showProfilePhone.viewProfileComponentImage);
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_address).into(viewBinding.showProfileAddress.viewProfileComponentImage);
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_job).into(viewBinding.showProfileJob.viewProfileComponentImage);

        viewBinding.showProfileName.viewProfileComponentText.setText(user.fullName);
        viewBinding.showProfileEmail.viewProfileComponentText.setText(user.email);
        viewBinding.showProfileDate.viewProfileComponentText.setText(user.dayBirth);
        viewBinding.showProfileGender.viewProfileComponentText.setText(parseGender(user.gender));
        viewBinding.showProfilePhone.viewProfileComponentText.setText(user.phoneNumber);
        viewBinding.showProfileAddress.viewProfileComponentText.setText(user.AddressName);
        viewBinding.showProfileJob.viewProfileComponentText.setText(user.job);

        viewBinding.showProfilePhone.viewProfileComponentButton.setVisibility(View.INVISIBLE);
        viewBinding.showProfileAddress.viewProfileComponentButton.setVisibility(View.INVISIBLE);
        viewBinding.showProfileJob.viewProfileComponentButton.setVisibility(View.INVISIBLE);
        viewBinding.showProfileDate.viewProfileComponentButton.setVisibility(View.INVISIBLE);
        viewBinding.showProfileGender.viewProfileComponentButton.setVisibility(View.INVISIBLE);
    }

    void gotoUp() {
        NavHostFragment.findNavController(this).navigateUp();
    }

    String parseGender(int gender) {
        if (gender == 1) return "Male";
        else if (gender == 2) return "Female";
        else return "Male";
    }
}