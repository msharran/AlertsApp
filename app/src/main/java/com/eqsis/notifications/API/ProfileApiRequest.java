package com.eqsis.notifications.API;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import com.eqsis.notifications.Activity.MainActivity;
import com.eqsis.notifications.Activity.ProfileActivity;
import com.eqsis.notifications.R;
import com.eqsis.notifications.Utils.AlertItems;
import com.eqsis.notifications.Utils.ProfileItem;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static com.eqsis.notifications.Utils.Constants.authToken;

public class ProfileApiRequest extends AsyncTask<Object,Void,Void> {
    private ArrayList<AlertItems> alertItemsList;
    private URL url;
    private HttpsURLConnection httpURLConnection;
    private String jsonDataString;
    public static ProfileItem profileItem;

    @Override
    protected void onPreExecute() {
        // SHOW THE SPINNER WHILE LOADING FEEDS
        if(ProfileActivity.Instance!=null)
        {
            ProfileActivity.Instance.showMainProgress(true);
        }

    }


    @Override
    protected void onPostExecute(Void aVoid) {
        ProfileItem item = profileItem;
        ProfileActivity.name.setText(item.getUserName());
        ProfileActivity.dob.setText(item.getDob());
        ProfileActivity.gender.setText(item.getGender());
        ProfileActivity.status.setText(item.getStatus());
        ProfileActivity.maritalStatus.setText(item.getMaritalStatus());
        ProfileActivity.email.setText(item.getEmail());
        ProfileActivity.phone.setText(item.getUserPhone());
        if(ProfileActivity.Instance!=null)
        {
            ProfileActivity.Instance.showMainProgress(false);
        }

    }

    @Override
    protected Void doInBackground(Object... objects) {

            try
            {
                String sUrl=(String)objects[0];

                alertItemsList=new ArrayList<>();
                url=new URL(sUrl);
                httpURLConnection=(HttpsURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Authorization","Token token="+authToken);




                InputStream inputStream= httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String line="";
                while (line!=null)
                {
                    line=bufferedReader.readLine();
                    jsonDataString = jsonDataString+line;

                }
                String crappyPrefix = "null";

                if(jsonDataString.startsWith(crappyPrefix)){
                    jsonDataString = jsonDataString.substring(crappyPrefix.length(), jsonDataString.length());
                }
                JSONObject response=new JSONObject(jsonDataString);
                JSONObject JO=response.getJSONObject("data").getJSONObject("attributes");
                String name= JO.getString("firstname").toUpperCase()+" "+JO.getString("lastname").toUpperCase();
                String gender=JO.getString("gender");
                String dob=JO.getString("dob");
                String[] dobArray =dob.split("-");
                dob=dobArray[2]+"/"+dobArray[1]+"/"+dobArray[0];
                String marital_status=JO.getString("marital_status");
                String email=JO.getString("email");
                String phone=JO.getString("phone");
                String status=JO.getString("status");
                profileItem=new ProfileItem(name,dob,gender,status,marital_status,email,phone);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }



        return null;
    }
}
