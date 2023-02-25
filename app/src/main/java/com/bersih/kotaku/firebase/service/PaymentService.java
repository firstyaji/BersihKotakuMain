package com.bersih.kotaku.firebase.service;

import com.bersih.kotaku.firebase.model.Payment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class PaymentService {
    private final FirebaseFirestore firestore;

    @Inject  public PaymentService(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public Observable<List<Payment>> listenPayment() {
        return Observable.create(emitter -> {
            ListenerRegistration listener = this.firestore.collection("payments").addSnapshotListener((value, error) -> {
                        if (error != null) {
                            emitter.onError(error);
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            try {
                                List<Payment> subscriptionList = new ArrayList<>();
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    if (snapshot.exists()) {
                                        Payment subscription = Objects.requireNonNull(snapshot.toObject(Payment.class))
                                                .withId(snapshot.getId());
                                        subscriptionList.add(subscription);
                                    }
                                }
                                emitter.onNext(subscriptionList);
                            } catch (Exception e) {
                                emitter.onError(e);
                            }
                        }
                    });
            emitter.setCancellable(listener::remove);
        });
    }

    public Observable<Optional<Payment>> getPayment(String paymentID) {
        return Observable.create(emitter -> this.firestore.collection("payments").document(paymentID)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {
                            emitter.onNext(Optional.empty());
                            return;
                        }

                        Payment model = task.getResult().toObject(Payment.class);
                        if (model != null) {
                            model.withId(task.getResult().getId());
                            emitter.onNext(Optional.of(model));
                        } else {
                            emitter.onNext(Optional.empty());
                        }
                    } else {
                        emitter.onError(task.getException());
                    }
                }));
    }
}
