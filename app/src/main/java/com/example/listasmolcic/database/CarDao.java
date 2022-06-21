package com.example.listasmolcic.database;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.listasmolcic.model.Car;

import java.util.List;

@Dao
public interface CarDao {

    @Query("SELECT * FROM car")
    List<Car> getAll();
}
