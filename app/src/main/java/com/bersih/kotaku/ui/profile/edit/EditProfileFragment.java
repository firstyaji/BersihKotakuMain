package com.bersih.kotaku.ui.profile.edit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.FragmentEditProfileBinding;
import com.bersih.kotaku.firebase.model.User;
import com.bersih.kotaku.utils.GlideApp;
import com.bersih.kotaku.utils.GlideFirebaseStorage;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class EditProfileFragment extends Fragment {

    private EditProfileViewModel mViewModel;
    private FragmentEditProfileBinding viewBinding;
    private final ActivityResultLauncher<Intent> startImagePicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Intent data = result.getData();

        if (result.getResultCode() == Activity.RESULT_OK && data != null) {
            Uri fileUrl = data.getData();
            mViewModel.updateProfileImage(fileUrl);
        }
    });
    private final MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.select_date_of_birth)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build();
    private final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
            .withZone(ZoneId.systemDefault());

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        viewBinding.editProfileHeader.headerNavigationButton5.setOnClickListener(v -> gotoUp());
        viewBinding.editProfileHeader.headerTitle5.setText(R.string.profile);
        viewBinding.editProfileButtonUploadProfile.setOnClickListener(v -> requestImagePicker());

        mViewModel.listenUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) bindUser(user);
        });

        viewBinding.editProfileSave.setOnClickListener(v -> updateData());
    }

    void bindUser(User user) {
        GlideFirebaseStorage.smartLoad(viewBinding.getRoot(), user.profile, viewBinding.editProfileImage);
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_date_birth).into(viewBinding.editProfileDate.editProfileComponentImage);
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_gender).into(viewBinding.editProfileGender.editProfileComponentImage);
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_phone).into(viewBinding.editProfilePhone.editProfileComponentImage);
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_address).into(viewBinding.editProfileAddress.editProfileComponentImage);
      //  GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_address).into(viewBinding.editProfileAddressDetails.editProfileComponentImage);
        GlideApp.with(viewBinding.getRoot()).load(R.drawable.ic_job).into(viewBinding.editProfileJob.editProfileComponentImage);

        Optional.ofNullable(viewBinding.editProfilePhone.editProfileComponentTextInput.getEditText()).ifPresent(editText -> editText.setText(user.phoneNumber));
        Optional.ofNullable(viewBinding.editProfileAddress.editProfileComponentTextInput.getEditText()).ifPresent(editText -> editText.setText(user.AddressName));
        Optional.ofNullable(viewBinding.editProfileAddressDetails.editProfileComponentTextInput.getEditText()).ifPresent(editText -> editText.setText(user.AddressDetails));
        Optional.ofNullable(viewBinding.editProfileJob.editProfileComponentTextInput.getEditText()).ifPresent(editText -> editText.setText(user.job));
        viewBinding.editProfileGender.editProfileComponentTextInput.setText(parseGender(user.gender));
        viewBinding.editProfileDate.editProfileComponentTextInput.setText(user.dayBirth);

        viewBinding.editProfileGender.getRoot().setOnClickListener(v -> showGenderDialog());
        viewBinding.editProfileDate.getRoot().setOnClickListener(v -> showDatePicker());
    }

    private String parseGender(int gender) {
        if (gender == 1) return "Laki-laki";
        if (gender == 2) return "Perempuan";
        return "";
    }

    void updateData() {
        String date = viewBinding.editProfileDate.editProfileComponentTextInput.getText().toString();
        String gender = viewBinding.editProfileGender.editProfileComponentTextInput.getText().toString();
        String phone = Objects.requireNonNull(viewBinding.editProfilePhone.editProfileComponentTextInput.getEditText()).getText().toString();
        String address = Objects.requireNonNull(viewBinding.editProfileAddress.editProfileComponentTextInput.getEditText()).getText().toString();
        String addressDetails = Objects.requireNonNull(viewBinding.editProfileAddressDetails.editProfileComponentTextInput.getEditText()).getText().toString();
        String job = Objects.requireNonNull(viewBinding.editProfileJob.editProfileComponentTextInput.getEditText()).getText().toString();

        mViewModel.updateProfile(date, gender, phone, address, addressDetails, job);
        Toast.makeText(requireContext(), R.string.profile_updated, Toast.LENGTH_SHORT).show();
    }

    private void showDatePicker() {
        materialDatePicker.clearOnCancelListeners();
        materialDatePicker.clearOnDismissListeners();
        materialDatePicker.clearOnNegativeButtonClickListeners();
        materialDatePicker.clearOnPositiveButtonClickListeners();

        materialDatePicker.addOnPositiveButtonClickListener(epochMilli -> {
            Instant instant = Instant.ofEpochMilli(epochMilli);
            viewBinding.editProfileDate.editProfileComponentTextInput.setText(formatterDate.format(instant));
            materialDatePicker.dismiss();
        });
        materialDatePicker.show(getChildFragmentManager(), "date_time");
    }

    private void showGenderDialog() {
        String[] items = {"Laki-laki", "Perempuan"};
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.select_gender)
                .setCancelable(false)
                .setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, items), (dialogInterface, i) -> {
                    viewBinding.editProfileGender.editProfileComponentTextInput.setText(items[i]);
                    dialogInterface.dismiss();
                }).create().show();
    }

    void gotoUp() {
        NavHostFragment.findNavController(this).navigateUp();
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