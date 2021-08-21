package com.ashwin.rxjavasandbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ashwin.rxjavasandbox.databinding.ActivityMainBinding;
import com.ashwin.rxjavasandbox.flowable.buffer.BufferFlowableActivity;
import com.ashwin.rxjavasandbox.operator.debounce.DebounceActivity;
import com.ashwin.rxjavasandbox.operator.filter.FilterActivity;
import com.ashwin.rxjavasandbox.operator.reduce.ReduceActivity;
import com.ashwin.rxjavasandbox.operator.repeat.RepeatActivity;
import com.ashwin.rxjavasandbox.operator.scan.ScanActivity;
import com.ashwin.rxjavasandbox.subject.async.AsyncSubjectActivity;
import com.ashwin.rxjavasandbox.subject.behavior.BehaviorSubjectActivity;
import com.ashwin.rxjavasandbox.subject.publish.PublishSubjectActivity;
import com.ashwin.rxjavasandbox.subject.replay.ReplaySubjectActivity;
import com.ashwin.rxjavasandbox.subject.single.SingleSubjectActivity;
import com.ashwin.rxjavasandbox.subject.unicast.UnicastSubjectActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.filterButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FilterActivity.class));
        });

        binding.repeatButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RepeatActivity.class));
        });

        binding.scanButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ScanActivity.class));
        });

        binding.reduceButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ReduceActivity.class));
        });

        binding.debounceButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, DebounceActivity.class));
        });

        binding.flowableButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BufferFlowableActivity.class));
        });

        binding.subjectPublishButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PublishSubjectActivity.class));
        });

        binding.subjectUnicastButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, UnicastSubjectActivity.class));
        });

        binding.subjectBehaviorButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BehaviorSubjectActivity.class));
        });

        binding.subjectAsyncButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AsyncSubjectActivity.class));
        });

        binding.subjectReplayButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ReplaySubjectActivity.class));
        });

        binding.subjectSingleButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SingleSubjectActivity.class));
        });
    }
}
