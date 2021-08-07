package com.ashwin.rxjavasandbox.subject.unicast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ashwin.rxjavasandbox.Constant;
import com.ashwin.rxjavasandbox.databinding.ActivityUnicastSubjectBinding;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.UnicastSubject;

public class UnicastSubjectActivity extends AppCompatActivity {
    private UnicastSubject<Integer> unicastSubject;
    private Disposable disposable1, disposable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUnicastSubjectBinding binding = ActivityUnicastSubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startButton.setOnClickListener(v -> {
            UnicastSubjectRepository unicastSubjectRepository = new UnicastSubjectRepository();
            unicastSubject = unicastSubjectRepository.getUnicastSubject();
        });

        binding.subscribeButton.setOnClickListener(v -> {
            subscribe();
        });

        binding.unsubscribeButton.setOnClickListener(v -> {
            unsubscribe();
        });
    }

    private void subscribe() {
        unicastSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 1 onSubscribe ( " + Thread.currentThread().getName() + " )");
                        disposable1 = d;
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        // Will get live or replayed values here for only one (first) observer.
                        // Will get all the values replayed even if subscribed after the emitter has completed.
                        // Cannot subscribe to the same UnicaseSubject instance again.
                        // If the observer unsubscribed before onComplete, then no one will ever get any remaining values.
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 1 onNext( " + integer + " ) ( " + Thread.currentThread().getName() + " )");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(Constant.TAG, this.getClass().getSimpleName() + ": 1 onError ( " + Thread.currentThread().getName() + " )", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constant.TAG, this.getClass().getSimpleName() + ": 1 onComplete ( " + Thread.currentThread().getName() + " )");
                    }
                });

        unicastSubject
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
                        Log.e(Constant.TAG, this.getClass().getSimpleName() + ": 2 onError ( " + Thread.currentThread().getName() + " )", e);
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
