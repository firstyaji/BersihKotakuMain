package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewAddAddressBinding;

public class AddAddressModel extends EpoxyModelWithHolder<AddAddressModel.Holder> {
    private final String title;
    private final @DrawableRes int icon;
    private final OnClickListener onClickListener;

    public AddAddressModel(OnClickListener onClickListener, String title, @DrawableRes int icon) {
        this.onClickListener = onClickListener;
        this.title = title;
        this.icon = icon;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(this.onClickListener, title, icon);
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(title, icon);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_add_address;
    }

    static class Holder extends EpoxyHolder {
        private ViewAddAddressBinding viewBinding;
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
            viewBinding = ViewAddAddressBinding.bind(itemView);
            bind(title, icon);
        }

        void bind(String title, int icon) {
            if (viewBinding == null) return;

            viewBinding.addAddressTitle.setText(title);
            viewBinding.addAddressButton.setOnClickListener(v-> {
                this.onClickListener.onClick();
            });
        }
    }

    public interface OnClickListener {
        void onClick();
    }
}
