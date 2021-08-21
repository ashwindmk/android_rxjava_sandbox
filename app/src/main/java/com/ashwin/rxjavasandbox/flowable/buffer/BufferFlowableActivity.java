package com.ashwin.rxjavasandbox.flowable.buffer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.ashwin.rxjavasandbox.Constant;
import com.ashwin.rxjavasandbox.databinding.ActivityBufferFlowableBinding;

import org.reactivestreams.Subscription;

import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
//import io.reactivex.subscribers.ResourceSubscriber;

public class BufferFlowableActivity extends AppCompatActivity {
    private final String NAME = this.getClass().getSimpleName();
    private Subscription subscription1, subscription2;
    private Flowable<Integer> bufferFlowable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBufferFlowableBinding binding = ActivityBufferFlowableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.subscribeButton.setOnClickListener(v -> {
            subscribe();
        });

        binding.unsubscribeButton.setOnClickListener(v -> {
            unsubscribe();
        });

        BufferFlowableRepository bufferFlowableRepository = new BufferFlowableRepository();
        bufferFlowable = bufferFlowableRepository.getBufferFlowable();
    }

    private void subscribe() {
        int bufferSize = 1;
        int capacity = 2;  // Buffer-capacity = bufferSize * capacity
        Action action = () -> {
            Log.d(Constant.TAG, NAME + ": onBackpressureBuffer action: buffer overflow!");
        };

        bufferFlowable  // No error if no onBackpressureBuffer, since flowable uses BackpressureStrategy.BUFFER, but might cause OOM.
            //.onBackpressureBuffer(capacity, action)  // Error if capacity is not enough
            .onBackpressureBuffer(capacity, action, BackpressureOverflowStrategy.DROP_OLDEST)  // No errors or OOM since buffer overflow is handled.
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), false, bufferSize)  // observeOn uses a default 128 buffer size so we overwrite it.
            .subscribe(new FlowableSubscriber<Integer>() {
                @Override
                public void onSubscribe(Subscription s) {
                    Log.d(Constant.TAG, NAME + ": subscription1: onSubscribe: " + System.identityHashCode(s));
                    subscription1 = s;

                    // onNext will not be called if not requested
                    subscription1.request(100);
                }

                @Override
                public void onNext(Integer integer) {
                    Log.d(Constant.TAG, NAME + ": subscription1: onNext: " + integer);

                    SystemClock.sleep(800);  // Slow down subscriber (consumer).

                    // More onNext will not be called if more is not requested
                    subscription1.request(20);
                }

                @Override
                public void onError(Throwable t) {
                    Log.e(Constant.TAG, NAME + ": subscription1: onError", t);
                }

                @Override
                public void onComplete() {
                    Log.d(Constant.TAG, NAME + ": subscription1: onComplete");
                }
            });

        bufferFlowable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new FlowableSubscriber<Integer>() {
                @Override
                public void onSubscribe(Subscription s) {
                    Log.d(Constant.TAG, NAME + ": subscription2: onSubscribe: " + System.identityHashCode(s));
                    subscription2 = s;
                    subscription2.request(100);
                }

                @Override
                public void onNext(Integer integer) {
                    Log.d(Constant.TAG, NAME + ": subscription2: onNext: " + integer);
                    subscription2.request(20);
                }

                @Override
                public void onError(Throwable t) {
                    Log.e(Constant.TAG, NAME + ": subscription2: onError", t);
                }

                @Override
                public void onComplete() {
                    Log.d(Constant.TAG, NAME + ": subscription2: onComplete");
                }
            });

        // With ResourceSubscriber no need to request for the next items.
        /*bufferFlowable
            .onBackpressureBuffer(capacity, action)  // Error if buffer-capacity overflows.
            //.onBackpressureBuffer(1, action, BackpressureOverflowStrategy.DROP_OLDEST)  // No errors since buffer overflow is handled.
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), false, bufferSize)  // observeOn uses a default 128 buffer size so we overwrite it.
            .subscribe(new ResourceSubscriber<Integer>() {
                @Override
                public void onNext(Integer integer) {
                    Log.d(Constant.TAG, NAME + ": subscription3: onNext: " + integer);
                    SystemClock.sleep(800);
                }

                @Override
                public void onError(Throwable t) {
                    Log.e(Constant.TAG, NAME + ": subscription3: onError", t);
                }

                @Override
                public void onComplete() {
                    Log.d(Constant.TAG, NAME + ": subscription3: onComplete");
                }
            });*/
    }

    private void unsubscribe() {
        if (subscription1 != null) {
            subscription1.cancel();
            Log.d(Constant.TAG, NAME + ": cancelled subscription1");
        }
        if (subscription2 != null) {
            subscription2.cancel();
            Log.d(Constant.TAG, NAME + ": cancelled subscription2");
        }
    }
}
