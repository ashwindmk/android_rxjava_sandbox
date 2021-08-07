package com.ashwin.rxjavasandbox.subject.behavior;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ashwin.rxjavasandbox.Constant;
import com.ashwin.rxjavasandbox.databinding.ActivityBehaviorSubjectBinding;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public class BehaviorSubjectActivity extends AppCompatActivity {
    private Disposable disposable1, disposable2;
    private BehaviorSubject<Integer> behaviorSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBehaviorSubjectBinding binding = ActivityBehaviorSubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.subscribeButton.setOnClickListener(v -> {
            subscribe();
        });

        binding.unsubscribeButton.setOnClickListener(v -> {
            unsubscribe();
        });

        BehaviorSubjectRepository behaviorSubjectRepository = new BehaviorSubjectRepository();
        behaviorSubject = behaviorSubjectRepository.getBehaviorSubject();
    }

    private void subscribe() {
        behaviorSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 1 onSubscribe ( " + Thread.currentThread().getName() + " )");
                        disposable1 = d;
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        // Will receive the last emitted value and then the live values until the subject is completed.
                        // No value will be received after the subject is completed.
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 1 onNext( " + integer + " ) ( " + Thread.currentThread().getName() + " )");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 1 onError ( " + Thread.currentThread().getName() + " )");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 1 onComplete ( " + Thread.currentThread().getName() + " )");
                    }
                });

        behaviorSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 2 onSubscribe ( " + Thread.currentThread().getName() + " )");
                        disposable2 = d;
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 2 onNext( " + integer + " ) ( " + Thread.currentThread().getName() + " )");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 2 onError ( " + Thread.currentThread().getName() + " )");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 2 onComplete ( " + Thread.currentThread().getName() + " )");
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