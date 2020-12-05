package com.example.rentalkamera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewbarangActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapterbarang adapter;
    private ImageView ivAdd;
    ArrayList<ModelMasterbarang> datalist;
    CardView cardview;
    TextView txtmerkkamera, txtwarnakamera,txthargasewa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewbarang);

        txtmerkkamera = findViewById(R.id.txtmerk);
        txtwarnakamera = findViewById(R.id.txtwarna);
        txthargasewa  = findViewById(R.id.txthargasewa);

        cardview = findViewById(R.id.cardbarangview);
        recyclerView = findViewById(R.id.list);
        ivAdd = findViewById(R.id.ivAdd);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewbarangActivity.this,AddbarangActivity.class);
                startActivity(i);
            }
        });

        //Toolbar toolbar = findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Data Customer");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getData();

    }

    private void getData() {
        datalist = new ArrayList<>();
        Log.i("daa", "onCreate: ");
                 AndroidNetworking.get("http://192.168.6.93/RentalKamera/viewdatakamera.php")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ViewbarangActivity.this, "STATUS 200 OK...", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onResponse: "+response);
                        try {
                            JSONArray data = response.getJSONArray("result");
                            for (int i = 0; i < data.length(); i++) {
                               // Toast.makeText(ViewbarangActivity.this, "STATUS 200 OK...", Toast.LENGTH_SHORT).show();
                                ModelMasterbarang modelku = new ModelMasterbarang();
                                JSONObject object = data.getJSONObject(i);
                                modelku.setId(object.getString("id"));
                                modelku.setKodekamera(object.getString("kodekamera"));
                                modelku.setMerkkamera(object.getString("merkkamera"));
                                modelku.setWarnakamera(object.getString("warnakamera"));
                                modelku.setHargasewa(object.getString("hargasewa"));
                                modelku.setGambarkamera(object.getString("gambarkameraa"));
                                datalist.add(modelku);
                            }

                            adapter = new Adapterbarang(datalist);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ViewbarangActivity.this);

                            recyclerView.setLayoutManager(layoutManager);

                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.i("daa", "onResponse: " + anError.toString());
                        Log.i("daa", "onResponse: " + anError.getErrorBody());
                        Log.i("daa", "onResponse: " + anError.getErrorCode());
                        Log.i("daa", "onResponse: " + anError.getErrorDetail());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 23 && data.getStringExtra("refresh") != null) {
            //refresh list
            getData();
            Toast.makeText(this, "iiin", Toast.LENGTH_SHORT).show();

        }
    }

}