package com.eqsis.notifications.API;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.eqsis.notifications.Activity.LoginActivity;
import com.eqsis.notifications.Activity.MainActivity;


import com.eqsis.notifications.Activity.SplashActivity;
import com.eqsis.notifications.FCM_Services.MyFirebaseInstanceIdService;
import com.eqsis.notifications.R;
import com.eqsis.notifications.Utils.Constants;
import com.eqsis.notifications.Utils.Eqsis;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static com.eqsis.notifications.Utils.Constants.authToken;


public class SignInApiRequest extends AsyncTask<Object,Void,Void> {
    private Context mcontext;
    private String mEmail;
    private String mPassword;
    private static String TAG="SignINApiRequest";
    private int responseCode=0;
    private HttpsURLConnection urlConnection;


    private JSONObject response;



    @Override
    protected Void doInBackground(Object... objects) {
        try {

            //requirements for the json body
            mcontext=(Context)objects[0];
            mEmail=(String)objects[1];
            mPassword=(String)objects[2];
            String deviceManufacturer = android.os.Build.MANUFACTURER;
            String deviceModelName = android.os.Build.MODEL;
            String osVersion = android.os.Build.VERSION.RELEASE;
            String osName=Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
//            String fcmToken = MyFirebaseInstanceIdService.fcm_token;
            String token=FirebaseInstanceId.getInstance().getToken().toString();
            Log.d("SignInApiReq:token",token);
//            Log.d("methodToken",token);
            //creating a json body
            JSONObject jo=new JSONObject();
            JSONObject jo1=new JSONObject();
            jo.put("email",mEmail);
            jo.put("password",mPassword);

            jo1.put("device_fcm_id", token);
            jo1.put("device_os",osName);
            jo1.put("device_os_version",osVersion);
            jo1.put("device_manufacturer",deviceManufacturer);
            jo1.put("device_model_name", deviceModelName);
            jo1.put("sns_user_key", "");
            jo1.put("status", "active");

            jo.put("user_device",jo1);



            String data=jo.toString();
            URL url=new URL("https://eqsisalert.herokuapp.com/api/v1/sign_in");
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json");

            DataOutputStream dStream=new DataOutputStream(urlConnection.getOutputStream());
            dStream.writeBytes(data);
            dStream.flush();
            dStream.close();
            responseCode = urlConnection.getResponseCode();
            if(responseCode== 200)
            {
                InputStream inputStream= urlConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String line="";
                String jsonDataString="";
                while (line!=null)
                {
                    line=bufferedReader.readLine();
                    jsonDataString = jsonDataString+line;

                }

                response=new JSONObject(jsonDataString);
                authToken=response.getJSONObject("data").getJSONObject("attributes").getString("auth_token");
//                Eqsis.logged_in=(response.getJSONObject("data").getJSONObject("attributes").getBoolean("logged_in"));
                SharedPreferences sharedPreferences = LoginActivity.Instance.getSharedPreferences("DATA",Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("Logged_in",(response.getJSONObject("data").getJSONObject("attributes").getBoolean("logged_in"))).apply();
                sharedPreferences.edit().putString("auth_token",authToken).apply();
                Log.d("SignInAPIresponceToken",authToken);
                Log.d("SignInAPIDevicedata",String.format("\nmanufacturer: %s\nos: %s\nosVersion %s\nmodel: %s\n",deviceManufacturer,osName,osVersion,deviceModelName));
                Log.d("SignInAPIRequest",response.toString());
            }else{

                Log.d(TAG,"res code: "+responseCode);



            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


       if(responseCode!=200)
       {
           ((LoginActivity)mcontext).showProgress(false);
           Snackbar.make(((LoginActivity)mcontext).findViewById(R.id.loginContent),"Please fill in the valid credentials",Snackbar.LENGTH_LONG).show();

       }else
       {
           ((LoginActivity)mcontext).finish();
            Intent intent = new Intent(mcontext,MainActivity.class);
           ((LoginActivity)mcontext).startActivity(intent);
           ((LoginActivity)mcontext).showProgress(false);

       }


    }
}
