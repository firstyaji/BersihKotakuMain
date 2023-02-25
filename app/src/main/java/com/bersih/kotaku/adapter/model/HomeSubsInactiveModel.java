package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewCardPackBinding;
import com.bersih.kotaku.utils.GlideApp;

public class HomeSubsInactiveModel extends EpoxyModelWithHolder<HomeSubsInactiveModel.Holder> {
    private final OnClickHomeSubsInactive onClickListener;

    public HomeSubsInactiveModel(OnClickHomeSubsInactive onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(this.onClickListener);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_card_pack;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(onClickListener);
    }

    static class Holder extends EpoxyHolder {
        private ViewCardPackBinding viewBinding;
        private final OnClickHomeSubsInactive onClickListener;

        public Holder(OnClickHomeSubsInactive onClickListener) {
            this.onClickListener = onClickListener;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewCardPackBinding.bind(itemView);
            bind(onClickListener);
        }

        void bind(OnClickHomeSubsInactive onClickListener) {
            if (viewBinding == null) return;
            GlideApp.with(viewBinding.getRoot()).load(R.drawable.no_plan).into(viewBinding.packCoverImage);
            viewBinding.packBuyButton.setOnClickListener(view -> onClickListener.onBuy());
        }
    }

    public interface OnClickHomeSubsInactive {
        void onBuy();
    }
}
