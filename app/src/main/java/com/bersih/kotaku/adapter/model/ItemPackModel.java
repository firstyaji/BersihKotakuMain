package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewPackBinding;
import com.bersih.kotaku.firebase.model.Pack;
//import com.bersih.kotaku.utils.GlideApp;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

public class ItemPackModel extends EpoxyModelWithHolder<ItemPackModel.Holder> {
    private final Pack data;
    private final OnClickListener onClickListener;
    private final boolean available;

    public ItemPackModel(OnClickListener onClickListener, Pack data, boolean available) {
        this.onClickListener = onClickListener;
        this.data = data;
        this.available = available;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(this.onClickListener, data, available);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_pack;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(onClickListener, data, available);
    }

    static class Holder extends EpoxyHolder {
        private ViewPackBinding viewBinding;
        private final OnClickListener onClickListener;
        private final Pack pack;
        private final boolean available;

        public Holder(OnClickListener onClickListener, Pack pack, boolean available) {
            this.onClickListener = onClickListener;
            this.pack = pack;
            this.available = available;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewPackBinding.bind(itemView);
            bind(onClickListener, pack, available);
        }

        void bind(OnClickListener onClickListener, Pack pack, boolean available) {
            if (viewBinding == null) return;
            viewBinding.packItemBigTitle.setText(pack.title);
            viewBinding.packItemTitle.setText(pack.description);
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            viewBinding.packItemPrice.setText(String.format("Rp%s", decimalFormat.format(pack.price)));
            Glide.with(viewBinding.getRoot()).load(pack.image).error(R.drawable.ic_launcher_foreground).into(viewBinding.packItemCover);
            if (available) {
                viewBinding.packItemBuy.setOnClickListener(v -> {
                    onClickListener.onClick(pack);
                });
                viewBinding.packItemBuy.setEnabled(true);
            } else {
                viewBinding.packItemBuy.setEnabled(false);
            }
        }
    }

    public interface OnClickListener {
        void onClick(Pack pack);
    }
}
