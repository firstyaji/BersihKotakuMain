package com.bersih.kotaku.adapter.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.airbnb.epoxy.Carousel;
import com.airbnb.epoxy.CarouselModel_;
import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.OnModelBoundListener;
import com.bersih.kotaku.firebase.model.Recommend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomeRecommendPacketGroup extends CarouselModel_ {

    public static List<EpoxyModel<?>> getModels(List<Recommend> data, HomeRecommendModel.OnClickRecommendItem onClickRecommendItem) {
        return Arrays.stream(data.toArray().clone()).map(v -> (Recommend)v)
                .map(v -> new HomeRecommendModel(onClickRecommendItem, v).id(v.id))
                .collect(Collectors.toList());
    }
}
