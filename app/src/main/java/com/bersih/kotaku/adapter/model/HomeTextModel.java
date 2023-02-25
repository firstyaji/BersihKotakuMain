package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewTextBinding;

public class HomeTextModel extends EpoxyModelWithHolder<HomeTextModel.Holder> {
    private final String data;

    public HomeTextModel(String data) {
        this.data = data;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(data);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_text;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(data);
    }

    static class Holder extends EpoxyHolder {
        private ViewTextBinding viewBinding;
        private final String text;

        public Holder(String text) {
            this.text = text;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewTextBinding.bind(itemView);
            bind(text);
        }

        void bind(String text) {
            if (viewBinding == null) return;
            viewBinding.recommendTitle.setText(text);
        }
    }
}
