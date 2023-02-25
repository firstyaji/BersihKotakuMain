package com.bersih.kotaku.service;

import androidx.annotation.NonNull;

import com.bersih.kotaku.firebase.model.User;
import com.bersih.kotaku.firebase.service.UserService;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import timber.log.Timber;

@AndroidEntryPoint
public class NotificationService extends FirebaseMessagingService {

    @Inject public UserService userService;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FirebaseUser currentUser = userService.loadUser();
        if (currentUser == null) return;

        disposable.add(
                userService.getUsers(currentUser.getEmail()).concatMap(v -> {
                    if (!v.isPresent()) return Observable.error(new Throwable("user not found in database"));
                    User userData = v.get();
                    userData.firebaseToken = token;
                    return userService.updateUsers(userData);
                }).onErrorComplete().subscribe()
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
