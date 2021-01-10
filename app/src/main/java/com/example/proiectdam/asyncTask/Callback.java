package com.example.proiectdam.asyncTask;

public interface Callback<R> {
    void runResultOnUiThread(R result);
}
