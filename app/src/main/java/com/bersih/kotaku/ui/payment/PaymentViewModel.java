package com.bersih.kotaku.ui.payment;

import android.graphics.Path;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.bersih.kotaku.firebase.model.Pack;
import com.bersih.kotaku.firebase.model.Payment;
import com.bersih.kotaku.firebase.model.User;
import com.bersih.kotaku.firebase.service.PackService;
import com.bersih.kotaku.firebase.service.PaymentService;
import com.bersih.kotaku.firebase.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.jakewharton.rxrelay3.BehaviorRelay;

import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import timber.log.Timber;

@HiltViewModel
public class PaymentViewModel extends ViewModel {
    private final PaymentService paymentService;
    private final UserService userService;
    private final PackService packService;
    private final BehaviorRelay<String> currentReceipt = BehaviorRelay.createDefault("");
    private final BehaviorRelay<Optional<Pack>> currentPack = BehaviorRelay.createDefault(Optional.empty());
    private final BehaviorRelay<Optional<Boolean>> navigation = BehaviorRelay.createDefault(Optional.empty());

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject public PaymentViewModel(PaymentService paymentService, UserService userService, PackService packService) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.packService = packService;
    }

    public LiveData<List<Payment>> listenPayment() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return null;
        }
        Observable<User> userObservable = this.userService.listenUser(firebaseUser.getEmail());
        Observable<List<Payment>> paymentObservable = this.paymentService.listenPayment();
        Observable<List<Payment>> paymentWithSelectedObservable = Observable.combineLatest(
                        paymentObservable, userObservable,
                        (paymentList, selected) -> paymentList
                                .stream().peek(e -> e.isSelected = selected.defaultBank.equals(e.id))
                                .collect(Collectors.toList()))
                .doOnNext(paymentList -> Timber.i("Payment list with %d", paymentList.size()))
                .observeOn(Schedulers.io());
        return LiveDataReactiveStreams.fromPublisher(paymentWithSelectedObservable.toFlowable(BackpressureStrategy.LATEST));
    }

    public LiveData<String> listenReceipt() {
        return LiveDataReactiveStreams.fromPublisher(currentReceipt.observeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST));
    }

    public LiveData<Boolean> listenNavigation() {
        return LiveDataReactiveStreams.fromPublisher(navigation.observeOn(Schedulers.io()).map(v -> v.orElse(false))
                .toFlowable(BackpressureStrategy.LATEST));
    }

    public void setCurrentReceipt(Uri fileURI) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        FirebaseStorage.getInstance()
                .getReference().child(String.format("users/%s/receipt/%s.jpeg", firebaseUser.getUid(), UUID.randomUUID().toString()))
                .putFile(fileURI).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(downloadTask -> {
                            if (downloadTask.isSuccessful()) {
                                currentReceipt.accept(downloadTask.getResult().toString());
                            }
                        });
                    } else {
                        currentReceipt.accept("");
                    }
                });
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

    public LiveData<Boolean> isFilled() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return null;
        }
        Observable<Optional<User>> userObservable = this.userService.listenUser(firebaseUser.getEmail())
                .map(Optional::of)
                .onErrorReturn(throwable -> Optional.empty());
        Observable<Boolean> isFilledObservable = Observable.combineLatest(userObservable, currentReceipt,
                (user, receipt) -> user.isPresent() && !user.get().AddressName.equals("") && !user.get().AddressDetails.equals("") && !receipt.isEmpty())
                .subscribeOn(Schedulers.io());

        return LiveDataReactiveStreams.fromPublisher(isFilledObservable.toFlowable(BackpressureStrategy.LATEST));
    }

    public LiveData<Optional<Pack>> listenPack() {
        return LiveDataReactiveStreams.fromPublisher(currentPack.observeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST));
    }

    public void setPack(String packID) {
        compositeDisposable.add(packService.getPack(packID).observeOn(Schedulers.io()).subscribe(currentPack));
    }

    public void setSelectedPayment(String paymentID) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        compositeDisposable.add(
                this.userService.getUsers(firebaseUser.getEmail()).concatMap(user -> {
                    if (user.isPresent()) {
                        User data = user.get();
                        data.defaultBank = paymentID;
                        return this.userService.updateUsers(data);
                    } else {
                        return Observable.error(new Throwable("can't find use while update profile data"));
                    }
                }).observeOn(Schedulers.io()).onErrorComplete().subscribe()
        );
    }

    public void orderSubs() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        Optional.of(true).orElse(false);
        Observable<Optional<Payment>> getPayment = this.userService.listenUser(firebaseUser.getEmail()).concatMap(v -> {
            if (!Objects.equals(v.defaultBank, "")) {
                return paymentService.getPayment(v.defaultBank);
            } else {
                return Observable.just(Optional.empty());
            }
        });
        compositeDisposable.add(Observable.zip(userService.getUsers(firebaseUser.getEmail()), getPayment, currentReceipt, currentPack, (user, payment, receipt, pack) -> {
            if (user.isPresent() && payment.isPresent() && !Objects.equals(receipt, "") && pack.isPresent()) {
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.user = user.get();
                orderDTO.payment = payment.get();
                orderDTO.receipt = receipt;
                orderDTO.pack = pack.get();
                return orderDTO;
            } else {
                throw new Throwable("data its not complete please try again");
            }
        }).concatMap(orderDTO -> packService.orderSubs(orderDTO.user, URI.create(orderDTO.receipt), orderDTO.payment, orderDTO.pack))
                .doOnError(Timber::e).subscribe(it -> navigation.accept(Optional.of(true))));
    }

    void consumeNavigate() {
        navigation.accept(Optional.of(false));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    static class OrderDTO {
        User user;
        Payment payment;
        String receipt;
        Pack pack;
    }
}