package com.simiyu.usaidizihub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.simiyu.usaidizihub.models.User;

public class Registration extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final String DOMAIN_NAME = "strathmore.edu";

    //widgets
    private EditText mEmail, mPassword, mConfirmPassword;
    private Button mRegister;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mEmail = findViewById(R.id.emailAddress);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.cpassword);
        mRegister = findViewById(R.id.register);
        mProgressBar =  findViewById(R.id.progressBar);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attempting to register.");

                //check for null valued EditText fields
                if(!isEmpty(mEmail.getText().toString())
                        && !isEmpty(mPassword.getText().toString())
                        && !isEmpty(mConfirmPassword.getText().toString())){

                    //check if user has a student email address
                    if(isValidDomain(mEmail.getText().toString())){

                        //check if passwords match
                        if(doStringsMatch(mPassword.getText().toString(), mConfirmPassword.getText().toString())){

                            registerNewEmail(mEmail.getText().toString(),mPassword.getText().toString());

                        }else{
                            Toast.makeText(Registration.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Registration.this, "Please Register with student Email", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Registration.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hideSoftKeyboard();

    }

    /***
     * Send verification email
     * */
    private void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Registration.this, "Sent Verification Email", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Registration.this, "Could not send Verification email", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    /**
     * Register new email
     * */
    private void registerNewEmail(final String email, String password){
        showDialog();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: onComplete: " +task.isSuccessful());

                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: AuthState: "+FirebaseAuth.getInstance().getCurrentUser()
                            .getUid());

                            sendVerificationEmail();

                            User user = new User();
                            user.setName(email.substring(0,email.indexOf("@")));
                            user.setPhone("1");
                            user.setProfile_image("");
                            user.setSecurity_level("1");
                            user.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            FirebaseDatabase.getInstance().getReference()
                                    .child(getString(R.string.dbnode_users))
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseAuth.getInstance().signOut();
                                    // TODO: 9/5/2020 Redirect user to login screen
                                    //redirecttoLoginScreen();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    FirebaseAuth.getInstance().signOut();
                                    // TODO: 9/5/2020 Redirect user to login screen
                                    //redirecttoLoginScreen();
                                    Toast.makeText(Registration.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });

//                            FirebaseAuth.getInstance().signOut();
//                            // TODO: 9/5/2020 Redirect user to login screen
//                            //redirecttoLoginScreen();
                        }else{
                            Toast.makeText(Registration.this, "Unable to register", Toast.LENGTH_SHORT).show();
                        }
                        hideDialog();
                    }
                }
        );
    }

    /**
     * Returns True if the user's email contains '@strathmore.edu'
     * @param email
     * @return
     */
    private boolean isValidDomain(String email){
        Log.d(TAG, "isValidDomain: verifying email has correct domain: " + email);
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        Log.d(TAG, "isValidDomain: users domain: " + domain);
        return domain.equals(DOMAIN_NAME);
    }

    /**
     * Return true if @param 's1' matches @param 's2'
     * @param s1
     * @param s2
     * @return
     */
    private boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
    }

    /**
     * Return true if the @param is null
     * @param string
     * @return
     */
    private boolean isEmpty(String string){
        return string.equals("");
    }


    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    /**
     * Redirects the user to the login screen
     */
    public void redirecttoLoginScreen(View view) {
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(Registration.this, Login.class);
        startActivity(intent);
        finish();
    }
}