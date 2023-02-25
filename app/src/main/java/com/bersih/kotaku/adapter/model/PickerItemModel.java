package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewPickerItemBinding;
import com.bersih.kotaku.firebase.model.Picker;
import com.bersih.kotaku.utils.GlideApp;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PickerItemModel extends EpoxyModelWithHolder<PickerItemModel.Holder> {
    private final OnClickListener onClickListener;
    private final Picker picker;

    public PickerItemModel(OnClickListener onClickListener, Picker picker) {
        this.onClickListener = onClickListener;
        this.picker = picker;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(onClickListener, picker);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_picker_item;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(onClickListener, picker);
    }

    public static class Holder extends EpoxyHolder {
        private ViewPickerItemBinding viewBinding;
        private final OnClickListener onClickListener;
        private final Picker picker;
        private final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd MMM yyyy")
                .withZone(ZoneId.systemDefault());
        private final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("hh:mm a")
                .withZone(ZoneId.systemDefault());

        public Holder(OnClickListener onClickListener, Picker picker) {
            this.onClickListener = onClickListener;
            this.picker = picker;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewPickerItemBinding.bind(itemView);
            bind(onClickListener, picker);
        }

        void bind(OnClickListener onClickListener, Picker picker) {
            if (viewBinding == null) return;
            viewBinding.pickerTitle.setText(picker.title);
            GlideApp.with(viewBinding.getRoot()).load(R.drawable.notification_pick).into(viewBinding.pickerImage);
            viewBinding.pickerTimeLabel.setText(R.string.time);
            Instant time = Instant.ofEpochMilli(picker.createdAt);
            viewBinding.pickerTime.setText(formatterTime.format(time));
            viewBinding.pickerDate.setText(formatterDate.format(time));
            viewBinding.pickerDelete.setOnClickListener(v -> {
                onClickListener.onClick(picker);
            });
        }
    }

    public interface OnClickListener {
        void onClick(Picker picker);
    }
}
