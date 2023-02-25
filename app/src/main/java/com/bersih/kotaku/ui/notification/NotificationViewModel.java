package com.bersih.kotaku.ui.notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.bersih.kotaku.firebase.model.Notification;
import com.bersih.kotaku.firebase.model.Payment;
import com.bersih.kotaku.firebase.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class NotificationViewModel extends ViewModel {
    private final UserService userService;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject public NotificationViewModel(UserService userService) {
        this.userService = userService;
    }

    public LiveData<List<Notification>> listenNotification() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return null;
        }
        Observable<List<Notification>> paymentObservable = this.userService.listenNotificationn(firebaseUser.getUid())
                .observeOn(Schedulers.io());
        return LiveDataReactiveStreams.fromPublisher(paymentObservable.toFlowable(BackpressureStrategy.LATEST));
    }

    public void read(Notification notification) {
        notification.isRead = true;
        disposable.add(this.userService.updatedNotification(notification).observeOn(Schedulers.io()).subscribe());
    }
}