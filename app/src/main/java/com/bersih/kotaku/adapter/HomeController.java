package com.bersih.kotaku.adapter;

import android.content.Context;

import com.airbnb.epoxy.AsyncEpoxyController;
import com.airbnb.epoxy.EpoxyModel;
import com.bersih.kotaku.R;
import com.bersih.kotaku.adapter.model.HomeActivePlan;
import com.bersih.kotaku.adapter.model.HomeNoPlan;
import com.bersih.kotaku.adapter.model.HomeRecommendModel;
import com.bersih.kotaku.adapter.model.HomeRecommendPacketGroup;
import com.bersih.kotaku.adapter.model.HomeSubsInactiveModel;
import com.bersih.kotaku.adapter.model.HomeTextActionModel;
import com.bersih.kotaku.adapter.model.HomeTextModel;
import com.bersih.kotaku.adapter.model.HomeTopBarModel;
import com.bersih.kotaku.firebase.model.Pack;
import com.bersih.kotaku.firebase.model.Recommend;
import com.bersih.kotaku.firebase.model.User;

import java.util.ArrayList;
import java.util.List;

public class HomeController extends AsyncEpoxyController {
    private User user;
    private Pack subscriptionPack;
    private List<Recommend> recommends = new ArrayList<>();

    private HomeSubsInactiveModel.OnClickHomeSubsInactive onClickHomeSubsInactive;
    private HomeTopBarModel.OnClickListener notificationClickListener;
    private final Context context;
    private HomeRecommendModel.OnClickRecommendItem onClickRecommendItem;
    private HomeTextActionModel.OnClickListener onClickListenerAction;

    public HomeController(Context context) {
        this.context = context;
    }

    public void setSubscription(Pack subscriptionPack) {
        this.subscriptionPack = subscriptionPack;
        requestModelBuild();
    }

    public void setOnClickHomeSubsInactive(HomeSubsInactiveModel.OnClickHomeSubsInactive onClickHomeSubsInactive) {
        this.onClickHomeSubsInactive = onClickHomeSubsInactive;
    }

    public void setOnClickRecommendItem(HomeRecommendModel.OnClickRecommendItem onClickRecommendItem) {
        this.onClickRecommendItem = onClickRecommendItem;
    }

    public void setOnClickListenerAction(HomeTextActionModel.OnClickListener onClickListenerAction) {
        this.onClickListenerAction = onClickListenerAction;
    }


    public void setUser(User user) {
        this.user = user;
        requestModelBuild();
    }

    public void setRecommends(List<Recommend> recommends) {
        this.recommends = recommends;
        requestModelBuild();
    }

    @Override
    protected void buildModels() {
        if (user != null) {
            new HomeTopBarModel(notificationClickListener).setData(user).id(user.hashCode()).addTo(this);
        }
        if (subscriptionPack == null) {
            new HomeSubsInactiveModel(onClickHomeSubsInactive).id("HomeSubsInactiveModel".hashCode()).addTo(this);
        }

        if (this.recommends.size() > 0) {
            new HomeTextModel(context.getString(R.string.recommend_plans)).id("Recommend_plans".hashCode()).addTo(this);
            List<EpoxyModel<?>> modelList = HomeRecommendPacketGroup.getModels(this.recommends, onClickRecommendItem);
            new HomeRecommendPacketGroup().models(modelList).id(this.recommends.hashCode()).numViewsToShowOnScreen(1.2f).addTo(this);
        }

        new HomeTextActionModel(onClickListenerAction, context.getString(R.string.status_plans), context.getString(R.string.view_all)).id("StatusPlan".hashCode()).addTo(this);
        if (subscriptionPack == null) {
            new HomeNoPlan(R.drawable.ic_no_pack_active, context.getString(R.string.there_no_active_plan)).id("ThereIsNoActivePlan".hashCode()).addTo(this);
        } else {
            new HomeActivePlan(subscriptionPack.title).id(subscriptionPack.title.hashCode()).addTo(this);
        }
    }

    public void setNotificationClickListener(HomeTopBarModel.OnClickListener notificationClickListener) {
        this.notificationClickListener = notificationClickListener;
    }
}