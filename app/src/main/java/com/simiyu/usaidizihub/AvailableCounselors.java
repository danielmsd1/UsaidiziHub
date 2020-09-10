package com.simiyu.usaidizihub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simiyu.usaidizihub.models.User;

import static java.net.Proxy.Type.HTTP;

public class AvailableCounselors extends AppCompatActivity {

    private ImageView imageView;
    private ImageButton videoCall,emailCommunication,phoneCall;
    private int mUserSecurityLevel;
    private static final String TAG = "AvailableCounselors";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_counselors);
        /**
         * Collect a user from database whose security level is 2
         * security level == 2 == counselor
         */
        getUserSecurityLevel();

        if (mUserSecurityLevel==2) {
            Toast.makeText(this, "User is a counselor!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "User not a counselor!", Toast.LENGTH_SHORT).show();
        }

    }

    private void getUserSecurityLevel(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child(getString(R.string.dbnode_users))
                .orderByKey()
                //OR could use ->.orderByChild(getString(R.string.field_user_id))
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot:  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: users security level: "
                            + singleSnapshot.getValue(User.class).getSecurity_level());

                    mUserSecurityLevel = Integer.parseInt(String.valueOf(
                            singleSnapshot.getValue(User.class).getSecurity_level()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void openEmail(View view) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String mailaddress = user.getEmail();
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType(HTTP.toString());
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {mailaddress}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Session student");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello sir,");
    }

    public void openPhone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0715598801"));
        startActivity(intent);
    }
}