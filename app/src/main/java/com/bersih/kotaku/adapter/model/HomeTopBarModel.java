package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewMainTopBinding;
import com.bersih.kotaku.firebase.model.User;
import com.bersih.kotaku.utils.GlideApp;
import com.bersih.kotaku.utils.GlideFirebaseStorage;

import java.util.Locale;
import java.util.Optional;

public class HomeTopBarModel extends EpoxyModelWithHolder<HomeTopBarModel.Holder> {
    private User data;
    private final OnClickListener onClickListener;

    public HomeTopBarModel(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public HomeTopBarModel setData(User data) {
        this.data = data;
        return this;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(data, onClickListener);
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(data, onClickListener);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_main_top;
    }

    static class Holder extends EpoxyHolder {
        private ViewMainTopBinding viewBinding;
        private final User data;
        private final OnClickListener onClickListener;

        public Holder(User data, OnClickListener onClickListener){
            this.data = data;
            this.onClickListener = onClickListener;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewMainTopBinding.bind(itemView);
            bind(data, onClickListener);
        }

        void bind(User data, OnClickListener onClickListener) {
            if (viewBinding == null) return;
            String helloName = String.format(Locale.ENGLISH, "Hi, %s!", Optional.ofNullable(data.fullName).orElse("There"));
            viewBinding.mainNameTopBar.setText(helloName);
            GlideFirebaseStorage.smartLoad(viewBinding.getRoot(), data.profile, viewBinding.mainProfileTopBar);
            viewBinding.mainNotificationTopBar.setOnClickListener(v -> {
                onClickListener.onClick();
            });
        }
    }

    public interface OnClickListener {
        void onClick();
    }
}
