package com.bersih.kotaku.adapter;

import com.airbnb.epoxy.AsyncEpoxyController;
import com.bersih.kotaku.adapter.model.NotificationModel;
import com.bersih.kotaku.adapter.model.SubsItemModel;
import com.bersih.kotaku.firebase.model.Notification;
import com.bersih.kotaku.firebase.model.Subscription;

import java.util.ArrayList;
import java.util.List;

public class SubsController extends AsyncEpoxyController {
    private SubsItemModel.OnClickListener onClickListener;
    private List<Subscription> subscriptionList = new ArrayList<>();

    public void setSubscriptionList(List<Subscription> notificationsList) {
        this.subscriptionList = notificationsList;
        requestModelBuild();
    }

    public void setOnClickListener(SubsItemModel.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void buildModels() {
        for (Subscription subscription: subscriptionList) {
            new SubsItemModel(onClickListener, subscription).id(subscription.hashCode()).addTo(this);
        }
    }
}
