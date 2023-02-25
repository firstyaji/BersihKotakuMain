package com.bersih.kotaku.adapter;

import android.content.Context;

import com.airbnb.epoxy.AsyncEpoxyController;
import com.bersih.kotaku.R;
import com.bersih.kotaku.adapter.model.AddAddressModel;
import com.bersih.kotaku.adapter.model.AddressAddedModel;
import com.bersih.kotaku.adapter.model.ButtonModel;
import com.bersih.kotaku.adapter.model.HeaderModel;
import com.bersih.kotaku.adapter.model.HomeTextModel;
import com.bersih.kotaku.adapter.model.OrderDetailsModel;
import com.bersih.kotaku.adapter.model.PaymentModel;
import com.bersih.kotaku.adapter.model.UploadReceiptModel;
import com.bersih.kotaku.firebase.model.Pack;
import com.bersih.kotaku.firebase.model.Payment;
import com.bersih.kotaku.firebase.model.User;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class PaymentController extends AsyncEpoxyController {
    private User user;
    private Pack pack;
    private List<Payment> paymentList;
    private String paymentReceipt;
    private Boolean isFilled;

    private HeaderModel.OnClickListener headerModelOnClick;
    private AddAddressModel.OnClickListener addAddressOnClick;
    private PaymentModel.OnClickListener paymentOnClick;
    private ButtonModel.OnClickListener buttonPayOnClick;
    private UploadReceiptModel.OnClickListener requestReceiptOnClick;
    private final Context context;

    public PaymentController(Context context) {
        this.context = context;
    }

    public void setUser(User user) {
        this.user = user;
        requestModelBuild();
    }

    public void setPack(Pack pack) {
        this.pack = pack;
        requestModelBuild();
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
        requestModelBuild();
    }

    public void setPaymentReceipt(String paymentReceipt) {
        this.paymentReceipt = paymentReceipt;
        requestModelBuild();
    }

    public void setFilled(Boolean filled) {
        this.isFilled = filled;
        requestModelBuild();
    }

    public void setHeaderModelOnClick(HeaderModel.OnClickListener headerModelOnClick) {
        this.headerModelOnClick = headerModelOnClick;
    }

    public void setAddAddressOnClick(AddAddressModel.OnClickListener addAddressOnClick) {
        this.addAddressOnClick = addAddressOnClick;
    }

    public void setPaymentOnClick(PaymentModel.OnClickListener paymentOnClick) {
        this.paymentOnClick = paymentOnClick;
    }

    public void setButtonPayOnClick(ButtonModel.OnClickListener buttonPayOnClick) {
        this.buttonPayOnClick = buttonPayOnClick;
    }

    public void setRequestReceiptOnClick(UploadReceiptModel.OnClickListener requestReceiptOnClick) {
        this.requestReceiptOnClick = requestReceiptOnClick;
    }

    @Override
    protected void buildModels() {
        new HeaderModel(this.headerModelOnClick, context.getString(R.string.payment_confirmation), R.drawable.ic_arrow_back).id("PaymentConfirmation".hashCode()).addTo(this);

        if (user != null && !Objects.equals(user.AddressName, "")) {
            new AddressAddedModel(user.AddressName, user.AddressDetails).id(user.hashCode()).addTo(this);
        } else {
            new AddAddressModel(this.addAddressOnClick, context.getString(R.string.add_address), R.drawable.ic_add).id("AddAddress".hashCode()).addTo(this);
        }

        if (pack != null) {
            new OrderDetailsModel(pack.title, pack.price).id(pack.hashCode()).addTo(this);
        }

        if (paymentList != null && paymentList.size() > 0) {
            new HomeTextModel(context.getString(R.string.select_payment_method)).id("SelectPaymentMethod".hashCode()).addTo(this);
            for (Payment payment: paymentList) {
                new PaymentModel(payment, this.paymentOnClick).id(UUID.randomUUID().toString()).addTo(this);
            }

            new UploadReceiptModel(paymentReceipt, requestReceiptOnClick).id(paymentReceipt.hashCode()).addTo(this);
        }

        if (isFilled != null && isFilled) {
            new ButtonModel(this.buttonPayOnClick, context.getString(R.string.confirmation)).id(isFilled.hashCode()).addTo(this);
        } else {
            new ButtonModel(()->{}, context.getString(R.string.please_fill_the_form)).id("Please fill the form".hashCode()).addTo(this);
        }
    }
}
