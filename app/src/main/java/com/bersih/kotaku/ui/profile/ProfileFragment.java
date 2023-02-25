package com.bersih.kotaku.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.FragmentProfileBinding;
import com.bersih.kotaku.utils.GlideApp;
import com.bersih.kotaku.utils.GlideFirebaseStorage;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding viewBinding;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProfileViewModel mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewBinding.profileHeader2.headerNavigationButton2.setVisibility(View.INVISIBLE);
        viewBinding.profileHeader2.headerTitle2.setText(R.string.profile_header);

        viewBinding.profileEdit.profileMenuText.setText(R.string.edit_profile);
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_profile_edit).into(viewBinding.profileEdit.profileMenuIcon);
        viewBinding.profileEdit.getRoot().setOnClickListener(v -> gotoEditProfile());

        viewBinding.profileShow.profileMenuText.setText(R.string.show_profile);
      //  GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_infromation_account).into(viewBinding.profileShow.profileMenuIcon);
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_view_details).into(viewBinding.profileShow.profileMenuIcon);
        viewBinding.profileShow.getRoot().setOnClickListener(v -> gotoShowProfile());

        viewBinding.profileSignOut.setOnClickListener(v -> {});

        mViewModel.listenUser().observe(getViewLifecycleOwner(), value -> {
            if (value != null) {
                GlideFirebaseStorage.smartLoad(viewBinding.getRoot(), value.profile, viewBinding.profileImage);
            }
        });

        viewBinding.profileSignOut.setOnClickListener(v -> {
            AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                   NavHostFragment.findNavController(this).navigate(R.id.login_nav, null,
                           new NavOptions.Builder()
                                   .setLaunchSingleTop(true)
                                   .setPopUpTo(R.id.login_nav, true)
                                   .build());
                }
            });
        });
    }

    void gotoEditProfile() {
        NavHostFragment.findNavController(this).navigate(R.id.editProfileFragment);
    }

    void gotoShowProfile() {
        NavHostFragment.findNavController(this).navigate(R.id.showProfileFragment);
    }
}