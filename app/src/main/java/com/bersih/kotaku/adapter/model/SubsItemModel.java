package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewSubsItemBinding;
import com.bersih.kotaku.firebase.model.Subscription;
import com.bersih.kotaku.utils.GlideApp;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SubsItemModel extends EpoxyModelWithHolder<SubsItemModel.Holder> {
    private final OnClickListener onClickListener;
    private final Subscription subscription;

    public SubsItemModel(OnClickListener onClickListener, Subscription subscription) {
        this.onClickListener = onClickListener;
        this.subscription = subscription;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(onClickListener, subscription);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_subs_item;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(onClickListener, subscription);
    }

    public static class Holder extends EpoxyHolder {
        private ViewSubsItemBinding viewBinding;
        private final OnClickListener onClickListener;
        private final Subscription subscription;
        private final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd MMM yyyy")
                .withZone(ZoneId.systemDefault());

        public Holder(OnClickListener onClickListener, Subscription subscription) {
            this.onClickListener = onClickListener;
            this.subscription = subscription;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewSubsItemBinding.bind(itemView);
            bind(onClickListener, subscription);
        }

        void bind(OnClickListener onClickListener, Subscription subscription) {
            if (viewBinding == null) return;
            GlideApp.with(viewBinding.getRoot()).load(R.drawable.notification_order).into(viewBinding.subsImage);
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            viewBinding.subsPrice.setText(String.format("Rp%s", decimalFormat.format(subscription.price)));
            viewBinding.subsPriceLabel.setText(R.string.price);
            Instant instant = Instant.ofEpochMilli(subscription.registerAt);
            viewBinding.subsTime.setText(formatterDate.format(instant));
            viewBinding.subsTitle.setText(subscription.packName);
            viewBinding.subsDelete.setOnClickListener(v -> {
                onClickListener.onClick(subscription);
            });
        }
    }

    public interface OnClickListener {
        void onClick(Subscription subscription);
    }
}
