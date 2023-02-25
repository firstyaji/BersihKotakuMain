package com.bersih.kotaku.adapter;

import android.content.Context;

import com.airbnb.epoxy.AsyncEpoxyController;
import com.bersih.kotaku.R;
import com.bersih.kotaku.adapter.model.Header4Model;
import com.bersih.kotaku.adapter.model.HomeTextModel;
import com.bersih.kotaku.adapter.model.HomeTopBarModel;
import com.bersih.kotaku.adapter.model.ItemPackModel;
import com.bersih.kotaku.firebase.model.Pack;
import com.bersih.kotaku.firebase.model.Subscription;
import com.bersih.kotaku.firebase.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PackController extends AsyncEpoxyController {
    private User user;
    private List<Pack> packList = new ArrayList<>();
    private Pack subscription;

    private ItemPackModel.OnClickListener itemPackOnClickListener;
   // private HomeTopBarModel.OnClickListener homeClickListener;
   private Header4Model.OnClickListener headerbackhomeOnclick;
    private final Context context;

    public PackController(Context context) {
        this.context = context;
    }

    public void setUser(User user) {
        this.user = user;
        requestModelBuild();
    }

    public void setPackList(List<Pack> packList) {
        this.packList = packList;
        requestModelBuild();
    }

    public void setSubscription(Pack subscription) {
        this.subscription = subscription;
        requestModelBuild();
    }

    public void setItemPackOnClickListener(ItemPackModel.OnClickListener itemPackOnClickListener) {
        this.itemPackOnClickListener = itemPackOnClickListener;
    }

    public void setHeaderbackhomeOnclick(Header4Model.OnClickListener headerbackhomeOnclick) {
        this.headerbackhomeOnclick = headerbackhomeOnclick;
    }

    @Override
    protected void buildModels() {
        if (user != null) {
           // new HomeTopBarModel(homeClickListener).setData(user).id(user.hashCode()).addTo(this);
            new Header4Model(headerbackhomeOnclick, "Paket", R.drawable.ic_arrow_back).id(1).addTo(this);
        }

        if (packList.size() > 0) {
            new HomeTextModel(context.getString(R.string.pick_plan)).id("PickPlan".hashCode()).addTo(this);
            for (Pack p: packList) {
                if (subscription == null) {
                    new ItemPackModel(this.itemPackOnClickListener, p, true).id(UUID.randomUUID().hashCode()).addTo(this);
                } else {
                    new ItemPackModel(this.itemPackOnClickListener, p, false).id(UUID.randomUUID().hashCode()).addTo(this);
                }
            }
        }
    }
    /*
    public void setHomeClickListener(HomeTopBarModel.OnClickListener homeClickListener) {
        this.homeClickListener = homeClickListener;
    }

     */
}
