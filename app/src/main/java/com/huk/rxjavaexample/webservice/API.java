package com.huk.rxjavaexample.webservice;

import com.huk.rxjavaexample.model.Model;
import com.huk.rxjavaexample.utils.AppConstants;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by huk on 25-06-2017.
 */

public class API {

    private static API mInstance;

    public static API getInstance() {
        if (mInstance == null) {
            mInstance = new API();
        }
        return mInstance;
    }

    public Observable<Model> getUserData() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();

        GetData apiService = retrofit.create(GetData.class);

        return apiService.getUserData();
    }
}
