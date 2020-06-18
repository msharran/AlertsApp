package com.eqsis.notifications.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.eqsis.notifications.API.SingleAlertApiRequest;
import com.eqsis.notifications.R;
import com.eqsis.notifications.Utils.MyAdapter;



public class AlertActivity extends AppCompatActivity {

    private SingleAlertApiRequest alertApi;

    public static AlertActivity Instance;
    private View linlaHeaderProgressAlert;
    private Toolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        Instance = AlertActivity.this;

        toolbar = findViewById(R.id.Toolbar_Alert);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        linlaHeaderProgressAlert=findViewById(R.id.alert_progressbar);
        initView();
    }

    private void initView() {




        //setting up recycler and card view in background after fetching data from api

       
        alertApi = new SingleAlertApiRequest();
        alertApi.execute();




    }

    public void showMainProgress(final boolean show) {
        linlaHeaderProgressAlert.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
