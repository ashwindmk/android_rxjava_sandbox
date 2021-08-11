package com.ashwin.rxjavasandbox.flowable.buffer;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ashwin.rxjavasandbox.Constant;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
//import io.reactivex.Observable;
//import io.reactivex.ObservableEmitter;
//import io.reactivex.ObservableOnSubscribe;

public class BufferFlowableRepository {
    private final String NAME = this.getClass().getSimpleName();

    private Flowable<Integer> getFlowable() {
        return Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(Constant.TAG,  NAME + ": FlowableOnSubscribe: subscribe ( " + Thread.currentThread().getName() + " )");
                for (int i = 1; i <= 500; i++) {
                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {
                        Log.e(Constant.TAG, NAME + ": Sleep error", e);
                    }
                    if (emitter.isCancelled()) {
                        Log.d(Constant.TAG, NAME + ": Subscription cancelled");
                        break;
                    }
                    Log.d(Constant.TAG, NAME + ": emitter.onNext( " + i + " ) ( " + Thread.currentThread().getName() + " )");
                    emitter.onNext(i);
                }
                Log.d(Constant.TAG, NAME + ": emitter.onComplete() ( " + Thread.currentThread().getName() + " )");
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);  // BackpressureStrategy will be used if no back-pressure handling is done by the subscriber.

        // Alternatively, we can convert an Observable to Flowable
        /*return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(Constant.TAG,  NAME + ": FlowableOnSubscribe: subscribe ( " + Thread.currentThread().getName() + " )");
                for (int i = 1; i <= 500; i++) {
                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {
                        Log.e(Constant.TAG, NAME + ": Sleep error", e);
                    }
                    if (emitter.isDisposed()) {
                        Log.d(Constant.TAG, NAME + ": Subscription disposed");
                        break;
                    }
                    Log.d(Constant.TAG, NAME + ": emitter.onNext( " + i + " ) ( " + Thread.currentThread().getName() + " )");
                    emitter.onNext(i);
                }
                Log.d(Constant.TAG, NAME + ": emitter.onComplete() ( " + Thread.currentThread().getName() + " )");
                emitter.onComplete();
            }
        }).toFlowable(BackpressureStrategy.BUFFER);*/
    }

    public Flowable<Integer> getBufferFlowable() {
        return getFlowable();
    }
}
