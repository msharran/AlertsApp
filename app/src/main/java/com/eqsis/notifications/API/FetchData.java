package com.eqsis.notifications.API;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.eqsis.notifications.Activity.AlertActivity;
import com.eqsis.notifications.Activity.FilteredActivity;
import com.eqsis.notifications.Activity.MainActivity;
import com.eqsis.notifications.Activity.SettingsActivity;
import com.eqsis.notifications.R;
import com.eqsis.notifications.Utils.AlertItems;
import com.eqsis.notifications.Utils.MyAdapter;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ContentHandler;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static com.eqsis.notifications.Utils.Constants.authToken;


public class FetchData extends AsyncTask<Object,Void,Void> {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private static final String TAG ="FetchData" ;
    private String jsonDataString="";
    private URL url;
    private  HttpsURLConnection httpURLConnection;
    public static ArrayList<AlertItems> alertItemsList;
    private Context mcontext;

    private int responseCode=0;
    private String which;


    public FetchData(String which)
    {
        this.which=which;
    }
    @Override
    protected void onPreExecute() {
        if(which.equals("alerts"))
        {
            MainActivity.Instance.showMainProgress(true);
        }
        if(which.equals("filter"))
        {
            FilteredActivity.Instance.showProgress(true);
        }


    }

    @Override
    protected Void doInBackground(Object... objects) {

        try {
            String sUrl=(String)objects[0];
//            which=(String)objects[1];
            mcontext= (Context) objects[2];

            alertItemsList=new ArrayList<>();
            url=new URL(sUrl);
            httpURLConnection=(HttpsURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Authorization","Token token="+authToken);


            switch(which)
            {
                case "filter":
                    onHittingFilterApi();
                    getApiDataString();
                    fetchingAlertsData();
                    break;
                case "alerts":
                    getApiDataString();
                    fetchingAlertsData();
                    break;

            }





        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }



    private void getApiDataString() {
        InputStream inputStream= null;
        try {
            inputStream = httpURLConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        String line="";
        while (line!=null)
        {
            try {
                line=bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            jsonDataString = jsonDataString+line;

        }
    }


    private void fetchingAlertsData() {

        try {
            try {
                int responseCode = httpURLConnection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject response=new JSONObject(jsonDataString);

            JSONArray JA =response.getJSONArray("data");



            for(int i=0;i<JA.length();i++)
            {
                JSONObject JO =JA.getJSONObject(i);

                String token= JO.getJSONObject("attributes").getJSONObject("brand").getString("code_name");
                int target=JO.getJSONObject("attributes").getInt("target");
                int stopLoss=JO.getJSONObject("attributes").getInt("stop_loss");
                int bid=JO.getJSONObject("attributes").getInt("risk");
                String type= JO.getJSONObject("attributes").getJSONObject("alert_category").getString("name");
                String buySell= JO.getJSONObject("attributes").getJSONObject("analysis_inference").getString("name");
                String id=JO.getString("id");
                AlertItems alertItems;
                alertItems=new AlertItems(token,target,bid,stopLoss,type,id,buySell);
                alertItemsList.add(alertItems);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onHittingFilterApi()
    {

        try {
            JSONObject jo=new JSONObject();

            if(SettingsActivity.Sadapter.checkedStocks.size()>0)
            {
                JSONArray ja=new JSONArray();
                for(int i=0;i<SettingsActivity.Sadapter.checkedStocks.size();i++)
                {
                    ja.put(SettingsActivity.Sadapter.checkedStocks.get(i).getStock().toString());

                }
                jo.put("brand_code_name",ja);
            }else
            {
                jo.put("brand_code_name","");
            }


            jo.put("alert_category_name",SettingsActivity.F_i_p);
            jo.put("analysis_inference",SettingsActivity.F_b_s);
            String data=jo.toString();
            httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type","application/json");
            httpURLConnection.setRequestProperty("Authorization","Token token="+authToken);

            DataOutputStream dStream=new DataOutputStream(httpURLConnection.getOutputStream());
            dStream.writeBytes(data);
            dStream.flush();
            dStream.close();
            responseCode = httpURLConnection.getResponseCode();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d(TAG,this.jsonDataString);
        if(which.equals("alerts"))
        {
            initMainRecyclerView();
            MainActivity.Instance.showMainProgress(false);
        }
        if(which.equals("filter"))
        {
            initFilterRecyclerView();
            FilteredActivity.Instance.showProgress(false);
        }

    }
    private void initFilterRecyclerView()
    {
        //setting up recycler view
        recyclerView=(RecyclerView) FilteredActivity.Instance.findViewById(R.id.Filter_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FilteredActivity.Instance));
        adapter=new MyAdapter(alertItemsList,FilteredActivity.Instance);
        recyclerView.setAdapter(adapter);

    }
    private void initMainRecyclerView() {
        //setting up recycler view
        recyclerView=(RecyclerView) MainActivity.Instance.findViewById(R.id.MainRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.Instance));
        adapter=new MyAdapter(alertItemsList,MainActivity.Instance);
        recyclerView.setAdapter(adapter);

    }

}
