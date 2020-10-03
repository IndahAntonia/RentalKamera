package com.example.rentalkamera;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister;

    private ProgressDialog progressBar;
    SharedPreferences mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = new ProgressDialog(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);


        mLogin = getSharedPreferences("login",MODE_PRIVATE);
        mLogin.edit().putString("logged", mLogin.getString("logged", "missing")).apply();

        String admin = mLogin.getString("logged", "missing");
        String costomer = mLogin.getString("logged", "missing");

        if(costomer.equals("customer")){
            Intent intent = new Intent(MainActivity.this, Costumeractivity.class);
            startActivity(intent);
            finish();
        }else if (admin.equals("admin")){
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                progressBar.setTitle("Logging In...");
                progressBar.show();
                AndroidNetworking.post("http://192.168.137.1/RentalKamera/LoginCustomer.php")
                        .addBodyParameter("email" , email)
                        .addBodyParameter("password" , password)
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("hasil", "onResponse: " + response);
                                try {
                                    JSONObject PAYLOAD = response.optJSONObject("PAYLOAD");
                                    String respon = PAYLOAD.getString("respon");
                                    String roleuser = PAYLOAD.getString("roleuser");
                                    Log.d("PAYLOAD", "onResponse: " + respon);
                                    if (respon.equals("true")) {
                                        Log.d("PAYLOAD", "onResponse: " + respon);
                                        if (roleuser.equals("2")) {
                                            mLogin.edit().putString("logged", "admin").apply();
                                            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                            startActivity(intent);
                                            finish();
                                            progressBar.dismiss();
                                        } else if (roleuser.equals("1")) {
                                            mLogin.edit().putString("logged", "customer").apply();
                                            Intent intent = new Intent(MainActivity.this, Costumeractivity.class);
                                            startActivity(intent);
                                            finish();
                                            progressBar.dismiss();
                                        } else {
                                            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                            progressBar.dismiss();
                                        }
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this,"error",Toast.LENGTH_SHORT).show();
                                    }
                                } catch ( JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                progressBar.dismiss();
                                Log.i("test", "onError: " + anError.getErrorDetail());
                                Log.i("test", "onError: " + anError.getErrorBody());
                                Log.i("test", "onError: " + anError.getErrorCode());
                            }
                        });

            }

        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrasiActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);                    }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
