package com.eqsis.notifications.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.eqsis.notifications.API.FetchData;
import com.eqsis.notifications.R;

public class FilteredActivity extends AppCompatActivity {

    private Toolbar toolbar;

    public static FilteredActivity Instance;
    private View mProgressView;
    private FetchData fetchData;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered);
        init();
        main();
    }

    private void main() {

        Object[] object=new Object[3];
        object[0]="https://eqsisalert.herokuapp.com/api/v1/filter/alerts";

        object[2]=this;
        fetchData = new FetchData("filter");
        fetchData.execute(object);
    }

    private void init() {
        //Setting Custom Actionbar
        mProgressView=findViewById(R.id.Filter_Progress);
        toolbar = findViewById(R.id.Toolbar_Filter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Instance = FilteredActivity.this;


    }

    public  void showProgress(final boolean show) {

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);


    }
}
