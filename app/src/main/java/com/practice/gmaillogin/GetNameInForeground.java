package com.practice.gmaillogin;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

/**
 * Created by Himanshu Sagar on 30-05-2016.
 */

public class GetNameInForeground extends AbstractGetNameTask
{
    public GetNameInForeground(MainActivity mainActivity ,String mEmail, String mScope)
    {
        super(mainActivity,mEmail,mScope);
    }

    @Override
    protected String fetchToken( ) throws IOException
    {
        try
        {
            return GoogleAuthUtil.getToken(mainActivity,mEmail,mScope);

        }
        catch(GooglePlayServicesAvailabilityException e)
        {


        }
        catch(UserRecoverableAuthException u)
        {
            mainActivity.startActivityForResult(u.getIntent() , mRequest);

        }
        catch(GoogleAuthException ga)
        {
            ga.printStackTrace();

        }
        return null;


    }
}
