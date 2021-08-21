package com.ashwin.rxjavasandbox.operator.repeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ashwin.rxjavasandbox.Constant;
import com.ashwin.rxjavasandbox.databinding.ActivityRepeatBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RepeatActivity extends AppCompatActivity {
    private Observable<Integer> observable;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRepeatBinding binding = ActivityRepeatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.subscribeButton.setOnClickListener(v -> {
            subscribe();
        });

        binding.unsubscribeButton.setOnClickListener(v -> {
            unsubscribe();
        });

        RepeatRepository repeatRepository = new RepeatRepository();
        observable = repeatRepository.getObservable();
    }

    private void subscribe() {
        observable
                //.repeat()  // This will repeat infinitely
                .repeat(3)  // This will invoke the subscribe 3 times.
                // Even though emitter will complete 3 times, but observer will get onComplete only once at the end.
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Constant.TAG, RepeatActivity.this.getClass().getSimpleName() + ": onSubscribe ( " + Thread.currentThread().getName() + " )");
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.d(Constant.TAG, RepeatActivity.this.getClass().getSimpleName() + ": onNext ( " + integer + " ) ( " + Thread.currentThread().getName() + " )");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(Constant.TAG, RepeatActivity.this.getClass().getSimpleName() + ": onError ( " + Thread.currentThread().getName() + " )", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constant.TAG, RepeatActivity.this.getClass().getSimpleName() + ": onComplete ( " + Thread.currentThread().getName() + " )");
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