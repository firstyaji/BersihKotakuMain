package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewOrderDetailsBinding;

import java.text.DecimalFormat;

public class OrderDetailsModel extends EpoxyModelWithHolder<OrderDetailsModel.Holder> {
    private final String packName;
    private final double price;

    public OrderDetailsModel(String packName, double price) {
        this.packName = packName;
        this.price = price;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(packName, price);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_order_details;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(packName, price);
    }

    static class Holder extends EpoxyHolder {
        private ViewOrderDetailsBinding viewBinding;
        private final String packName;
        private final double price;

        Holder(String packName, double price) {
            this.packName = packName;
            this.price = price;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewOrderDetailsBinding.bind(itemView);
            bind(packName, price);
        }

        void bind(String packName, double price) {
            if (viewBinding == null) return;
            viewBinding.orderDetailsPackValue.setText(packName);
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            viewBinding.orderDetailsPriceValue.setText(String.format("Rp%s", decimalFormat.format(price)));
        }
    }
}
