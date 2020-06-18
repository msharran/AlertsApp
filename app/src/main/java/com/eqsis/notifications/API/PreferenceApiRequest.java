package com.eqsis.notifications.API;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eqsis.notifications.Activity.PreferenceActivity;
import com.eqsis.notifications.R;
import com.eqsis.notifications.Utils.AlertItems;
import com.eqsis.notifications.Utils.PreferenceAdapter;
import com.eqsis.notifications.Utils.PreferenceItems;
import com.eqsis.notifications.Utils.StockAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import static com.eqsis.notifications.Activity.SettingsActivity.Sadapter;
import static com.eqsis.notifications.Utils.Constants.authToken;

public class PreferenceApiRequest extends AsyncTask<Object,Void,Void> {


    private final String APItype;
    private Context mcontext;
    public static ArrayList<PreferenceItems> PreferenceItemsList;
    private  ArrayList<HashMap<String,String>> SubscribtionList;
    private  ArrayList<HashMap<String,String>> NotificationList;
    private HashMap<String,String> name_id_map;
    private URL url;
    private HttpsURLConnection httpURLConnection;

    private String jsonDataString;
    private RecyclerView PrecyclerView;
    public static PreferenceAdapter Padapter;
    private JSONArray JA;

    public PreferenceApiRequest (String APItype)
    {
        this.APItype=APItype;
    }
    @Override
    protected void onPreExecute() {
        try {
            PreferenceActivity.Instance.showMainProgress(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected Void doInBackground(Object... objects) {


        try {
            String sUrl=(String)objects[0];




            url=new URL(sUrl);
            httpURLConnection = (HttpsURLConnection) url.openConnection();


            switch(APItype)
            {
                case "put":
                      putData();


                    break;
                case "get":
                    httpURLConnection.setRequestProperty("Authorization","Token token="+authToken);
                    getApiDataString();
                    fetchingAlertsData();
                    break;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private void putData() {
        try {

            JSONObject JO=new JSONObject();
             JA=new JSONArray();
            for(int i=0;i<PreferenceItemsList.size();i++)
            {
                PreferenceItems item1=PreferenceActivity.subscribedList.get(i);
                PreferenceItems item2=PreferenceItemsList.get(i);

                if(!item1.getSubscribtion().equals(item2.getSubscribtion()) || (item1.isNotified()!=item2.isNotified()))
                {
                    JA.put(new JSONObject().put("id",item1.getId())
                            .put("allow_push_notification",item1.isNotified())
                            .put("status",item1.getSubscribtion()));
                }

            }

            if(!JA.isNull(0))
            {
                JO.put("user_preferences",JA);
                String JOString=JO.toString();
                Log.d("Preference Api Request",JOString);
                httpURLConnection.setRequestProperty("Authorization","Token token="+authToken);
                httpURLConnection.setRequestProperty("Content-Type","application/json");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("PUT");

                OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
                out.write(String.valueOf(JO));
                out.close();
                int reqCode=httpURLConnection.getResponseCode();
//                httpURLConnection.getInputStream();
                getApiDataString();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getApiDataString() {
        try {


            InputStream  inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line="";
            while (line!=null)
            {

                    line=bufferedReader.readLine();

                jsonDataString = jsonDataString+line;

            }
            if(APItype.equals("put"))
            Log.d("Preference Api Response",jsonDataString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void fetchingAlertsData() {

        try {
            String crappyPrefix = "null";

            if(jsonDataString.startsWith(crappyPrefix)){
                jsonDataString = jsonDataString.substring(crappyPrefix.length(), jsonDataString.length());
            }
            JSONObject response=new JSONObject(jsonDataString);

            JSONArray JA1 =response.getJSONArray("data");

            PreferenceItemsList=new ArrayList<>();

            for(int i=0;i<JA1.length();i++)
            {
                JSONObject JO =JA1.getJSONObject(i);

                String codeName= JO.getJSONObject("attributes").getJSONObject("target").getString("code_name");
                String subscribtion=JO.getJSONObject("attributes").getString("status");
                boolean isNotified=JO.getJSONObject("attributes").getBoolean("allow_push_notification");
                String type=JO.getJSONObject("attributes").getString("target_type");
                String id=JO.getString("id");
                PreferenceItems preferenceItems;
               preferenceItems=new PreferenceItems(codeName,id,subscribtion,isNotified,type);
                PreferenceItemsList.add(preferenceItems);



            }
            for(int i=0;i<PreferenceItemsList.size();i++)
            {
                PreferenceItems item=PreferenceItemsList.get(i);
                PreferenceActivity.subscribedList.add(new PreferenceItems(item.getStock(),item.getId(),item.getSubscribtion(),item.isNotified(),item.getType()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void alreadySubscribedAndNotificationEnabled() {
        try {
            for(PreferenceItems preferenceItem : PreferenceItemsList)
            {
                if(preferenceItem.isNotified())
                {
                    String codeName=preferenceItem.getStock();
                    switch (codeName)
                    {
                        case "positional":
                            PreferenceActivity.currentImageMap.put("pos",1);
                            PreferenceActivity.btnnotifyPositional.setImageResource(R.drawable.ic_notify_green);
                            break;
                        case "optional":
                            PreferenceActivity.currentImageMap.put("op",1);
                            PreferenceActivity.btnnotifyOptional.setImageResource(R.drawable.ic_notify_green);
                            break;
                        case "Intraday":
                            PreferenceActivity.currentImageMap.put("int",1);
                            PreferenceActivity.btnnotifyIntraday.setImageResource(R.drawable.ic_notify_green);
                            break;
                        case "bullish":
                            PreferenceActivity.currentImageMap.put("buy",1);
                            PreferenceActivity.btnnotifyBuy.setImageResource(R.drawable.ic_notify_green);
                            break;
                        case "bearish":
                            PreferenceActivity.currentImageMap.put("sell",1);
                            PreferenceActivity.btnnotifySell.setImageResource(R.drawable.ic_notify_green);
                            break;
                    }
                }
                 if(preferenceItem.getSubscribtion().equals("subscribed"))
                {
                    String codeName=preferenceItem.getStock();
                    switch (codeName)
                    {
                        case "positional":
                            PreferenceActivity.chkpos.setChecked(true);
                            break;
                        case "optional":
                            PreferenceActivity.chkop.setChecked(true);
                            break;
                        case "Intraday":
                            PreferenceActivity.chkint.setChecked(true);
                            break;
                        case "bullish":
                            PreferenceActivity.chkbuy.setChecked(true);
                            break;
                        case "bearish":
                            PreferenceActivity.chksell.setChecked(true);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPreferenceRecyclerView() {

        try {
            PrecyclerView=(RecyclerView) PreferenceActivity.Instance.findViewById(R.id.StockRecyclerView_preference);
            PrecyclerView.setHasFixedSize(true);
            PrecyclerView.setLayoutManager(new LinearLayoutManager(PreferenceActivity.Instance));
            Padapter=new PreferenceAdapter(PreferenceItemsList,PreferenceActivity.Instance);
            PrecyclerView.setAdapter(Padapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        try {
            if(APItype.equals("get"))
            {


                initPreferenceRecyclerView();
                alreadySubscribedAndNotificationEnabled();
                PreferenceActivity.Instance.showMainProgress(false);

            }else if(APItype.equals("put"))
            {


                PreferenceActivity.Instance.showMainProgress(false);
                PreferenceActivity.Instance.finish();
                Toast.makeText(PreferenceActivity.Instance,"Your Preferences have been subscribed successfully...",Toast.LENGTH_LONG).show();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
