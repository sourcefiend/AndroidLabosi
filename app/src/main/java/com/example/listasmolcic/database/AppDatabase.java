package com.example.listasmolcic.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.listasmolcic.model.Car;

@Database(entities = {Car.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CarDao carDao();
}
