package com.ashwin.rxjavasandbox.operator.filter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ashwin.rxjavasandbox.Constant;
import com.ashwin.rxjavasandbox.databinding.ActivityFilterBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class FilterActivity extends AppCompatActivity {
    private Observable<Integer> observable;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFilterBinding binding = ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.subscribeButton.setOnClickListener(v -> {
            subscribe();
        });

        binding.unsubscribeButton.setOnClickListener(v -> {
            unsubscribe();
        });

        FilterRepository filterRepository = new FilterRepository();
        observable = filterRepository.getObservable();
    }

    private void subscribe() {
        observable
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())  // This will cause filter to run on main thread
                .observeOn(Schedulers.computation())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        Log.d(Constant.TAG, FilterActivity.this.getClass().getSimpleName() + ": filter.test( " + integer + " ) ( " + Thread.currentThread().getName() + " )");
                        return integer % 2 == 0;  // Observer will only receive even numbers
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Constant.TAG, FilterActivity.this.getClass().getSimpleName() + ": onSubscribe ( " + Thread.currentThread().getName() + " )");
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.d(Constant.TAG, FilterActivity.this.getClass().getSimpleName() + ": onNext ( " + integer + " ) ( " + Thread.currentThread().getName() + " )");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(Constant.TAG, FilterActivity.this.getClass().getSimpleName() + ": onError ( " + Thread.currentThread().getName() + " )", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constant.TAG, FilterActivity.this.getClass().getSimpleName() + ": onComplete ( " + Thread.currentThread().getName() + " )");
                    }
                });
    }

    private void unsubscribe() {
        if (disposable != null) {
            Log.d(Constant.TAG, this.getClass().getSimpleName() + ": unsubscribe: isDisposed: " + disposable.isDisposed());
            disposable.dispose();  // No issue if already disposed
        } else {
            Log.d(Constant.TAG, this.getClass().getSimpleName() + ": unsubscribe: disposable null");
        }
    }
}
