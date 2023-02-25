package com.bersih.kotaku.adapter;

import android.content.Context;

import com.airbnb.epoxy.AsyncEpoxyController;
import com.bersih.kotaku.R;
import com.bersih.kotaku.adapter.model.HeaderModel;
import com.bersih.kotaku.adapter.model.HistoryNoData;
import com.bersih.kotaku.adapter.model.HistoryPesananNoData;
import com.bersih.kotaku.adapter.model.HistoryTabModel;
import com.bersih.kotaku.adapter.model.HistoryTrash_no_data;
import com.bersih.kotaku.adapter.model.PickerItemModel;
import com.bersih.kotaku.adapter.model.SubsItemModel;
import com.bersih.kotaku.firebase.model.Picker;
import com.bersih.kotaku.firebase.model.Subscription;

import java.util.ArrayList;
import java.util.List;

public class HistoryController extends AsyncEpoxyController {
    private SubsItemModel.OnClickListener subsOnClickListener;
    private PickerItemModel.OnClickListener pickOnClickListener;
    private HistoryTabModel.OnClickListener tabOnClickListener;
    private List<Subscription> subscriptionList = new ArrayList<>();
    private List<Picker> pickerList = new ArrayList<>();
    private List<String> tabMenu = new ArrayList<>();
    private String selected;
    private final Context context;

    public HistoryController(Context context) {
        this.context = context;
    }

    public void setSubsOnClickListener(SubsItemModel.OnClickListener subsOnClickListener) {
        this.subsOnClickListener = subsOnClickListener;
    }

    public void setPickOnClickListener(PickerItemModel.OnClickListener pickOnClickListener) {
        this.pickOnClickListener = pickOnClickListener;
    }

    public void setTabOnClickListener(HistoryTabModel.OnClickListener tabOnClickListener) {
        this.tabOnClickListener = tabOnClickListener;
    }

    public void setSubscriptionList(List<Subscription> subscriptionList) {
        this.subscriptionList = subscriptionList;
        requestModelBuild();
    }

    public void setPickerList(List<Picker> pickerList) {
        this.pickerList = pickerList;
        requestModelBuild();
    }

    public void setTabMenu(List<String> tabMenu) {
        this.tabMenu = tabMenu;
        requestModelBuild();
    }

    public void setSelected(String selected) {
        this.selected = selected;
        requestModelBuild();
    }

    @Override
    protected void buildModels() {
        new HeaderModel(null, context.getString(R.string.history_title), R.drawable.ic_arrow_back).id("history".hashCode()).addTo(this);

        if (tabMenu.size() == 2 && selected != null) {
            new HistoryTabModel(tabOnClickListener, tabMenu, selected).id(selected, tabMenu.size()).addTo(this);

            if (selected.equals(tabMenu.get(0))) {
                if (subscriptionList.size() == 0) {
                  //  new HistoryNoData().id("history_controller_no_data".hashCode()).addTo(this);
                    new HistoryPesananNoData().id("history_controller_no_data".hashCode()).addTo(this);
                }
                for (Subscription subscription: subscriptionList) {
                    new SubsItemModel(subsOnClickListener, subscription).id(subscription.hashCode()).addTo(this);
                }
            } else if (selected.equals(tabMenu.get(1))) {
                if (pickerList.size() == 0) {
                  //  new HistoryNoData().id("history_controller_no_data".hashCode()).addTo(this);
                    new HistoryTrash_no_data().id("history_controller_no_data".hashCode()).addTo(this);
                }
                for (Picker picker: pickerList) {
                    new PickerItemModel(pickOnClickListener, picker).id(picker.hashCode()).addTo(this);
                }
            }
        }
    }


}
