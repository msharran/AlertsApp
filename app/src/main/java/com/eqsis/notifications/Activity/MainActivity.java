    package com.eqsis.notifications.Activity;

    import android.app.Dialog;
    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.app.ProgressDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.graphics.Color;
    import android.os.Handler;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.support.v7.widget.LinearLayoutManager;
    import android.support.v7.widget.RecyclerView;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.ProgressBar;
    import android.widget.Switch;


    import com.eqsis.notifications.API.FetchData;
    import com.eqsis.notifications.API.SignInApiRequest;
    import com.eqsis.notifications.API.SignOutApiRequest;
    import com.eqsis.notifications.FCM_Services.Constants;

    import com.eqsis.notifications.R;
    import com.eqsis.notifications.Utils.MyAdapter;
    import com.google.firebase.iid.FirebaseInstanceId;

    import static com.eqsis.notifications.API.FetchData.alertItemsList;


    public class MainActivity extends AppCompatActivity  {



        private Switch IS,II,PS,PI,OS;
        private ProgressDialog dialog;
        private FetchData fetchData;
        private View linlaHeaderProgress ;



        public static MainActivity Instance;

        private android.support.v7.widget.Toolbar toolbar;


        public  void showMainProgress(final boolean show) {

            linlaHeaderProgress.setVisibility(show ? View.VISIBLE : View.GONE);


        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            try {

                //if signed in

               if(com.eqsis.notifications.Utils.Constants.authToken==null)
               {
                   SharedPreferences sharedPreferences = this.getSharedPreferences("DATA", Context.MODE_PRIVATE);
                   com.eqsis.notifications.Utils.Constants.authToken = sharedPreferences.getString("auth_token", null);

               }

                //Setting Custom Actionbar
                toolbar = findViewById(R.id.Toolbar_main);
                setSupportActionBar(toolbar);

                Instance = MainActivity.this;



                linlaHeaderProgress = findViewById(R.id.linlaHeaderProgress);




                Object[] object=new Object[3];
                object[0]="https://eqsisalert.herokuapp.com/api/v1/alerts";

                object[2]=this;
                fetchData = new FetchData("alerts");
                fetchData.execute(object);









                //setting up push notifications
                pushNotifications();


                //switch initialization
                IS = findViewById(R.id.sw_IS);
                II = findViewById(R.id.sw_II);
                PS = findViewById(R.id.sw_PS);
                PI = findViewById(R.id.sw_PI);
                OS = findViewById(R.id.sw_OS);






            } catch (Exception e) {
                e.printStackTrace();
            }

        }




        private void pushNotifications() {
            /*
             * If the device is having android oreo we will create a notification channel
             * */

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
                mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotificationManager.createNotificationChannel(mChannel);
            }

            Log.d("My Firebase Token", FirebaseInstanceId.getInstance().getToken());

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main,menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            try {
                int id=item.getItemId();
                switch (id) {

                    case R.id.preference:
                        onPreferenceClicked();
                        break;
                    case R.id.settings:
                        onSettingsClicked();
                        break;
                    case R.id.profile:
                        onProfileClicked();
                        break;
                    case R.id.signOut:
                        onSignOutClicked();
                        break;
                    case R.id.refresh:
                        onRefreshClicked();
                        break;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.onOptionsItemSelected(item);
        }

        private void onRefreshClicked() {

            Object[] object=new Object[3];
            object[0]="https://eqsisalert.herokuapp.com/api/v1/alerts";

            object[2]=this;
            fetchData = new FetchData("alerts");
            fetchData.execute(object);


        }

        private void onPreferenceClicked() {
            startActivity(new Intent(MainActivity.this,PreferenceActivity.class));
        }

        private void onSettingsClicked() {
            Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        }

        private void onSignOutClicked() {
            finish();
            SignOutApiRequest signOutApiRequest=  new SignOutApiRequest();
            signOutApiRequest.execute();
            Intent i =new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);

        }

        private void onProfileClicked() {
            Intent i =new Intent(MainActivity.this,ProfileActivity.class);
            startActivity(i);
        }








    }
