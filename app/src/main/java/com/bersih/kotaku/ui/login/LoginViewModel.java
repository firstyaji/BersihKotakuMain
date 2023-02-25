package com.bersih.kotaku.ui.login;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.bersih.kotaku.firebase.model.User;
import com.bersih.kotaku.firebase.service.UserService;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.time.Instant;
import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import timber.log.Timber;

class LoginEvent {
    static class Idle extends LoginEvent{}
    static class LoginSuccess extends LoginEvent{
        User user;
        LoginSuccess(User user) { this.user = user; }
    }
    static class LoginFailed extends LoginEvent{
        String message;
        LoginFailed(String message){ this.message = message; }
    }
}

@HiltViewModel
public class LoginViewModel extends ViewModel {
    private final PublishSubject<LoginEvent> event = PublishSubject.create();
    private final UserService userService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public Observable<LoginEvent> getEvent() {
        return event.observeOn(AndroidSchedulers.mainThread());
    }

    @Inject
    LoginViewModel(UserService userService){
        this.userService = userService;
    }

    public void loginRegister(FirebaseUser firebaseUser) {
        event.onNext(new LoginEvent.Idle());
        compositeDisposable.add(this.userService.getUsers(firebaseUser.getEmail())
                .subscribeOn(Schedulers.io())
                .concatMap(currentUser -> {
                    if (!currentUser.isPresent()) {
                        long currentTime = Instant.now().toEpochMilli();
                        User data = new User();
                        data.email = firebaseUser.getEmail();
                        data.firebaseID = firebaseUser.getUid();
                        data.fullName = firebaseUser.getDisplayName();
                        Uri currentProfile = firebaseUser.getPhotoUrl();
                        if (currentProfile != null) {
                            data.profile = currentProfile.toString();
                        }
                        data.phoneNumber = firebaseUser.getPhoneNumber();
                        data.gender = -1;
                        data.isActive = true;
                        data.createAt = currentTime;
                        data.updatedAt = currentTime;
                        return this.userService.insertUsers(data);
                    } else {
                        return Observable.just(currentUser);
                    }
                }).concatMap(v -> {
                    if (!v.isPresent()) return Observable.just(v);
                    User user = v.get();
                   return userService.getToken().map(it -> {
                       user.firebaseToken = it;
                       return user;
                   }).concatMap(userService::updateUsers);
                }).doOnError(throwable -> {
                    event.onNext(new LoginEvent.LoginFailed(throwable.getMessage()));
                }).subscribe(user -> {
                    user.ifPresent(value -> event.onNext(new LoginEvent.LoginSuccess(value)));
                })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}