package com.example.proiectdam.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.proiectdam.database.dao.ItemDao;
//import com.example.proiectdam.database.dao.UserDao;
import com.example.proiectdam.database.dao.UserDao;
import com.example.proiectdam.database.model.Item;
import com.example.proiectdam.database.model.User;
//import com.example.proiectdam.database.model.User;

//orice clasa marcata cu clasa entity, va genera scriptul pentru generateTable
//exportschema => true, poti exporta pe telefon
//in functie de version, daca crestere, se va crea migrarea; daca se modifica baza de date
@Database(entities = {Item.class, User.class}, exportSchema = false, version = 1)
public abstract class DataBaseManager extends RoomDatabase {

    public static final String FILATELIA_DB = "filatelia_db";
    //o singura conexiune, nu permitem sa cream obiecte multiple
    //design pattern = o instanta/conexiune unica a unui obiect = SINGLETON = mecanism in jaava
    //conexiuni multiple la o baza de date poate duce la o desincronizare a datelor
    //pentru asta facem constructorul privat, dar daca avem clasa abstracta, implicit nu se vor putea crea noi instante
    private static DataBaseManager dataBaseManager;

    //creare instanta unica
    public static DataBaseManager getInstance(Context context){
        //acest if nu permite reinitializarea dubla
        if(dataBaseManager == null) {
            //ne asiguram ca  synchronized va decide ce se va trimite
            //nu lasa procesarea in paralel
            synchronized (DataBaseManager.class) {
                if(dataBaseManager == null) {
                    //fallbackToDestructiveMigration => ne asigura, daca nu se va reusi migrarea, va sterge inregistrarile
                    dataBaseManager = Room.databaseBuilder(context, DataBaseManager.class, FILATELIA_DB)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return dataBaseManager;
    }

    //pentru DML => initializare Dao
    public abstract ItemDao getItemDao();
    public abstract UserDao getUserDao();

}
