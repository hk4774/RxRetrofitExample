package com.huk.rxjavaexample.webservice;

import com.huk.rxjavaexample.model.Model;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by huk on 25-06-2017.
 */

public interface GetData {

    @GET("/contacts")
    Observable<Model> getUserData();

}
