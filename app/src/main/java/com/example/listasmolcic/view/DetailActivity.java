package com.example.listasmolcic.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listasmolcic.R;
import com.example.listasmolcic.model.Car;

public class DetailActivity extends AppCompatActivity {

    private Car carItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        carItem = intent.getParcelableExtra("Car item");

        int imageRes = carItem.getImageResource();
        String name = carItem.getName();
        String description = carItem.getDescription();

        ImageView imageView = findViewById(R.id.car_image);
        imageView.setImageResource(imageRes);

        TextView carName = findViewById(R.id.car_name_detail);
        carName.setText(name);

        TextView carDesc = findViewById(R.id.car_desc);
        carDesc.setText(description);
    }

    public void startAlert(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Podijeli ime i opis automobila kroz broadcast?")
                .setPositiveButton("Da", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent();
                        i.setAction("send_custom_broadcast");
                        i.putExtra("Car item", carItem);
                        sendBroadcast(i);
                        Toast.makeText(getApplicationContext(), "Podaci u automobilu uspješno broadcastani.",Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Odustali ste.",Toast.LENGTH_LONG).show();
                    }
                })
                .create()
                .show();
    }
}