package com.eqsis.notifications.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.eqsis.notifications.Activity.LoginActivity;
import com.eqsis.notifications.Activity.MainActivity;
import com.eqsis.notifications.Activity.SplashActivity;
import com.eqsis.notifications.Utils.AlertItems;
import com.eqsis.notifications.Utils.Eqsis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static com.eqsis.notifications.Utils.Constants.authToken;

public class SignOutApiRequest extends AsyncTask<Object,Void,Void> {
    private URL url;
    private HttpsURLConnection httpURLConnection;
    private String jsonDataString;



    @Override
    protected Void doInBackground(Object... objects) {
        try {
            try {
                url=new URL("https://eqsisalert.herokuapp.com/api/v1/signout");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                httpURLConnection=(HttpsURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpURLConnection.setRequestProperty("Authorization","Token token="+authToken);

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

            Log.d("Signout response",jsonDataString);
            String crappyPrefix = "null";

            if(jsonDataString.startsWith(crappyPrefix)){
                jsonDataString = jsonDataString.substring(crappyPrefix.length(), jsonDataString.length());
            }

            try {
                int responseCode = httpURLConnection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject response=new JSONObject(jsonDataString);

//            Eqsis.logged_in=(response.getJSONObject("data").getJSONObject("attributes").getBoolean("logged_in"));
            SharedPreferences sharedPreferences = LoginActivity.Instance.getSharedPreferences("DATA", Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("Logged_in",(response.getJSONObject("data").getJSONObject("attributes").getBoolean("logged_in"))).apply();
            Log.v("Signout clicked", String.valueOf(response.getJSONObject("data").getJSONObject("attributes").getBoolean("logged_in")));
            sharedPreferences.edit().putString("auth_token",null).apply();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
