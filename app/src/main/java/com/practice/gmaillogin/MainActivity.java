package com.practice.gmaillogin;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;

public class MainActivity extends AppCompatActivity {


    Context mContext  = MainActivity.this;
    AccountManager mAccountManager;
    String token;
    int serverCode;

    private static final String scope = "oauth2:https://www.googleapis.com/auth/userinfo.profile";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        syncGoogleAccount();
    }
    private String[] getAccountNames()
    {
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[ accounts.length ];

        for (int i = 0 ; i < names.length ; i++)
        {
            names[i] = accounts[i].name;
        }
        return names;

    }
    private AbstractGetNameTask getTask(MainActivity activity ,String email ,String scope )
    {
        return new GetNameInForeground(activity,email,scope);

    }

    public void syncGoogleAccount()
    {
        if(isNetworkAvailable() == true)
        {
            String[] accountarrs = getAccountNames();
            if(accountarrs.length > 0)
            {
                 getTask(MainActivity.this , accountarrs[0] ,scope).execute();
            }
            else
            {
                Toast.makeText(MainActivity.this,"NO Google Account Sync" ,Toast.LENGTH_SHORT).show();

            }
        }
        else
        {

            Toast.makeText(MainActivity.this,"NO Network Available" ,Toast.LENGTH_SHORT).show();

        }

    }
    public boolean isNetworkAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) mContext.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected())
        {
            Log.e("Network Testing" , "Available");
            return true;
        }
        else
        {
            Log.e("Network Testing" , "NOT Available");
            return false;

        }
    }
}

