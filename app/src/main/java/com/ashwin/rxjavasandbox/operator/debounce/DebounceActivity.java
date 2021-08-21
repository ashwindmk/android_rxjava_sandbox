package com.ashwin.rxjavasandbox.operator.debounce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ashwin.rxjavasandbox.Constant;
import com.ashwin.rxjavasandbox.databinding.ActivityDebounceBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DebounceActivity extends AppCompatActivity {
    private Observable<Integer> observable;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDebounceBinding binding = ActivityDebounceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.subscribeButton.setOnClickListener(v -> {
            subscribe();
        });

        binding.unsubscribeButton.setOnClickListener(v -> {
            unsubscribe();
        });

        DebounceRepository debounceRepository = new DebounceRepository();
        observable = debounceRepository.getObservable();
    }

    private void subscribe() {
        observable
                .subscribeOn(Schedulers.io())
                // If previous value was emitted within 1 sec, then debounce will discard the previous value.
                // It will allow the last value.
                .debounce(1000L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Constant.TAG, DebounceActivity.this.getClass().getSimpleName() + ": onSubscribe ( " + Thread.currentThread().getName() + " )");
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.d(Constant.TAG, DebounceActivity.this.getClass().getSimpleName() + ": onNext ( " + integer + " ) ( " + Thread.currentThread().getName() + " )");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(Constant.TAG, DebounceActivity.this.getClass().getSimpleName() + ": onError ( " + Thread.currentThread().getName() + " )", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constant.TAG, DebounceActivity.this.getClass().getSimpleName() + ": onComplete ( " + Thread.currentThread().getName() + " )");
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