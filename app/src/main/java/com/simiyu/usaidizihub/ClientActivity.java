package com.simiyu.usaidizihub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import io.kommunicate.KmConversationBuilder;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KmCallback;

public class ClientActivity extends AppCompatActivity {

    private static final String TAG = "SignedInActivity";
    //kommunicate bot app id
    final String APP_ID ="363f0d3ab44dd1a0538e05cb5e825fd3d";

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;

    //widgets
    private ProgressBar mProgressBar;
    private FloatingActionButton ftext,fprofile,fmenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        //initialize views
        mProgressBar =  findViewById(R.id.progressBar2);
        ftext = findViewById(R.id.fabchatbot);
        fprofile = findViewById(R.id.fabprofile);
        fmenu = findViewById(R.id.fabmore);

        //Initialization of Kommunicate SDK
        Kommunicate.init(this,APP_ID);

        setupFirebaseAuth();

        //getUserDetails();
        setUserDetails();
    }
    /***
     * Set user details
     * */
    // TODO: 9/5/2020 setDisplayName dynamically
    
    private void setUserDetails(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName("Simiyu Daniel")
                    .setPhotoUri(Uri.parse("https://www.google.com/imgres?imgurl=https%3A%2F%2Fwww.usnews.com%2Fdims4%2FUSNEWS%2Ff45ea7c%2F2147483647%2Fthumbnail%2F640x420%2Fquality%2F85%2F%3Furl%3Dhttp%253A%252F%252Fmedia.beam.usnews.com%252Fd1%252Fd8%252F8501ba714a21aed9a7327e02ade1%252F180515-10thingselonmusk-editorial.jpg&imgrefurl=https%3A%2F%2Fwww.usnews.com%2Fnews%2Fnational-news%2Farticles%2F2018-05-24%2F10-things-you-didnt-know-about-elon-musk&tbnid=aFJaMH5wTn0DbM&vet=12ahUKEwig5L6R_tDrAhVJw4UKHWyNBhoQMygWegUIARD7AQ..i&docid=0fZr_NLvm8byIM&w=640&h=420&q=elon%20musk&ved=2ahUKEwig5L6R_tDrAhVJw4UKHWyNBhoQMygWegUIARD7AQ"))
                    .build();

            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.d(TAG, "onComplete: Profile updated");

                        getUserDetails();
                    }
                }
            });
        }
    }

    /**
     *Kommunicate bot function
     * todo webhook:
     * **/
    private void kommunicatebot(){
        new KmConversationBuilder(ClientActivity.this)
                .launchConversation(new KmCallback() {
                    @Override
                    public void onSuccess(Object message) {
                        Log.d("Conversation", "Success : " + message);
                        hideDialog();
                    }

                    @Override
                    public void onFailure(Object error) {
                        Log.d("Conversation", "Failure : " + error);
                        hideDialog();
                    }
                });
    }

    /***
     * Get user details
     * */
    private void getUserDetails(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            String uid = user.getUid();
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUri = user.getPhotoUrl();

            String properties = "uid: "+uid + "\n" +
                    "name: "+name + "\n" +
                    "email: "+email + "\n" +
                    "photouri: "+photoUri + "\n" ;

            Log.d(TAG, "getUserDetails: Properties: "+properties);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    private void checkAuthenticationState(){
        Log.d(TAG, "checkAuthenticationState: checking authentication state.");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Log.d(TAG, "checkAuthenticationState: user is null, navigating back to login screen.");

            Intent intent = new Intent(ClientActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Log.d(TAG, "checkAuthenticationState: user is authenticated.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.optionSignOut:
                signOut();
                return true;
            case R.id.settings:
                openSettings();
                return true;
            case R.id.policy:
                openLicence();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sign out the current user
     */
    private void signOut(){
        Log.d(TAG, "signOut: signing out");
        FirebaseAuth.getInstance().signOut();
    }

    /**
     * Open the application settings
     */
    private void openSettings(){
        Intent intent = new Intent(this,ApplicationSettings.class);
        startActivity(intent);
    }

    /**
     * Open MIT open source licence
     * Counselor application link available here
     */
    private void openLicence(){
        Intent intent = new Intent(this,Policy.class);
        startActivity(intent);
    }
    /*
            ----------------------------- Firebase setup ---------------------------------
         */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(ClientActivity.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * manage fabs display
     * */
    public void fabMore(View view) {
        showFabs();
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
     * Execute bot onClick function
     * */
    public void openChatbot(View view) {
        showDialog();
        try {
            kommunicatebot();
            //Collapse the menu
            hideFabs();
        }
        catch (Exception e){
            Toast.makeText(this, "Error in displaying chatbot", Toast.LENGTH_SHORT).show();
            hideDialog();
        }
    }
    /**
     * Open my profile
     * **/
    public void openProfile(View view) {
        showDialog();
        try {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            hideDialog();
            //Collapse menu
            hideFabs();
        }
        catch (Exception e){
            hideDialog();
            Log.d(TAG, "openProfile: Could not open profile");
            Toast.makeText(this, "Could not open profile", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Manage fabs
     * Remove from view and make visible
     * */

    private void showFabs(){
        ftext.setVisibility(View.VISIBLE);
        fprofile.setVisibility(View.VISIBLE);
        fmenu.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_baseline_menu_open_24));
//        view.setBackgroundResource(R.drawable.ic_baseline_menu_open_24);
    }
    private void hideFabs(){
        if (ftext.getVisibility() == View.VISIBLE){
            if (fprofile.getVisibility() == View.VISIBLE){
                // TODO: 9/6/2020 Change drawable to menu.open
                fmenu.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_baseline_menu_open_24));
                /**
                 * Invisible and gone: note: Using gone in xml
                 * */
                ftext.setVisibility(View.GONE);
                fprofile.setVisibility(View.GONE);
            }
        }
    }


}