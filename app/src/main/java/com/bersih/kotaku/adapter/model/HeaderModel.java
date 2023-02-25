package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewHeaderBinding;

public class HeaderModel extends EpoxyModelWithHolder<HeaderModel.Holder> {
    private final String title;
    private final @DrawableRes int icon;
    private final OnClickListener onClickListener;

    public HeaderModel(OnClickListener onClickListener, String title, @DrawableRes int icon) {
        this.onClickListener = onClickListener;
        this.title = title;
        this.icon = icon;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(this.onClickListener, title, icon);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_header;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(onClickListener, title, icon);
    }

    static class Holder extends EpoxyHolder {
        private ViewHeaderBinding viewBinding;
        private final OnClickListener onClickListener;
        private final String title;
        private final @DrawableRes int icon;

        public Holder(OnClickListener onClickListener, String title, int icon){
            this.onClickListener = onClickListener;
            this.title = title;
            this.icon = icon;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewHeaderBinding.bind(itemView);
            bind(onClickListener, title, icon);
        }

        void bind(OnClickListener onClickListener, String title, int icon) {
            if (viewBinding == null) return;
            viewBinding.headerTitle.setText(title);
            if (onClickListener != null) {
                viewBinding.headerNavigationButton.setVisibility(View.VISIBLE);
                viewBinding.headerNavigationButton.setImageResource(icon);
                viewBinding.headerNavigationButton.setOnClickListener(v-> {
                    onClickListener.onClick();
                });
            } else {
                viewBinding.headerNavigationButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    public interface OnClickListener {
        void onClick();
    }
}
