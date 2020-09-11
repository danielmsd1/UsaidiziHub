package com.simiyu.usaidizihub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.simiyu.usaidizihub.models.User;

public class UploadDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String TAG = "UploadDataActivity";

    private int PICKFILE_RESULT_CODE = 12334;

    //widgets
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        //populate the spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.areasofcounseling, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mProgressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // An item was selected. You can retrieve the selected item using
        //parent.getItemAtPosition(pos)
        if (adapterView.getOnItemSelectedListener()!= null){
            Toast.makeText(this, "Selected Category: "+adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "onItemSelected: An item has been selected!");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Another interface callback
    }
    /**
     * Progress bar implementations
     * */
    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     *Allow Counselor to upload files
     * @param view
     */
    public void selectFiles(View view) {
        uploadFile();
    }
    public void uploadFile()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);

        Toast.makeText(this, "Enter file to upload", Toast.LENGTH_SHORT).show();
    }
}