package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewTextBinding;

public class HomeTextActionModel extends EpoxyModelWithHolder<HomeTextActionModel.Holder> {
    private final OnClickListener onClickListener;
    private final String title;
    private final String action;

    public HomeTextActionModel(OnClickListener onClickListener, String title, String action) {
        this.onClickListener = onClickListener;
        this.title = title;
        this.action = action;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_text_action;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(this.onClickListener, title, action);
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(onClickListener, title, action);
    }

    static class Holder extends EpoxyHolder {
        private ViewTextBinding viewBinding;
        private final OnClickListener onClickListener;
        private final String text;
        private final String actions;

        public Holder(OnClickListener onClickListener, String text, String actions) {
            this.onClickListener = onClickListener;
            this.text = text;
            this.actions = actions;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewTextBinding.bind(itemView);
            bind(onClickListener, text, actions);
        }

        void bind(OnClickListener onClickListener, String text, String actions) {
            if (viewBinding == null) return;
            viewBinding.recommendTitle.setText(text);
            viewBinding.recommendAction.setText(actions);
            viewBinding.recommendAction.setOnClickListener(v -> {
                onClickListener.onClick();
            });
        }

    }

    public interface OnClickListener {
        void onClick();
    }
}
