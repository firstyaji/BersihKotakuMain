package com.bersih.kotaku.firebase.service;

import com.bersih.kotaku.firebase.model.Notification;
import com.bersih.kotaku.firebase.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class UserService {
    private final FirebaseFirestore firestore;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseMessaging firebaseMessaging;

    @Inject public UserService(FirebaseFirestore firestore, FirebaseAuth firebaseAuth, FirebaseMessaging firebaseMessaging) {
        this.firestore = firestore;
        this.firebaseAuth = firebaseAuth;
        this.firebaseMessaging = firebaseMessaging;
    }

    public FirebaseUser loadUser() {
        return firebaseAuth.getCurrentUser();
    }

    public Observable<String> getToken() {
        return Observable.create(emitter -> this.firebaseMessaging.getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) emitter.onNext(task.getResult());
            else emitter.onNext("");
        }));
    }

    public Observable<User> listenUser(String email) {
        return Observable.create(emitter -> {
            ListenerRegistration listener = this.firestore.collection("users")
                    .whereEqualTo("email", email).limit(1)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            emitter.onError(error);
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            try {
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    if (snapshot.exists()) {
                                        User user = Objects.requireNonNull(snapshot.toObject(User.class))
                                                .withId(snapshot.getId());
                                        emitter.onNext(user);
                                    }
                                }
                            } catch (Exception e) {
                                emitter.onError(e);
                            }
                        }
                    });
            emitter.setCancellable(listener::remove);
        });
    }

    public Observable<List<Notification>> listenNotificationn(String userID) {
        return Observable.create(emitter -> {
            ListenerRegistration listener = this.firestore.collection("users")
                    .document(userID).collection("notification").limit(50).addSnapshotListener((value, error) -> {
                        if (error != null) {
                            emitter.onError(error);
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            try {
                                List<Notification> subscriptionList = new ArrayList<>();
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    if (snapshot.exists()) {
                                        Notification subscription = Objects.requireNonNull(snapshot.toObject(Notification.class))
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

    public Observable<Optional<Notification>> getNotification(String id, String userID) {
        return Observable.create(emitter -> this.firestore.collection("users").document(userID)
                .collection("notification").document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().exists()) {
                    emitter.onNext(Optional.empty());
                    return;
                }

                Notification model = task.getResult().toObject(Notification.class);
                Objects.requireNonNull(model);
                model.withId(task.getResult().getId());
                emitter.onNext(Optional.of(model));
            } else {
                emitter.onError(task.getException());
            }
        }));
    }

    public Observable<Optional<Notification>> insertNotification(Notification notification) {
        return Observable.<Notification>create(emitter -> this.firestore.collection("users").document(notification.userID)
                .collection("notification").add(notification).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                notification.withId(task.getResult().getId());
                emitter.onNext(notification);
            } else {
                emitter.onError(task.getException());
            }
        })).concatMap(v -> getNotification(v.id, v.userID));
    }

    public Observable<Optional<Notification>> updatedNotification(Notification data) {
        return Observable.create(emitter -> this.firestore.collection("users").document(data.userID)
                .collection("notification").document(data.id).set(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                emitter.onNext(Optional.of(data));
            } else {
                emitter.onError(task.getException());
            }
        }));
    }

    public Observable<Optional<User>> getUsers(String email) {
        return Observable.create(emitter -> this.firestore.collection("users").whereEqualTo("email", email)
                .limit(1).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            emitter.onNext(Optional.empty());
                            return;
                        }
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            User model = documentSnapshot.toObject(User.class).withId(documentSnapshot.getId());
                            emitter.onNext(Optional.of(model));
                        }
                    } else {
                        emitter.onError(task.getException());
                    }
                }));
    }

    public Observable<Optional<User>> insertUsers(User data) {
        return Observable.<String>create(emitter -> this.firestore.collection("users").document(data.firebaseID).set(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                emitter.onNext(data.email);
            } else {
                emitter.onError(task.getException());
            }
        })).concatMap(this::getUsers);
    }

    public Observable<Optional<User>> updateUsers(User data) {
        return Observable.<String>create(emitter -> this.firestore.collection("users").document(data.firebaseID).set(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                emitter.onNext(data.email);
            } else {
                emitter.onError(task.getException());
            }
        })).concatMap(this::getUsers);
    }
}
