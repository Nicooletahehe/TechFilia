package com.example.proiectdam.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proiectdam.database.model.Item;

import java.util.List;

//interfata pentru fiecare tabela
@Dao
public interface ItemDao {
    @Query("select * from items")
    List<Item> getAll();

//    @Query("select * from items where country=:country")
//    List<Item> getAllByCountry(String country);

    @Insert
    long insert(Item item);
    //daca returneaza -1, nu a functionat, eroare sql

    @Update
    int update(Item item);
    //int = numarul de randuri afectate
    //ar trebui sa returneze tot timpul 1
    //-1 daca sunt probleme

    @Delete
    int delete(Item item);
}
