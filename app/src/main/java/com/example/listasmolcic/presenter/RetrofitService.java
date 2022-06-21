package com.example.listasmolcic.presenter;

import com.example.listasmolcic.model.Car;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {

    @GET("Car")
    Call<List<Car>> dohvatiAute();
}
