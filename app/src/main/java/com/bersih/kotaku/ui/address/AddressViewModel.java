package com.bersih.kotaku.ui.address;

import androidx.core.util.Supplier;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.bersih.kotaku.firebase.model.User;
import com.bersih.kotaku.firebase.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class AddressViewModel extends ViewModel {
    private final UserService userService;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject public AddressViewModel(UserService userService) {
        this.userService = userService;
    }

    public LiveData<@Nullable User> getUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return null;
        }
        Observable<User> userObservable = this.userService.listenUser(firebaseUser.getEmail())
                .subscribeOn(Schedulers.io());
        return LiveDataReactiveStreams.fromPublisher(userObservable.toFlowable(BackpressureStrategy.LATEST));
    }

    void updateAddress(String name, String details) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) return;

        disposable.add(userService.getUsers(firebaseUser.getEmail()).concatMap(userData -> {
            if (userData.isPresent()) {
                User user = userData.get();
                user.AddressName = name;
                user.AddressDetails = details;
                return userService.updateUsers(user);
            } else {
                return Observable.just(Optional.empty());
            }
        }).onErrorComplete().subscribe());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}