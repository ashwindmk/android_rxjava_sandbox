package com.ashwin.rxjavasandbox.operator.scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ashwin.rxjavasandbox.Constant;
import com.ashwin.rxjavasandbox.databinding.ActivityScanBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class ScanActivity extends AppCompatActivity {
    private Observable<Integer> observable;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityScanBinding binding = ActivityScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.subscribeButton.setOnClickListener(v -> {
            subscribe();
        });

        binding.unsubscribeButton.setOnClickListener(v -> {
            unsubscribe();
        });

        ScanRepository scanRepository = new ScanRepository();
        observable = scanRepository.getObservable();
    }

    private void subscribe() {
        observable
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @NonNull
                    @Override
                    public Integer apply(@NonNull Integer i1, @NonNull Integer i2) throws Exception {
                        // i1 is the previous returned/onNext value, 0 if i2 is the first value emitted by the observable.
                        // i2 is the next value emitted by the observable.
                        return i1 + i2;
                    }
                })
                // Initial value can be passed as an argument to scan operator.
//                .scan(10, new BiFunction<Integer, Integer, Integer>() {
//                    @NonNull
//                    @Override
//                    public Integer apply(@NonNull Integer i1, @NonNull Integer i2) throws Exception {
//                        return i1 + i2;
//                    }
//                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Constant.TAG, ScanActivity.this.getClass().getSimpleName() + ": onSubscribe ( " + Thread.currentThread().getName() + " )");
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        // Gets the value returned by the scan operator
                        Log.d(Constant.TAG, ScanActivity.this.getClass().getSimpleName() + ": onNext ( " + integer + " ) ( " + Thread.currentThread().getName() + " )");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(Constant.TAG, ScanActivity.this.getClass().getSimpleName() + ": onError ( " + Thread.currentThread().getName() + " )", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constant.TAG, ScanActivity.this.getClass().getSimpleName() + ": onComplete ( " + Thread.currentThread().getName() + " )");
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
