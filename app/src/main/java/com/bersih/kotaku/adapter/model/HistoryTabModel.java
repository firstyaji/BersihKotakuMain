package com.bersih.kotaku.adapter.model;

import android.graphics.Color;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bersih.kotaku.R;
import com.bersih.kotaku.databinding.ViewHistoryTabBinding;

import java.util.List;

public class HistoryTabModel extends EpoxyModelWithHolder<HistoryTabModel.Holder> {
    private final OnClickListener onClickListener;
    private final List<String> stringList;
    private final String selected;

    public HistoryTabModel(OnClickListener onClickListener, List<String> stringList, String selected) {
        this.onClickListener = onClickListener;
        this.stringList = stringList;
        this.selected = selected;
    }

    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        holder.bind(onClickListener, stringList, selected);
    }

    @Override
    protected Holder createNewHolder(@NonNull ViewParent parent) {
        return new Holder(onClickListener, stringList, selected);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_history_tab;
    }


    public static class Holder extends EpoxyHolder {
        private ViewHistoryTabBinding viewBinding;
        private final OnClickListener onClickListener;
        private final List<String> stringList;
        private final String selected;

        public Holder(OnClickListener onClickListener, List<String> stringList, String selected) {
            this.onClickListener = onClickListener;
            this.stringList = stringList;
            this.selected = selected;
        }

        @Override
        protected void bindView(@NonNull View itemView) {
            viewBinding = ViewHistoryTabBinding.bind(itemView);
            bind(onClickListener, stringList, selected);
        }

        void bind(OnClickListener onClickListener, List<String> stringList, String selected) {
            if (viewBinding == null) return;
            if (stringList.size() == 2) {
                viewBinding.historyTabSubs.setText(stringList.get(0));
                viewBinding.historyTabPicker.setText(stringList.get(1));
                if (selected.equals(stringList.get(0))) {
                    viewBinding.historyTabSubs.setTextColor(Color.parseColor("#4FBF67"));
                    viewBinding.historyTabPicker.setTextColor(Color.parseColor("#69757C"));
                } else if (selected.equals(stringList.get(1))) {
                    viewBinding.historyTabPicker.setTextColor(Color.parseColor("#4FBF67"));
                    viewBinding.historyTabSubs.setTextColor(Color.parseColor("#69757C"));
                }
                viewBinding.historyTabSubs.setOnClickListener(v -> {
                    if (v instanceof Button) {
                        onClickListener.onClick(((Button) v).getText().toString());
                    }
                });
                viewBinding.historyTabPicker.setOnClickListener(v -> {
                    if (v instanceof Button) {
                        onClickListener.onClick(((Button) v).getText().toString());
                    }
                });
            }
        }
    }

    public interface OnClickListener {
        void onClick(String value);
    }
}
