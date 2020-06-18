package com.eqsis.notifications.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.eqsis.notifications.API.FetchData;
import com.eqsis.notifications.R;
import com.eqsis.notifications.Utils.AlertItems;
import com.eqsis.notifications.Utils.MyAdapter;
import com.eqsis.notifications.Utils.StockAdapter;
import com.eqsis.notifications.Utils.StockItems;


import java.util.ArrayList;



public class SettingsActivity extends AppCompatActivity {
    public static String F_b_s,F_i_p,Fstock;
    private  boolean checked=false;
    private FetchData fetchData;
    private ArrayList<StockItems> stockItemsList;
    public static StockAdapter Sadapter;
    private RecyclerView SrecyclerView;
    private Toolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.Toolbar_Settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init checkbox variables
        F_b_s="";
        F_i_p="";
        Fstock="";

        initStock();



    }

    public void initStock() {

        stockItemsList=new ArrayList<>();
        for (AlertItems item : FetchData.alertItemsList) {
            StockItems stockItem=new StockItems(item.getToken().toString(),item.getId().toString());
            stockItemsList.add(stockItem);

        }

        SrecyclerView=(RecyclerView) findViewById(R.id.StockRecyclerView);
        SrecyclerView.setHasFixedSize(true);
        SrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Sadapter=new StockAdapter(stockItemsList,this);
        SrecyclerView.setAdapter(Sadapter);

        EditText searchtxt=findViewById(R.id.searchTxt);
        searchtxt.setSingleLine();
        searchtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<StockItems> filteredList = new ArrayList<>();

        for (StockItems item : stockItemsList) {
            if (item.getStock().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        Sadapter.filterList(filteredList);
    }
    public void onApplyClicked(View v) {

        if(checked)
        {



            Intent intent=new Intent(SettingsActivity.this,FilteredActivity.class);
            finish();
            startActivity(intent);


        }

        if(Sadapter.checkedStocks.size()>0)
        {



            Intent intent=new Intent(SettingsActivity.this,FilteredActivity.class);
            finish();
            startActivity(intent);

        }

        if((!checked)&& (Sadapter.checkedStocks.size()==0))
        {


            //setting up recycler and card view in background after fetching data from api
            Object[] object=new Object[3];
            object[0]="https://eqsisalert.herokuapp.com/api/v1/alerts";

            object[2]=this;
            fetchData = new FetchData("alerts");
            fetchData.execute(object);

            finish();


        }


    }



    public void onCheckboxClicked(View view) {    //overrided method for checkbox on click, refered in xml
        // Is the view now checked?
        checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxPosition:

                F_i_p=checked?"positional":"";

                break;
            case R.id.checkBoxIntraday:


                F_i_p = checked?"Intraday":"";

                break;
            case R.id.checkBoxBuy:


                F_b_s=checked?"bullish":"";

                break;
            case R.id.checkBoxSell:

                F_b_s=checked?"bearish":"";
                break;

        }
    }


}
