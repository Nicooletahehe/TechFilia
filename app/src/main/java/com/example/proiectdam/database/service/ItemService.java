package com.example.proiectdam.database.service;

import android.content.Context;
import android.util.Log;

import com.example.proiectdam.asyncTask.AsyncTaskRunner;
import com.example.proiectdam.asyncTask.Callback;
import com.example.proiectdam.database.DataBaseManager;
import com.example.proiectdam.database.dao.ItemDao;
import com.example.proiectdam.database.model.Item;

import java.util.List;
import java.util.concurrent.Callable;

public class ItemService {
    private final ItemDao itemDao;
    private final AsyncTaskRunner taskRunner;

    public ItemService(Context context) {
        itemDao = DataBaseManager.getInstance(context).getItemDao();
        taskRunner = new AsyncTaskRunner();
    }

    public void getAll(Callback<List<Item>> callback){
        Callable<List<Item>> callable = new Callable<List<Item>>() {
            @Override
            public List<Item> call() {
                return itemDao.getAll();
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

//    public void getAllByCountry(final String country, Callback<List<Item>> callback){
//        Callable<List<Item>> callable = new Callable<List<Item>>() {
//            @Override
//            public List<Item> call() throws Exception {
//                if(country ==  null || country.trim().isEmpty()){
//                    return null;
//                }
//                return itemDao.getAllByCountry(country);
//            }
//        };
//        taskRunner.executeAsync(callable,callback);
//    }

    public void insert(Callback<Item> callback, final Item item){
        Callable<Item> callable = new Callable<Item>() {
            @Override
            public Item call() {
                Log.i("insertservice", String.valueOf(item));
                if(item == null) {
                    return null;
                }
                Log.i("insertservice", String.valueOf(item.getId()));
                Log.i("itemdao", String.valueOf(itemDao.getAll()));
                long id = itemDao.insert(item);
                Log.i("insertservice", String.valueOf(id));
                if(id == -1){
                    return null;
                }
                item.setId(id);
                Log.i("insertservice", String.valueOf(item));
                return item;
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void update(Callback<Item> callback, final Item item) {
        Callable<Item> callable = new Callable<Item>() {
            @Override
            public Item call() {
                if (item == null) {
                    return null;
                }
                int count = itemDao.update(item);
                if (count < 1) {
                    return null;
                }
                return item;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void delete(Callback<Integer> callback, final Item item) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() {
                if (item == null) {
                    return -1;
                }
                return itemDao.delete(item);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }
}
