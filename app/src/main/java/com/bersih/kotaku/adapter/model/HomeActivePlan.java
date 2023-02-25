package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewActivePlanBinding;

public class HomeActivePlan extends EpoxyModelWithHolder<HomeActivePlan.Holder> {
    private final String title;

    public HomeActivePlan(String title) {
        this.title = title;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(title);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_active_plan;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(title);
    }

    static class Holder extends EpoxyHolder {
        private ViewActivePlanBinding viewBinding;
        private final String title;

        Holder(String title) {
            this.title = title;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewActivePlanBinding.bind(itemView);
            bind(title);
        }

        void bind(String title) {
            if (viewBinding == null) return;
            viewBinding.activePlanText.setText(title);
        }
    }
}
