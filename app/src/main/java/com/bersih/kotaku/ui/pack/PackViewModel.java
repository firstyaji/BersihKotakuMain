package com.bersih.kotaku.ui.pack;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.bersih.kotaku.firebase.model.Pack;
import com.bersih.kotaku.firebase.model.User;
import com.bersih.kotaku.firebase.service.PackService;
import com.bersih.kotaku.firebase.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class PackViewModel extends ViewModel {
    private final PackService packService;
    private final UserService userService;

    @Inject public PackViewModel(PackService packService, UserService userService) {
        this.packService = packService;
        this.userService = userService;
    }

    public LiveData<Optional<Pack>> getCurrentActiveSubs() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return null;
        }
        Observable<Optional<Pack>> userSubs = this.userService.listenUser(firebaseUser.getEmail())
                .concatMap(user -> this.packService.listenCurrentActiveSubs(user.id))
                .concatMap(subscription -> this.packService.listenPack(subscription.orElseThrow(() -> new Throwable("there is not subscription")).packID))
                .onErrorResumeNext(throwable -> Observable.just(Optional.empty()))
                .subscribeOn(Schedulers.io());
        return LiveDataReactiveStreams.fromPublisher(userSubs.toFlowable(BackpressureStrategy.LATEST));
    }

    public LiveData<List<Pack>> getPack() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return null;
        }
        Observable<List<Pack>> recommends = this.packService.listenPack()
                .map(packs -> packs.stream().sorted(Comparator.comparingDouble(pack -> pack.price)).collect(Collectors.toList()))
                .subscribeOn(Schedulers.io());
        return LiveDataReactiveStreams.fromPublisher(recommends.toFlowable(BackpressureStrategy.LATEST));
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
}