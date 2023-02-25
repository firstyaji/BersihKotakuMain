package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewNoPlanBinding;
import com.bersih.kotaku.utils.GlideApp;

public class HomeNoPlan extends EpoxyModelWithHolder<HomeNoPlan.Holder> {
    private final @DrawableRes int icon;
    private final String title;

    public HomeNoPlan(@DrawableRes int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(icon, title);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_no_plan;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(icon, title);
    }

    static class Holder extends EpoxyHolder {
        private ViewNoPlanBinding viewBinding;
        private final @DrawableRes int icon;
        private final String title;

        Holder(int icon, String title) {
            this.icon = icon;
            this.title = title;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewNoPlanBinding.bind(itemView);
            bind(icon, title);
        }

        void bind(int icon, String title) {
            if (viewBinding == null) return;
            GlideApp.with(viewBinding.getRoot()).load(icon).into(viewBinding.noPlanImage);
            viewBinding.noPlanText.setText(title);
        }
    }
}
