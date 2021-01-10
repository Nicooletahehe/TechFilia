package com.example.proiectdam.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proiectdam.database.model.User;

import java.util.List;

//interfata pentru fiecare tabela
@Dao
public interface UserDao {
    @Query("select * from users")
    List<User> getAll();

    @Insert
    long insert(User user);
    //daca returneaza -1, nu a functionat, eroare sql

    @Update
    int update(User user);
    //int = numarul de randuri afectate
    //ar trebui sa returneze tot timpul 1
    //-1 daca sunt probleme

    @Delete
    int delete(User user);
}
