package com.ashwin.rxjavasandbox.subject.replay;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ashwin.rxjavasandbox.Constant;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

public class ReplaySubjectRepository {
    private Observable<Integer> getObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(Constant.TAG, this.getClass().getSimpleName() + ": ObservableOnSubscribe: subscribe ( " + Thread.currentThread().getName() + " )");
                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(5000);
                    Log.d(Constant.TAG, this.getClass().getSimpleName() + ": emitter.onNext( " + i + " ) ( " + Thread.currentThread().getName() + " )");
                    emitter.onNext(i);
                }
                Log.d(Constant.TAG, this.getClass().getSimpleName() + ": emitter.onComplete() ( " + Thread.currentThread().getName() + " )");
                emitter.onComplete();
            }
        });
    }

    public ReplaySubject<Integer> getReplaySubject() {
        ReplaySubject<Integer> replaySubject = ReplaySubject.create();
        return getObservable()
                .subscribeOn(Schedulers.io())
                .subscribeWith(replaySubject);
    }
}
