package com.ashwin.rxjavasandbox.subject.single;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ashwin.rxjavasandbox.Constant;
import com.ashwin.rxjavasandbox.databinding.ActivitySingleSubjectBinding;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.SingleSubject;

public class SingleSubjectActivity extends AppCompatActivity {
    private Disposable disposable1, disposable2;
    private SingleSubject<Integer> singleSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySingleSubjectBinding binding = ActivitySingleSubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.subscribeButton.setOnClickListener(v -> {
            subscribe();
        });

        binding.unsubscribeButton.setOnClickListener(v -> {
            unsubscribe();
        });

        SingleSubjectRepository singleSubjectRepository = new SingleSubjectRepository();
        singleSubject = singleSubjectRepository.getSingleSubject();
    }

    private void subscribe() {
        singleSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 1 onSubscribe ( " + Thread.currentThread().getName() + " )");
                        disposable1 = d;
                    }

                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        // Will get the one (first) value emitted by the emitter.
                        // Will get the single value replayed even if subscribed after the emitter has succeeded (completed).
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 1 onSuccess( " + integer + " ) ( " + Thread.currentThread().getName() + " )");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 1 onError ( " + Thread.currentThread().getName() + " )");
                    }
                });

        singleSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 2 onSubscribe ( " + Thread.currentThread().getName() + " )");
                        disposable2 = d;
                    }

                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 2 onSuccess( " + integer + " ) ( " + Thread.currentThread().getName() + " )");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 2 onError ( " + Thread.currentThread().getName() + " )");
                    }
                });
    }

    private void unsubscribe() {
        if (disposable1 != null) {
            if (!disposable1.isDisposed()) {
                disposable1.dispose();
                Log.d(Constant.TAG, this.getClass().getSimpleName() + ": disposed disposable 1");
            } else {
                Log.d(Constant.TAG, this.getClass().getSimpleName() + ": disposable 1 already disposed");
            }
        }
        if (disposable2 != null) {
            if (!disposable2.isDisposed()) {
                disposable2.dispose();
                Log.d(Constant.TAG, this.getClass().getSimpleName() + ": disposed disposable 2");
            } else {
                Log.d(Constant.TAG, this.getClass().getSimpleName() + ": disposable 2 already disposed");
            }
        }
    }
}
