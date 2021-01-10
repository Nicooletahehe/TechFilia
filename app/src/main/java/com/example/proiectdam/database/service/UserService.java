package com.example.proiectdam.database.service;

import android.content.Context;
import android.util.Log;

import com.example.proiectdam.asyncTask.AsyncTaskRunner;
import com.example.proiectdam.asyncTask.Callback;
import com.example.proiectdam.database.DataBaseManager;
import com.example.proiectdam.database.dao.UserDao;
import com.example.proiectdam.database.model.User;

import java.util.List;
import java.util.concurrent.Callable;

public class UserService {
    private final UserDao userDao;
    private final AsyncTaskRunner taskRunner;

    public UserService(Context context) {
        userDao = DataBaseManager.getInstance(context).getUserDao();
        taskRunner = new AsyncTaskRunner();
    }

    public void getAll(Callback<List<User>> callback){
        Callable<List<User>> callable = new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                return userDao.getAll();
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void insert(Callback<User> callback, final User user){
        Callable<User> callable = new Callable<User>() {
            @Override
            public User call() {
                Log.i("insertservice", String.valueOf(user));
                if(user == null) {
                    return null;
                }
                Log.i("insertservice", String.valueOf(user.getId()));
                Log.i("itemdao", String.valueOf(userDao.getAll()));
                long id = userDao.insert(user);
                Log.i("insertservice", String.valueOf(id));
                if(id == -1){
                    return null;
                }
                user.setId(id);
                Log.i("insertservice", String.valueOf(user));
                return user;
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void update(Callback<User> callback, final User user) {
        Callable<User> callable = new Callable<User>() {
            @Override
            public User call() {
                if (user == null) {
                    return null;
                }
                int count = userDao.update(user);
                if (count < 1) {
                    return null;
                }
                return user;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void delete(Callback<Integer> callback, final User user) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() {
                if (user == null) {
                    return -1;
                }
                return userDao.delete(user);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }
}
