package com.eqsis.notifications.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eqsis.notifications.API.FetchData;

import com.eqsis.notifications.API.ProfileApiRequest;
import com.eqsis.notifications.API.SignOutApiRequest;
import com.eqsis.notifications.R;
import com.eqsis.notifications.Utils.ProfileItem;


public class ProfileActivity extends AppCompatActivity{


    private Button signOut;
    private FetchData fetchData;
    public static TextView name,dob,gender,status,maritalStatus,email,phone;
    public static ProfileActivity Instance;
    private LinearLayout linearlayout;
    private Toolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        init();
        fetchingProfileData("https://eqsisalert.herokuapp.com/api/v1/current_user/profile");

        signOut();
    }

    private void fetchingProfileData(String s) {

        ProfileApiRequest apiRequest=new ProfileApiRequest();
        apiRequest.execute(s);

    }

    private void signOut() {
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                SignOutApiRequest signOutApiRequest=  new SignOutApiRequest();
                signOutApiRequest.execute();
                Intent i =new Intent(ProfileActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }



    private void init() {


        toolbar = findViewById(R.id.Toolbar_Profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Instance=ProfileActivity.this;
        linearlayout=findViewById(R.id.profile_progress_LL);
        signOut=findViewById(R.id.profile_btnSignOut);
        name=findViewById(R.id.profile_name);
        dob=findViewById(R.id.profile_dob);
        gender=findViewById(R.id.profile_gender);
        status=findViewById(R.id.profile_status);
        maritalStatus=findViewById(R.id.profile_maritalStatus);
        email=findViewById(R.id.profile_mail);
        phone=findViewById(R.id.profile_phone);


    }


    public void showMainProgress(boolean show) {
        linearlayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
