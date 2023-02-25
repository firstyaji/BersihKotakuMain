package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewHeader6Binding;

public class Header4Model extends EpoxyModelWithHolder<Header4Model.Holder> {
    private final String title;
    private final @DrawableRes int icon;
    private final OnClickListener onClickListener;

    public Header4Model(OnClickListener onClickListener, String title, @DrawableRes int icon) {
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
        return R.layout.view_header6;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(onClickListener, title, icon);
    }


    static class Holder extends EpoxyHolder {
        private ViewHeader6Binding viewBinding;
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
            viewBinding = ViewHeader6Binding.bind(itemView);
            bind(onClickListener, title, icon);
        }


        void bind(OnClickListener onClickListener, String title, int icon) {
            if (viewBinding == null) return;
            viewBinding.headerTitle6.setText(title);

            if (onClickListener != null) {
                viewBinding.headerNavigationButton6.setVisibility(View.VISIBLE);
                viewBinding.headerNavigationButton6.setImageResource(icon);
                viewBinding.headerNavigationButton6.setOnClickListener(v-> {
                    onClickListener.onClick();
                });
            } else {
                viewBinding.headerNavigationButton6.setVisibility(View.INVISIBLE);
            }
        }
    }

    public interface OnClickListener {
        void onClick();
    }
}
