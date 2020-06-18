package com.eqsis.notifications.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eqsis.notifications.API.SignInApiRequest;
import com.eqsis.notifications.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{




    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    public static LoginActivity Instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Instance=LoginActivity.this;
        mProgressView = findViewById(R.id.Login_Progress);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);


        mPasswordView = (EditText) findViewById(R.id.password);


      execute();


    }

    private void main() {
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void execute() {

        if ( (internet_connection())){



                main();



        }else{
            //create a snackbar telling the user there is no internet connection and issuing a chance to reconnect
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                    "No internet connection ",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(ContextCompat.getColor(getApplicationContext(),
                    R.color.white));
            snackbar.setAction("Try again", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    execute();
                }
            }).show();
        }
    }


    boolean internet_connection() {
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    private void attemptLogin() {


        if(TextUtils.isEmpty(mUsernameView.getText().toString())|| TextUtils.isEmpty(mPasswordView.getText().toString()) )
        {
            Snackbar.make(findViewById(R.id.loginContent),"Please don't leave the fields empty",Snackbar.LENGTH_LONG).show();
            return;
        }
        else
            {
                showProgress(true);
                Object[] params=new Object[4];
                params[0]=LoginActivity.this;
                params[1]=mUsernameView.getText().toString();
                params[2]=mPasswordView.getText().toString();
                SignInApiRequest apiRequest =new SignInApiRequest();
                apiRequest.execute(params);

            }


    }


    /**
     * Shows the progress UI and hides the login form.
     */

    public  void showProgress(final boolean show) {

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);


    }


    public void onForgotPasswordClicked(View view) {
        Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.eqsis.com/login/?action=lostpassword"));
        startActivity(browserIntent);
    }

    public void onSignUpClicked(View view) {
        Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.eqsis.com/login/"));
        startActivity(browserIntent);
    }
}

