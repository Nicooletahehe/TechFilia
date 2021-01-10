package com.example.proiectdam.asyncTask;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AsyncTaskRunner {

    //determina cate threaduri putem rula in paralel
    private final Executor executor = Executors.newCachedThreadPool();
    //va veghea asupra threadului principal
    private final Handler handler = new Handler(Looper.getMainLooper());

    public <R> void executeAsync(Callable<R> asyncOperation, Callback<R> mainThreadOperation){
        try {
            executor.execute(new RunnableTask<>(handler, asyncOperation, mainThreadOperation));
        } catch (Exception e) {
            Log.i("AsyncTaskRunner", "Failed Call Execute Async " + e.getMessage());
        }

    }
}
