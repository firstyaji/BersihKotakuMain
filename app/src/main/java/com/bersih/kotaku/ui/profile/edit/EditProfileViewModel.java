package com.bersih.kotaku.ui.profile.edit;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.bersih.kotaku.firebase.model.User;
import com.bersih.kotaku.firebase.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class EditProfileViewModel extends ViewModel {
    private final UserService userService;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject public EditProfileViewModel(UserService userService) {
        this.userService = userService;
    }

    public LiveData<@Nullable User> listenUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return null;
        }
        Observable<User> userObservable = this.userService.listenUser(firebaseUser.getEmail())
                .subscribeOn(Schedulers.io());
        return LiveDataReactiveStreams.fromPublisher(userObservable.toFlowable(BackpressureStrategy.LATEST));
    }

    public void updateProfileImage(Uri imageProfile) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        FirebaseStorage.getInstance()
                .getReference().child(String.format("users/%s/%s", firebaseUser.getUid(), imageProfile.getLastPathSegment()))
                .putFile(imageProfile).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(urlTask -> {
                            if (urlTask.isSuccessful()) {
                                disposable.add(
                                        this.userService.getUsers(firebaseUser.getEmail()).concatMap(user -> {
                                            if (user.isPresent()) {
                                                User data = user.get();
                                                data.profile = urlTask.getResult().toString();
                                                return this.userService.updateUsers(data);
                                            } else {
                                                return Observable.error(new Throwable("can't find use while update profile image"));
                                            }
                                        }).observeOn(Schedulers.io()).onErrorComplete().subscribe()
                                );
                            }
                        });
                    } else {
                        Timber.e(task.getException());
                    }
                });
    }

    public void updateProfile(String date, String gender, String phone, String address, String addressDetails, String job) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
     //   String[] addressList = address.split(",");
        disposable.add(
                this.userService.getUsers(firebaseUser.getEmail()).concatMap(user -> {
                    if (user.isPresent()) {
                        User data = user.get();
                        data.dayBirth = date;
                        data.gender = parseGender(gender);
                        data.phoneNumber = phone;
                        data.AddressName = address;
                        data.AddressDetails = addressDetails;
                        data.job = job;
                        return this.userService.updateUsers(data);
                    } else {
                        return Observable.error(new Throwable("can't find use while update profile data"));
                    }
                }).observeOn(Schedulers.io()).onErrorComplete().subscribe()
        );
    }

    private int parseGender(String gender) {
        if (gender.equals("Laki-laki")) return 1;
        if (gender.equals("Perempuan")) return 2;
        return -1;
    }
}