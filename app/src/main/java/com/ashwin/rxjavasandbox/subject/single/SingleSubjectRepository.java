package com.ashwin.rxjavasandbox.subject.single;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ashwin.rxjavasandbox.Constant;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.SingleSubject;

public class SingleSubjectRepository {
    private Single<Integer> getObservable() {
        return Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Integer> emitter) throws Exception {
                Log.d(Constant.TAG, this.getClass().getSimpleName() + ": SingleOnSubscribe: subscribe ( " + Thread.currentThread().getName() + " )");
                Thread.sleep(5000);
                Log.d(Constant.TAG, this.getClass().getSimpleName() + ": emitter.onSuccess( 1 ) ( " + Thread.currentThread().getName() + " )");
                emitter.onSuccess(1);

                emitter.onSuccess(2);  // This will not be emitted.
            }
        });
    }

    public SingleSubject<Integer> getSingleSubject() {
        SingleSubject<Integer> singleSubject = SingleSubject.create();
        return getObservable()
                .subscribeOn(Schedulers.io())
                .subscribeWith(singleSubject);
    }
}
