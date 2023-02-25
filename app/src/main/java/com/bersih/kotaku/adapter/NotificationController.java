package com.bersih.kotaku.adapter;

import com.airbnb.epoxy.AsyncEpoxyController;
import com.bersih.kotaku.adapter.model.NotificationModel;
import com.bersih.kotaku.firebase.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationController extends AsyncEpoxyController {
    private NotificationModel.OnClickListener onClickListener;
    private List<Notification> notificationsList = new ArrayList<>();

    public void setNotificationsList(List<Notification> notificationsList) {
        this.notificationsList = notificationsList;
        requestModelBuild();
    }

    public void setOnClickListener(NotificationModel.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void buildModels() {
        for (Notification notification: notificationsList) {
            new NotificationModel(onClickListener, notification).id(notification.hashCode()).addTo(this);
        }
    }
}
