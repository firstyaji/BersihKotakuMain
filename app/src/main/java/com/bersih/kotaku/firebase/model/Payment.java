package com.bersih.kotaku.firebase.model;

import com.google.firebase.firestore.Exclude;

public class Payment extends Model {
    public String icon;
    public String bankName;
    public String bankAccountName;
    public String bankAccountNumber;
    @Exclude
    public boolean isSelected;
}
