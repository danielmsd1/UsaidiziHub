package com.simiyu.usaidizihub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Policy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
    }

    public void uploaddata(View view) {
        Intent intent = new Intent(this,UploadDataActivity.class);
        startActivity(intent);
    }
}