package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewAddressAddedBinding;

import java.util.Objects;

public class AddressAddedModel extends EpoxyModelWithHolder<AddressAddedModel.Holder> {
    private final String content;
    private final String googleMaps;

    public AddressAddedModel(String content, String googleMaps) {
        this.content = content;
        this.googleMaps = googleMaps;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_address_added;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(content, googleMaps);
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(content, googleMaps);
    }

    static class Holder extends EpoxyHolder {
        private ViewAddressAddedBinding viewBinding;
        private final String address;
        private final String googleMaps;

        Holder(String address, String googleMaps) {
            this.address = address;
            this.googleMaps = googleMaps;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewAddressAddedBinding.bind(itemView);
            bind(address, googleMaps);
        }

        void bind(String address, String googleMaps) {
            if (viewBinding == null) return;

            Objects.requireNonNull(viewBinding.addressAddedContent.getEditText())
                    .setText(address);
            viewBinding.addressAddedContent.setEnabled(false);
            Objects.requireNonNull(viewBinding.addressAddedGoogleMapsContent.getEditText())
                    .setText(googleMaps);
            viewBinding.addressAddedGoogleMapsContent.setEnabled(false);
        }
    }
}
