package com.bersih.kotaku.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.bersih.kotaku.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

@GlideModule
public class GlideFirebaseStorage extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.append(StorageReference.class, InputStream.class,
                new FirebaseImageLoader.Factory());
    }

    public static void smartLoad(View view, String path, ImageView target) {
        smartLoad(view, path, target, R.drawable.ic_profile_fragment);

    }

    public static void smartLoad(View view, String path, ImageView target, @DrawableRes int id) {
        try {

            StorageReference profile = FirebaseStorage.getInstance().getReferenceFromUrl(path);
            GlideApp.with(view).load(profile).error(id).into(target);
        }catch (Throwable throwable) {
            GlideApp.with(view).load(path).error(id).into(target);
        }
    }
}
