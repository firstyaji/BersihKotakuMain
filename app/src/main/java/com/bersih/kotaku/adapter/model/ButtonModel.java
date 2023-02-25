package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewButtonBinding;

import timber.log.Timber;

public class ButtonModel extends EpoxyModelWithHolder<ButtonModel.Holder> {
    private final OnClickListener onClickListener;
    private final String title;

    public ButtonModel(OnClickListener onClickListener, String title) {
        this.onClickListener = onClickListener;
        this.title = title;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(title, onClickListener);
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(this.title, this.onClickListener);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_button;
    }

    static class Holder extends EpoxyHolder {
        private ViewButtonBinding viewBinding;
        private final String title;
        private final OnClickListener onClickListener;

        public Holder(String title, OnClickListener onClickListener) {
            this.title = title;
            this.onClickListener = onClickListener;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewButtonBinding.bind(itemView);
            bind(title, onClickListener);
        }

        void bind(String text, OnClickListener onClickListener) {
            viewBinding.viewButtonMain.setText(text);
            viewBinding.viewButtonMain.setOnClickListener(v -> onClickListener.onClick());
        }
    }

    public interface OnClickListener {
        void onClick();
    }
}
