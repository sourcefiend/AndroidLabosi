package com.example.listasmolcic.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.listasmolcic.BatteryLowReceiver;
import com.example.listasmolcic.R;
import com.example.listasmolcic.RecyclerAdapter;
import com.example.listasmolcic.ServiceGenerator;
import com.example.listasmolcic.database.AppDatabase;
import com.example.listasmolcic.database.CarDao;
import com.example.listasmolcic.model.Car;
import com.example.listasmolcic.presenter.RetrofitService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Car> carList;
    private RecyclerView recyclerView;

    private MediaPlayer mediaPlayer;

    public static String NOTIFICATION_CHANNEL = "notificationChannel";
    private static final String API_URL = "https://10.0.2.2:44447/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        carList = new ArrayList<>();

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.elevator_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        /* RetrofitService client =
                ServiceGenerator.getRetrofit().create(RetrofitService.class);

        Call<List<Car>> cars = client.dohvatiAute();

        cars.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful()) {
                    carList = (ArrayList<Car>) response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        }); */

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "car").allowMainThreadQueries().createFromAsset("database/car.db").build();

        CarDao carDao = db.carDao();

        carList = (ArrayList<Car>) carDao.getAll();

        setAdapter();

        IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_LOW");
        BatteryLowReceiver batteryLowReceiver = new BatteryLowReceiver();
        registerReceiver(batteryLowReceiver, intentFilter);

        // Create notification channel and check if API is 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, "My Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("My main notification channel.");
            NotificationManager notificationManager = (NotificationManager) (getSystemService(Context.NOTIFICATION_SERVICE));
            notificationManager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Main activity", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get the new Instance ID token
                        String token = task.getResult();

                        Log.d("Main activity", token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(carList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("Car item", carList.get(position));

                startActivity(intent);
            }
        });
    }

    private void setCarInfo() {
        carList.add(new Car(R.drawable.audi, "Audi", "Audi description"));
        carList.add(new Car(R.drawable.bmw, "BMW", "Bmw description"));
        carList.add(new Car(R.drawable.porsche, "Porsche", "Porsche description"));
        carList.add(new Car(R.drawable.opel, "Opel", "Opel description"));
        carList.add(new Car(R.drawable.alfa_romeo, "Alfa Romeo", "Alfa Romeo description"));
        carList.add(new Car(R.drawable.ferrari, "Ferrari", "Ferrari description"));
        carList.add(new Car(R.drawable.mclaren, "McLaren", "McLaren description"));
        carList.add(new Car(R.drawable.mercedes, "Mercedes", "Mercedes description"));
        carList.add(new Car(R.drawable.volvo, "Volvo", "Volvo description"));
    }

    public void openCarMarketplace(View view) {
        String url = "https://www.njuskalo.hr/auto-moto";
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(i);
        } catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(), "Nije moguće pokrenuti aktivnost.", Toast.LENGTH_LONG);
        }
    }
}