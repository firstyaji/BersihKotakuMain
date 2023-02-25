package com.bersih.kotaku.adapter;

import android.content.Context;

import com.airbnb.epoxy.AsyncEpoxyController;
import com.bersih.kotaku.R;
import com.bersih.kotaku.adapter.model.ButtonModel;
import com.bersih.kotaku.adapter.model.ConfirmationPendingModel;
import com.bersih.kotaku.adapter.model.Header2Model;
import com.bersih.kotaku.adapter.model.HeaderModel;

public class ConfirmationController extends AsyncEpoxyController {
    private ButtonModel.OnClickListener onClickListener;
    private final Context context;

    public ConfirmationController(Context context) {
        this.context = context;
    }

    public void setOnClickListener(ButtonModel.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        requestModelBuild();
    }

    @Override
    protected void buildModels() {
        new Header2Model(null,  context.getString(R.string.payment), R.drawable.ic_arrow_back).id("Payment".hashCode()).addTo(this);
        new ConfirmationPendingModel(context.getString(R.string.order_process)).id("Your order still on process".hashCode()).addTo(this);
        new ButtonModel(onClickListener, context.getString(R.string.dashboard)).id("Dashboard".hashCode()).addTo(this);
    }
}
