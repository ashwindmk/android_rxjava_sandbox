package com.ashwin.rxjavasandbox.operator.reduce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ashwin.rxjavasandbox.Constant;
import com.ashwin.rxjavasandbox.databinding.ActivityReduceBinding;

import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class ReduceActivity extends AppCompatActivity {
    private Observable<Integer> observable;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityReduceBinding binding = ActivityReduceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.subscribeButton.setOnClickListener(v -> {
            subscribe();
        });

        binding.unsubscribeButton.setOnClickListener(v -> {
            unsubscribe();
        });

        ReduceRepository reduceRepository = new ReduceRepository();
        observable = reduceRepository.getObservable();
    }

    private void subscribe() {
        observable
                .subscribeOn(Schedulers.io())
                // Reducer will return the final result after the emitter completes emitting.
                // You will need a MaybeObserver if using reducer.
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @NonNull
                    @Override
                    public Integer apply(@NonNull Integer i1, @NonNull Integer i2) throws Exception {
                        return i1 + i2;
                    }
                })
                // We can provide the seed (initial) value as argument to the reducer operator.
//                .reduce(10, new BiFunction<Integer, Integer, Integer>() {
//                    @NonNull
//                    @Override
//                    public Integer apply(@NonNull Integer i1, @NonNull Integer i2) throws Exception {
//                        return i1 + i2;
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new MaybeObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Constant.TAG, ReduceActivity.this.getClass().getSimpleName() + ": onSubscribe ( " + Thread.currentThread().getName() + " )");
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        // Gets the final result only once at the end.
                        Log.d(Constant.TAG, ReduceActivity.this.getClass().getSimpleName() + ": onSuccess ( " + integer + " ) ( " + Thread.currentThread().getName() + " )");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(Constant.TAG, ReduceActivity.this.getClass().getSimpleName() + ": onError ( " + Thread.currentThread().getName() + " )", e);
                    }

                    @Override
                    public void onComplete() {
                        // If success or error, then this will NOT be called, since this Maybe operator.
                        Log.d(Constant.TAG, ReduceActivity.this.getClass().getSimpleName() + ": onComplete ( " + Thread.currentThread().getName() + " )");
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
