package com.eqsis.notifications.API;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.eqsis.notifications.Activity.AlertActivity;
import com.eqsis.notifications.Activity.SettingsActivity;
import com.eqsis.notifications.R;
import com.eqsis.notifications.Utils.AlertItems;
import com.eqsis.notifications.Utils.Constants;
import com.eqsis.notifications.Utils.MyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;
import static com.eqsis.notifications.Utils.Constants.authToken;

public class SingleAlertApiRequest extends AsyncTask<Object,Void,Void> {

    private ArrayList<AlertItems> alertItemsListAlertActivity;
    private URL url;
    private RecyclerView alertrecyclerView;
    private RecyclerView.Adapter alertadapter;
   
    private HttpsURLConnection httpURLConnection;
    private String jsonDataString;

    @Override
    protected void onPreExecute() {
       
            AlertActivity.Instance.showMainProgress(true);
       

    }

    @Override
    protected Void doInBackground(Object... objects) {

        try {

           

            alertItemsListAlertActivity=new ArrayList<>();
            String surl="https://eqsisalert.herokuapp.com/api/v1/alerts/show";
            url=new URL(surl);
            httpURLConnection=(HttpsURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Authorization","Token token="+authToken);
            
            postingData();
            getApiDataString();
            fetchingSingleAlertData();
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void postingData() {


            try {
                JSONObject jo=new JSONObject();


                try {
                    jo.put("push_notification_ids",Constants.alertId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


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

            } catch (IOException e) {
                e.printStackTrace();
            }
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

        Log.d("single response",jsonDataString);
    }
    private void fetchingSingleAlertData() {
        try {
            String crappyPrefix = "null";

            if(jsonDataString.startsWith(crappyPrefix)){
                jsonDataString = jsonDataString.substring(crappyPrefix.length(), jsonDataString.length());
            }
            Log.d("single response", jsonDataString);
            JSONObject response=new JSONObject(jsonDataString);

            JSONArray JAR =response.getJSONArray("data");

            for(int i=0;i<JAR.length();i++)
            {
                JSONObject JA= (JSONObject) JAR.get(i);

                String token= JA.getJSONObject("attributes").getJSONObject("brand").getString("code_name");
                int target=JA.getJSONObject("attributes").getInt("target");
                int stopLoss=JA.getJSONObject("attributes").getInt("stop_loss");
                int bid=JA.getJSONObject("attributes").getInt("risk");
                String type= JA.getJSONObject("attributes").getJSONObject("alert_category").getString("name");
                String buySell= JA.getJSONObject("attributes").getJSONObject("analysis_inference").getString("name");
                String id=JA.getString("id");
                AlertItems alertItems;
                alertItems=new AlertItems(token,target,bid,stopLoss,type,id,buySell);
                alertItemsListAlertActivity.add(alertItems);

                Log.d("single alert",String.format("\ntarget %s \ntoken %s \n",target,token));

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //setting up recycler view
        alertrecyclerView= AlertActivity.Instance.findViewById(R.id.AlertRecyclerView);
        alertrecyclerView.setHasFixedSize(true);
        alertrecyclerView.setLayoutManager(new LinearLayoutManager(AlertActivity.Instance));
        alertadapter=new MyAdapter(alertItemsListAlertActivity,AlertActivity.Instance);
        alertrecyclerView.setAdapter(alertadapter);

        AlertActivity.Instance.showMainProgress(false);

    }

}
