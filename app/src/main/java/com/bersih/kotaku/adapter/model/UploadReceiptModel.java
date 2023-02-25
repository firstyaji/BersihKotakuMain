package com.bersih.kotaku.adapter.model;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewUploadReceiptBinding;
import com.bersih.kotaku.utils.GlideApp;
import com.bersih.kotaku.utils.GlideFirebaseStorage;

public class UploadReceiptModel extends EpoxyModelWithHolder<UploadReceiptModel.Holder> {
    private final String contentImage;
    private final OnClickListener onClickListener;

    public UploadReceiptModel(String contentImage, OnClickListener onClickListener) {
        this.contentImage = contentImage;
        this.onClickListener = onClickListener;
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(onClickListener, contentImage);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_upload_receipt;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(onClickListener, contentImage);
    }

    static class Holder extends EpoxyHolder {
        private ViewUploadReceiptBinding viewBinding;
        private final OnClickListener onClickListener;
        private final String contentImage;

        public Holder(OnClickListener onClickListener, String contentImage) {
            this.onClickListener = onClickListener;
            this.contentImage = contentImage;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewUploadReceiptBinding.bind(itemView);
            bind(onClickListener, contentImage);
        }

        void bind(OnClickListener onClickListener, String contentImage) {
            if (viewBinding == null) return;
            viewBinding.uploadReceiptImage.setOnClickListener(v -> onClickListener.onClick());
         //   GlideFirebaseStorage.smartLoad(viewBinding.getRoot(), contentImage, viewBinding.uploadReceiptImage);
            GlideFirebaseStorage.smartLoad(viewBinding.getRoot(), contentImage, viewBinding.uploadReceiptImage, R.drawable.ic_file_upload_24);
        }
    }

    public interface OnClickListener {
        void onClick();
    }
}
