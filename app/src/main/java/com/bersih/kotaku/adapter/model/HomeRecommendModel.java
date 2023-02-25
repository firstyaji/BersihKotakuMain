package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewCardRecommendBinding;
import com.bersih.kotaku.firebase.model.Recommend;
import com.bersih.kotaku.utils.GlideApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeRecommendModel extends EpoxyModelWithHolder<HomeRecommendModel.Holder> {
    private final OnClickRecommendItem onClickRecommendItem;
    private final Recommend data;

    public HomeRecommendModel(OnClickRecommendItem clickRecommendItem, Recommend data) {
        this.onClickRecommendItem = clickRecommendItem;
        this.data = data;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_card_recommend;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(this.onClickRecommendItem, data);
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(onClickRecommendItem, data);
    }

    static class Holder extends EpoxyHolder {
        private ViewCardRecommendBinding viewBinding;
        private final OnClickRecommendItem onClickRecommendItem;
        private final Recommend data;

        public Holder(OnClickRecommendItem onClickRecommendItem, Recommend data) {
            this.onClickRecommendItem = onClickRecommendItem;
            this.data = data;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewCardRecommendBinding.bind(itemView);
            bind(onClickRecommendItem, data);
        }

        void bind(OnClickRecommendItem onClickRecommendItem, Recommend data) {
            viewBinding.getRoot().setVisibility(View.VISIBLE);
            viewBinding.packRecommendTitle.setText(data.title);
            StorageReference image = FirebaseStorage.getInstance().getReferenceFromUrl(data.image);
            GlideApp.with(viewBinding.getRoot()).load(image).into(viewBinding.packRecommendImage);
            /*
            viewBinding.getRoot().setOnClickListener(v -> {
                if (onClickRecommendItem != null) {
                    onClickRecommendItem.onCLick(data);
                }
            });

             */
        }
    }

    public interface OnClickRecommendItem {
        void onCLick(Recommend data);
    }
}
