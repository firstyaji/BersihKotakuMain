package com.bersih.kotaku.adapter;

import com.airbnb.epoxy.AsyncEpoxyController;
import com.bersih.kotaku.adapter.model.PickerItemModel;
import com.bersih.kotaku.adapter.model.SubsItemModel;
import com.bersih.kotaku.firebase.model.Picker;
import com.bersih.kotaku.firebase.model.Subscription;

import java.util.ArrayList;
import java.util.List;

public class PickerController extends AsyncEpoxyController {
    private PickerItemModel.OnClickListener onClickListener;
    private List<Picker> pickerList = new ArrayList<>();

    public void setPickerList(List<Picker> notificationsList) {
        this.pickerList = notificationsList;
        requestModelBuild();
    }

    public void setOnClickListener(PickerItemModel.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void buildModels() {
        for (Picker picker: pickerList) {
            new PickerItemModel(onClickListener, picker).id(picker.hashCode()).addTo(this);
        }
    }
}
