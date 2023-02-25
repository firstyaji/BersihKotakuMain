package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;

public class HistoryPesananNoData extends EpoxyModelWithHolder<HistoryPesananNoData.Holder> {
    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_historypesanan_no_data;
    }

    public static class Holder extends EpoxyHolder {

        @Override
        protected void bindView(@NonNull View itemView) {

        }
    }
}
