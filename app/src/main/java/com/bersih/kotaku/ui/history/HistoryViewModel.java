package com.bersih.kotaku.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bersih.kotaku.firebase.model.Picker;
import com.bersih.kotaku.firebase.model.Subscription;
import com.bersih.kotaku.firebase.service.PackService;
import com.bersih.kotaku.firebase.service.PaymentService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class HistoryViewModel extends ViewModel {
    private final PackService packService;
    private final PaymentService paymentService;
    private final MutableLiveData<String> _selected = new MutableLiveData<>();
    private final MutableLiveData<List<String>> _menuList = new MutableLiveData<>();
    LiveData<String> selected = _selected;
    LiveData<List<String>> menuList = _menuList;

    @Inject public HistoryViewModel(PackService packService, PaymentService paymentService) {
        this.packService = packService;
        this.paymentService = paymentService;
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Pesanan");
        menuItems.add("Sampah");
        _menuList.postValue(menuItems);
        _selected.postValue("Pesanan");
    }

    public LiveData<List<Subscription>> listSubs() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return null;
        }
        Observable<List<Subscription>> paymentObservable = this.packService.listenActiveSubsss(firebaseUser.getUid())
                .observeOn(Schedulers.io());
        return LiveDataReactiveStreams.fromPublisher(paymentObservable.toFlowable(BackpressureStrategy.LATEST));
    }

    public LiveData<List<Picker>> listPicker() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return null;
        }
        Observable<List<Picker>> paymentObservable = this.packService.listenPickerr(firebaseUser.getUid())
                .observeOn(Schedulers.io());
        return LiveDataReactiveStreams.fromPublisher(paymentObservable.toFlowable(BackpressureStrategy.LATEST));
    }

    void setSelected(String value) {
        _selected.postValue(value);
    }
}