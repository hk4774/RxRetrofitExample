package com.huk.rxjavaexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.huk.rxjavaexample.model.Contact;
import com.huk.rxjavaexample.model.Model;
import com.huk.rxjavaexample.webservice.API;

import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ListView mListViewUserData;
    private ProgressBar mProgressBar;
    private Subscription mUserDataSubscription;
    private String TAG = MainActivity.class.getSimpleName();
    private Observable<Model> mUserDataObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        callAPI();
    }

    private void callAPI() {
        mUserDataObservable = API.getInstance().getUserData();
        mUserDataSubscription = mUserDataObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Model>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Task Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Model model) {
                        mProgressBar.setVisibility(View.GONE);
                        List<String> userNameList = new ArrayList<>();
                        for (Contact contact : model.getContacts()) {
                            userNameList.add(contact.getName());
                        }
                        ArrayAdapter<String> userDataAdpater = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, userNameList);
                        mListViewUserData.setAdapter(userDataAdpater);
                    }
                });
    }

    private void init() {
        mListViewUserData = (ListView) findViewById(R.id.lv_user_data);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
    }

    protected void onDestroy() {
        this.mUserDataSubscription.unsubscribe();
        super.onDestroy();
    }
}
