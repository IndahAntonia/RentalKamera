package com.example.rentalkamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class  EditbarangActivity extends AppCompatActivity {
    TextView tvId, tvKode, tvMerk, tvWarna, tvHarga;
    Button btnedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoactivity);
        tvId = findViewById(R.id.tvId);
        tvKode = findViewById(R.id.tvNama);
        tvMerk = findViewById(R.id.tvEmail);
        tvWarna = findViewById(R.id.tvNohp);
        tvHarga = findViewById(R.id.tvAlamat);
        btnedit = findViewById(R.id.btnedit);


        //Toolbar toolbar = findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Info Customer");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        final String id = extras.getString("id");
        final String kode = extras.getString("kode");
        final String merk = extras.getString("merk");
        final String warna = extras.getString("warna");
        final String harga = extras.getString("harga");


        tvId.setText("Id           : " + id);
        tvKode.setText("Kode      : " + kode);
        tvMerk.setText("Merk      : " + merk);
        tvWarna.setText("Warna     : " + warna);
        tvHarga.setText("Harga    : " + harga);


        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(EditbarangActivity.this,AddbarangActivity.class);
                in.putExtra("id", id);
                in.putExtra("kode", kode);
                in.putExtra("merk", merk);
                in.putExtra("warna", warna);
                in.putExtra("harga", harga);
                startActivityForResult(in, 23);
            }
        });
    }
}