package com.bersih.kotaku.firebase.service;

import com.bersih.kotaku.firebase.model.Pack;
import com.bersih.kotaku.firebase.model.Payment;
import com.bersih.kotaku.firebase.model.Picker;
import com.bersih.kotaku.firebase.model.Recommend;
import com.bersih.kotaku.firebase.model.Subscription;
import com.bersih.kotaku.firebase.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class PackService {
    private final FirebaseFirestore firestore;

    @Inject public PackService(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public Observable<List<Picker>> listenPickerr(String userID) {
        return Observable.create(emitter -> {
            ListenerRegistration listener = this.firestore.collection("picker")
                    .whereEqualTo("userID", userID)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .limit(100).addSnapshotListener(((value, error) -> {
                        if (error != null) {
                            emitter.onError(error);
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            try {
                                List<Picker> pickerList = new ArrayList<>();
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    if (snapshot.exists()) {
                                        Picker pick = Objects.requireNonNull(snapshot.toObject(Picker.class))
                                                .withId(snapshot.getId());
                                       pickerList.add(pick);
                                    }
                                }
                                emitter.onNext(pickerList);
                            } catch (Exception e) {
                                emitter.onError(e);
                            }
                        }
            }));
            emitter.setCancellable(listener::remove);
        });
    }

    public Observable<Optional<Pack>> listenPack(String id) {
        return Observable.create(emitter -> {
            ListenerRegistration listener = this.firestore.collection("pack")
                    .document(id).addSnapshotListener(((value, error) -> {
                if (error != null) {
                    emitter.onError(error);
                    return;
                }

                if (value != null && value.exists()) {
                    try {
                        Pack pack = value.toObject(Pack.class);
                        if (pack != null) {
                            pack.withId(value.getId());
                            emitter.onNext(Optional.of(pack));
                        } else {
                            emitter.onNext(Optional.empty());
                        }
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                }
            }));
            emitter.setCancellable(listener::remove);
        });
    }

    public Observable<Optional<Pack>> getPack(String packID) {
        return Observable.create(emitter -> this.firestore.collection("pack").document(packID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    Pack model = task.getResult().toObject(Pack.class);
                    if (model != null) model.withId(task.getResult().getId());
                    emitter.onNext(Optional.ofNullable(model));
                } else {
                    emitter.onNext(Optional.empty());
                }
            } else {
                emitter.onError(task.getException());
            }
        }));
    }

    public Observable<List<Pack>> listenPack() {
        return Observable.create(emitter -> {
            ListenerRegistration listener = this.firestore.collection("pack").whereEqualTo("isActive", true).addSnapshotListener(((value, error) -> {
                if (error != null) {
                    emitter.onError(error);
                    return;
                }

                if (value != null && !value.isEmpty()) {
                    try {
                        ArrayList<Pack> items = new ArrayList<>();
                        for (DocumentSnapshot snapshot: value.getDocuments()) {
                            if (snapshot.exists()) {
                                Pack pack = Objects.requireNonNull(snapshot.toObject(Pack.class)).withId(snapshot.getId());
                                items.add(pack);
                            }
                        }
                        emitter.onNext(items);
                    }catch (Exception e) {
                        emitter.onError(e);
                    }
                }
            }));
            emitter.setCancellable(listener::remove);
        });
    }

    public Observable<List<Recommend>> listenRecommend() {
        return Observable.create(emitter -> {
           ListenerRegistration listener = this.firestore.collection("recommend").whereEqualTo("isActive", true).addSnapshotListener(((value, error) -> {
               if (error != null) {
                   emitter.onError(error);
                   return;
               }

               if (value != null && !value.isEmpty()) {
                   try {
                       ArrayList<Recommend> items = new ArrayList<>();
                       for (DocumentSnapshot snapshot: value.getDocuments()) {
                           if (snapshot.exists()) {
                               Recommend recommend = Objects.requireNonNull(snapshot.toObject(Recommend.class)).withId(snapshot.getId());
                               items.add(recommend);
                           }
                       }
                       emitter.onNext(items);
                   }catch (Exception e) {
                       emitter.onError(e);
                   }
               }
           }));
           emitter.setCancellable(listener::remove);
        });
    }

    public Observable<Optional<Subscription>> listenCurrentActiveSubs(String userID) {
        long currentTime = Instant.now().toEpochMilli();
        return Observable.create(emitter -> {
            ListenerRegistration listener = this.firestore.collection("subscriptions")
                    .whereEqualTo("userID", userID)
                    .whereGreaterThanOrEqualTo("validUntil", currentTime)
                    .whereEqualTo("status", "Accepted").limit(1)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            emitter.onError(error);
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            try {
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    if (snapshot.exists()) {
                                        Subscription subscription = Objects.requireNonNull(snapshot.toObject(Subscription.class))
                                                .withId(snapshot.getId());
                                        emitter.onNext(Optional.of(subscription));
                                    }
                                }
                            } catch (Exception e) {
                                emitter.onError(e);
                            }
                        } else {
                            emitter.onNext(Optional.empty());
                        }
                    });
            emitter.setCancellable(listener::remove);
        });
    }

    public Observable<List<Subscription>> listenSubs(String userID) {
        return Observable.create(emitter -> {
            ListenerRegistration listener = this.firestore.collection("subscriptions")
                    .whereEqualTo("userID", userID).limit(50).addSnapshotListener((value, error) -> {
                        if (error != null) {
                            emitter.onError(error);
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            try {
                                List<Subscription> subscriptionList = new ArrayList<>();
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    if (snapshot.exists()) {
                                        Subscription subscription = Objects.requireNonNull(snapshot.toObject(Subscription.class))
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

    public Observable<List<Subscription>> listenActiveSubsss(String userID) {
        return Observable.create(emitter -> {
            ListenerRegistration listener = this.firestore.collection("subscriptions")
                    .whereEqualTo("userID", userID)
                    .whereEqualTo("status", "Accepted")
                    .orderBy("registerAt", Query.Direction.DESCENDING).limit(50).addSnapshotListener((value, error) -> {
                        if (error != null) {
                            emitter.onError(error);
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            try {
                                List<Subscription> subscriptionList = new ArrayList<>();
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    if (snapshot.exists()) {
                                        Subscription subscription = Objects.requireNonNull(snapshot.toObject(Subscription.class))
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

    public Observable<Optional<Subscription>> getSubs(String subsID) {
        return Observable.create(emitter -> this.firestore.collection("subscriptions").document(subsID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    Subscription model = Objects.requireNonNull(task.getResult().toObject(Subscription.class))
                            .withId(task.getResult().getId());
                    emitter.onNext(Optional.of(model));
                } else {
                    emitter.onNext(Optional.empty());
                }
            } else {
                emitter.onError(task.getException());
            }
        }));
    }

    public Observable<Optional<Subscription>> insertSubs(Subscription subscription) {
        return Observable.<String>create(emitter -> this.firestore.collection("subscriptions").add(subscription).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                emitter.onNext(task.getResult().getId());
            } else {
                emitter.onError(task.getException());
            }
        })).concatMap(this::getSubs);
    }

    public Observable<Optional<Subscription>> orderSubs(User user, URI receipt, Payment payment, Pack pack) {
        Instant now = Instant.now();
        ZonedDateTime zdt = ZonedDateTime.ofInstant( now , ZoneId.systemDefault() );
        long currentTime = now.toEpochMilli();
        long validUntil = zdt.plusMonths(1).toInstant().toEpochMilli();
        Subscription subscription = new Subscription();
        subscription.userID = user.firebaseID;
        subscription.packID = pack.id;
        subscription.packName = pack.title;
        subscription.fullName = user.fullName;
        subscription.addressName = user.AddressName;
        subscription.addressDetails = user.AddressDetails;
        subscription.price = pack.price;
        subscription.paymentReceipt = receipt.toString();
        subscription.receiverBank = payment.bankName;
        subscription.receiverAccountName = payment.bankAccountName;
        subscription.receiverAccountNumber = payment.bankAccountNumber;
        subscription.status = "Pending";
        subscription.registerAt = currentTime;
        subscription.validUntil = validUntil;
        return insertSubs(subscription);
    }

}
