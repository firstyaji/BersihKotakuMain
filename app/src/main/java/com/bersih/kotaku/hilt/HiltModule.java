package com.bersih.kotaku.hilt;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public final class HiltModule {

    @Provides
    public FirebaseFirestore provideFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    public FirebaseAuth provideAuth() { return FirebaseAuth.getInstance(); }

    @Provides
    public FirebaseMessaging provideMessage() { return FirebaseMessaging.getInstance(); }
}
