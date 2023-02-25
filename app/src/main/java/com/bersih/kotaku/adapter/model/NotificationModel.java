package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewNotificationBinding;
import com.bersih.kotaku.firebase.model.Notification;
import com.bersih.kotaku.utils.GlideApp;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class NotificationModel extends EpoxyModelWithHolder<NotificationModel.Holder> {
    private final OnClickListener onClickListener;
    private final Notification notification;

    public NotificationModel(OnClickListener onClickListener, Notification notification) {
        this.onClickListener = onClickListener;
        this.notification = notification;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(onClickListener, notification);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_notification;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(onClickListener, notification);
    }

    public static class Holder extends EpoxyHolder {
        private ViewNotificationBinding viewBinding;
        private final OnClickListener onClickListener;
        private final Notification notification;
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
                .withZone(ZoneId.systemDefault());

        public Holder(OnClickListener onClickListener, Notification notification) {
            this.onClickListener = onClickListener;
            this.notification = notification;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            // MM/dd/yy
            viewBinding = ViewNotificationBinding.bind(itemView);
            bind(onClickListener, notification);
        }

        void bind(OnClickListener onClickListener, Notification notification) {
            if (viewBinding == null) return;
            if (notification.isRead) viewBinding.notificationBullet.setVisibility(View.INVISIBLE);
            else viewBinding.notificationBullet.setVisibility(View.VISIBLE);

            viewBinding.notificationTitle.setText(notification.title);
            Instant currentTime = Instant.ofEpochMilli(notification.createdAt);
            viewBinding.notificationSubTitle.setText(formatter.format(currentTime));

            if (notification.type.equals("OrderFailed") || notification.type.equals("OrderSuccessfully")) {
                GlideApp.with(viewBinding.getRoot()).load(R.drawable.notification_order).into(viewBinding.notificationImage);
            } else if (notification.type.equals("Pick")) {
                GlideApp.with(viewBinding.getRoot()).load(R.drawable.notification_pick).into(viewBinding.notificationImage);
            }

            viewBinding.getRoot().setOnClickListener(v -> {
                onClickListener.onClick(notification);
            });
        }
    }

    public interface OnClickListener {
        void onClick(Notification notification);
    }
}
