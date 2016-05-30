package com.practice.gmaillogin;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Himanshu Sagar on 30-05-2016.
 */
public abstract class AbstractGetNameTask extends AsyncTask <Void, Void, Void >
{
    public static String LOG_TAG  = AbstractGetNameTask.class.getSimpleName();
    public static final String EXTRA_EMAIL = "com.practice.gmaillogin" + "email_id";

    protected MainActivity mainActivity;
    public static String GOOGLE_USER_DATA;
    protected String mScope;
    protected String mEmail;
    protected int mRequest;

    public AbstractGetNameTask(MainActivity mainActivity ,String mEmail, String mScope ) {
        this.mEmail = mEmail;
        this.mScope = mScope;
        this.mainActivity = mainActivity;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        try
        {
            fetchNameFromProfileServer();

        }
        catch(IOException e)
        {
            onError("Following Error occured " + e.getMessage() ,e);
        }
        catch(JSONException ex)
        {
            onError("Bad Response " + ex.getMessage() ,ex);
        }
        return null;


    }
    private void onError(String msg, Exception e)
    {
        if(e!=null)
        {
            Log.e(LOG_TAG , "Exception" + msg , e);
        }

    }
    protected abstract String fetchToken() throws IOException;

    private void fetchNameFromProfileServer() throws IOException, JSONException
    {
        String token = fetchToken();


        URL url = new URL("http:www.googleapis.com/oauth2/" +
                "vl/userinfo?access_token="+ token);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int sc = con.getResponseCode();
        if(sc == 200 )
        {
            InputStream is = con.getInputStream();
            GOOGLE_USER_DATA = readResponse(is);
            is.close();

            Intent in = new Intent(mainActivity , HomeActivity.class);
            in.putExtra(EXTRA_EMAIL,mEmail);
            mainActivity.startActivity(in);
            mainActivity.finish();



        }
        else if(sc == 401)
        {
            try
            {
                GoogleAuthUtil.clearToken(mainActivity, token);
            }
            catch (GoogleAuthException g)
            {

            }
}
        else
        {
            onError("Returned by Server " + sc ,null);

        }

    }
    private static String readResponse(InputStream is) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte data[] = new byte[2048];
        int len =0 ;

        while( (len = is.read(data, 0, data.length) ) >=0)
        {
            bos.write(data, 0 , len);
        }
        return new String(bos.toByteArray() , "UTF-8");

    }
}
