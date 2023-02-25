package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewPaymentOptionBinding;
import com.bersih.kotaku.firebase.model.Pack;
import com.bersih.kotaku.firebase.model.Payment;
import com.bersih.kotaku.utils.GlideApp;

public class PaymentModel extends EpoxyModelWithHolder<PaymentModel.Holder> {
    private final Payment data;
    private final PaymentModel.OnClickListener onClickListener;

    public PaymentModel(Payment data, OnClickListener onClickListener) {
        this.data = data;
        this.onClickListener = onClickListener;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(this.onClickListener, data);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_payment_option;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(onClickListener, data);
    }

    static class Holder extends EpoxyHolder {
        private ViewPaymentOptionBinding viewBinding;
        private final OnClickListener onClickListener;
        private final Payment data;

        Holder(OnClickListener onClickListener, Payment payment) {
            this.onClickListener = onClickListener;
            this.data = payment;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewPaymentOptionBinding.bind(itemView);
            bind(onClickListener, data);
        }

        void bind(OnClickListener onClickListener, Payment data) {
            if (viewBinding == null) return;
            GlideApp.with(viewBinding.getRoot()).load(data.icon).into(viewBinding.paymentOptionImage);
            viewBinding.paymentOptionTitle.setText(String.format("%s - %s", data.bankAccountName, data.bankAccountNumber));
            viewBinding.paymentOption.setOnClickListener(view -> onClickListener.OnClick(data));
            viewBinding.paymentOption.setChecked(data.isSelected);
        }
    }

    public interface OnClickListener {
        void OnClick(Payment payment);
    }
}
