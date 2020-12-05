package com.example.rentalkamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class AddbarangActivity extends AppCompatActivity implements IPickResult {
    private ImageView ivKamera;
    private Bitmap mSelectedImage;
    private String mSelectedImagePath;
    File mSelectedFileBanner;
    private EditText etKode, etMerk, etHarga, etWarna;
    private Button btnBuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbarang);
        ivKamera = findViewById(R.id.ivKamera);
        etKode = findViewById(R.id.etKode);
        etMerk = findViewById(R.id.etMerk);
        etWarna = findViewById(R.id.etWarna);
        etHarga = findViewById(R.id.etHarga);

        btnBuat = findViewById(R.id.btnBuat);
        ivKamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).show(AddbarangActivity.this);
                new PickSetup().setCameraButtonText("Gallery");
            }
        });
        btnBuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
    }

    @Override
    public void onPickResult(PickResult r) {
        if(r.getError() == null){
            try {
                File fileku = new Compressor(this)
                        .setQuality(50)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(new File(r.getPath()));
                mSelectedImagePath = fileku.getAbsolutePath();
                mSelectedFileBanner = new File(mSelectedImagePath);
                mSelectedImage=r.getBitmap();
                ivKamera.setImageBitmap(mSelectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(AddbarangActivity.this, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void post() {
        final String kode = etKode.getText().toString();
        final String merk = etMerk.getText().toString();
        final String warna = etWarna.getText().toString();
        final String hargasewa = etHarga.getText().toString();
        HashMap<String, String> body = new HashMap<>();
        body.put("kodekamera", kode);
        body.put("merkkamera", merk);
        body.put("warnakamera", warna);
        body.put("hargasewa", hargasewa);
        AndroidNetworking.upload("http://192.168.6.93/RentalKamera/insertdatakamera.php")
                .addMultipartFile("gambarkameraa",mSelectedFileBanner)
                .addMultipartParameter(body)
                .setOkHttpClient(((RS) getApplication()).getOkHttpClient())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent i = new Intent(AddbarangActivity.this,ViewbarangActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(AddbarangActivity.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d("HBB", "onError: " + anError.getErrorBody());
                        Log.d("HBB", "onError: " + anError.getLocalizedMessage());
                        Log.d("HBB", "onError: " + anError.getErrorDetail());
                        Log.d("HBB", "onError: " + anError.getResponse());
                        Log.d("HBB", "onError: " + anError.getErrorCode());
                    }

});

    }
}
