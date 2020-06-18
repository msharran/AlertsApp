package com.eqsis.notifications.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.eqsis.notifications.API.PreferenceApiRequest;
import com.eqsis.notifications.R;
import com.eqsis.notifications.Utils.PreferenceAdapter;
import com.eqsis.notifications.Utils.PreferenceItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.eqsis.notifications.API.PreferenceApiRequest.Padapter;

public class PreferenceActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static PreferenceActivity Instance;
    private View linlaHeaderProgress,relLayout;
    public static CheckBox chkpos,chkop,chkint,chkbuy,chksell;
    public static boolean checked;
    public static ImageButton btnnotifyPositional,btnnotifyOptional,btnnotifyIntraday,btnnotifyBuy,btnnotifySell;
    public static HashMap<String, Integer> currentImageMap= new HashMap<String, Integer>()
    {
        {
            put("pos", 0);
            put("op", 0);
            put("int", 0);
            put("buy", 0);
            put("sell", 0);

        }

    };
    private int[] notifyImages={R.drawable.ic_notify_green,R.drawable.ic_notify_grey};


    public static ArrayList<PreferenceItems> subscribedList;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        init();
        initRecyclerView();
        showMainProgress(false);

        onClicked();



    }



    private void initRecyclerView() {
        new PreferenceApiRequest("get").execute("https://eqsisalert.herokuapp.com/api/v1/current_user/preferences");



        EditText searchtxt=findViewById(R.id.searchTxt_preference);
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
        ArrayList<PreferenceItems> filteredList = new ArrayList<>();

        for (PreferenceItems item : PreferenceApiRequest.PreferenceItemsList) {
            if (item.getStock().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        Padapter.filterList(filteredList);
    }

    private void onClicked() {
        btnnotifyPositional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggleImage(btnnotifyPositional,"pos");
            }
        });
        btnnotifyOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggleImage(btnnotifyOptional,"op");
            }
        });
        btnnotifyIntraday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggleImage(btnnotifyIntraday,"int");
            }
        });
        btnnotifyBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggleImage(btnnotifyBuy,"buy");
            }
        });
        btnnotifySell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggleImage(btnnotifySell,"sell");
            }
        });

    }

    private void toggleImage(ImageButton notify,String type)
    {

       Log.d("imgbtnID", String.valueOf(notify.getId())) ;
        if(currentImageMap.get(type)==(notifyImages.length-1))
        {
            notify.setImageResource(notifyImages[currentImageMap.get(type)]);
            currentImageMap.put(type,0);
        }else{

            notify.setImageResource(notifyImages[currentImageMap.get(type)]);
            currentImageMap.put(type,1);
        }



    }



    private void init() {
        toolbar = findViewById(R.id.Toolbar_preference);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        linlaHeaderProgress=findViewById(R.id.preference_progressbar);
        relLayout=findViewById(R.id.preference_RelativeL);
        Instance=this;

        btnnotifyPositional=findViewById(R.id.notify_positional);
        btnnotifyOptional=findViewById(R.id.notify_optional);
        btnnotifyIntraday=findViewById(R.id.notify_intraday);
        btnnotifyBuy=findViewById(R.id.notify_buy);
        btnnotifySell=findViewById(R.id.notify_sell);

        chkpos=findViewById(R.id.checkboxPosition_preference);
        chkop=findViewById(R.id.checkboxOptional_preference);
        chkint=findViewById(R.id.checkBoxIntraday_preference);
        chkbuy=findViewById(R.id.checkBoxBuy_preference);
        chksell=findViewById(R.id.checkBoxSell_preference);

        subscribedList=new ArrayList<>();







    }

    public  void showMainProgress(final boolean show) {

        linlaHeaderProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        relLayout.setVisibility(show?View.GONE:View.VISIBLE);

    }



    public void onSubscribeClicked(View view) {
        try {



            for(int i=0;i<PreferenceApiRequest.PreferenceItemsList.size();i++)
                    {

                        if(PreferenceApiRequest.PreferenceItemsList.get(i).getStock().equals("positional"))
                        {

                            subscribedList.get(i).setSubscribtion( chkpos.isChecked()?"subscribed":"unsubscribed");
                            String it=subscribedList.get(i).getSubscribtion();
                            String it2=PreferenceApiRequest.PreferenceItemsList.get(i).getSubscribtion();

                            subscribedList.get(i).setNotified((currentImageMap.get("pos"))==1?true:false);
                        }
                        if(PreferenceApiRequest.PreferenceItemsList.get(i).getStock().equals("optional"))
                        {
                            subscribedList.get(i).setSubscribtion(chkop.isChecked()?"subscribed":"unsubscribed");
                            subscribedList.get(i).setNotified((currentImageMap.get("op"))==1?true:false);
                        }
                        if(PreferenceApiRequest.PreferenceItemsList.get(i).getStock().equals("Intraday"))
                        {
                            subscribedList.get(i).setSubscribtion(chkint.isChecked()?"subscribed":"unsubscribed");
                            subscribedList.get(i).setNotified((currentImageMap.get("int"))==1?true:false);
                        }
                        if(PreferenceApiRequest.PreferenceItemsList.get(i).getStock().equals("bullish"))
                        {
                            subscribedList.get(i).setSubscribtion(chkbuy.isChecked()?"subscribed":"unsubscribed");
                            subscribedList.get(i).setNotified((currentImageMap.get("buy"))==1?true:false);
                        }
                        if(PreferenceApiRequest.PreferenceItemsList.get(i).getStock().equals("bearish"))
                        {
                            subscribedList.get(i).setSubscribtion(chksell.isChecked()?"subscribed":"unsubscribed");
                            subscribedList.get(i).setNotified((currentImageMap.get("sell"))==1?true:false);
                        }
                        if(PreferenceApiRequest.PreferenceItemsList.get(i).getType().equals("Brand"))
                        {
                            String stock=PreferenceApiRequest.PreferenceItemsList.get(i).getStock();
                            subscribedList.get(i).setNotified((PreferenceAdapter.positionMap.get(stock))==1?true:false);
                            for(Map.Entry<String, String> entry:PreferenceAdapter.id_subscribtion_map.entrySet())
                            {
                               if(entry.getKey().equals(PreferenceApiRequest.PreferenceItemsList.get(i).getId()))
                                {
                                    subscribedList.get(i).setSubscribtion(entry.getValue());
                                }

                            }
                        }

                    }


        new PreferenceApiRequest("put").execute("https://eqsisalert.herokuapp.com/api/v1/current_user/preference");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
