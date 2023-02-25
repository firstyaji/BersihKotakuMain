package com.bersih.kotaku.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.bersih.kotaku.firebase.model.Pack;
import com.bersih.kotaku.firebase.model.Recommend;
import com.bersih.kotaku.firebase.model.Subscription;
import com.bersih.kotaku.firebase.model.User;
import com.bersih.kotaku.firebase.service.PackService;
import com.bersih.kotaku.firebase.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private final UserService userService;
    private final PackService packService;

    @Inject public MainViewModel(UserService userService, PackService packService){
        this.userService = userService;
        this.packService = packService;
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

    public LiveData<List<Recommend>> getRecommendPack() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return null;
        }
        Observable<List<Recommend>> recommends = this.packService.listenRecommend()
                .subscribeOn(Schedulers.io());
        return LiveDataReactiveStreams.fromPublisher(recommends.toFlowable(BackpressureStrategy.LATEST));
    }
}