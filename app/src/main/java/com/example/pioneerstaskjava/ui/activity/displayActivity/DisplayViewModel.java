package com.example.pioneerstaskjava.ui.activity.displayActivity;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pioneerstaskjava.data.api.ApiService;
import com.example.pioneerstaskjava.data.api.ApiServiceFactory;
import com.example.pioneerstaskjava.data.model.GitModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DisplayViewModel extends ViewModel {
    ApiService service = ApiServiceFactory.getRetrofitInstance();
    MutableLiveData<GitModel> completeDataSuccess = new MutableLiveData<>();

    @SuppressLint("CheckResult")
    void getData(String created
            ,
                 String per_page
            , String lang) {
        service.getData(created, "stars",
                "desc",
                per_page,
                lang).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(gitModel -> {
                    completeDataSuccess.postValue(gitModel);
                }, throwable -> Log.d("tag", "getData: ")
        );

    }

    LiveData<GitModel> getDataSuccess() {
        return completeDataSuccess;
    }
}