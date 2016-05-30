package com.practice.gmaillogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HomeActivity extends AppCompatActivity {
    ImageView imageProfile;
    TextView textViewName,
            textViewEmail,
            textViewGender;

    String textName, textGender, textEmail, userImageUrl;

    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textViewName = (TextView) findViewById(R.id.textViewNameValue);
        textViewEmail = (TextView) findViewById(R.id.textViewEmailValue);
        textViewGender = (TextView) findViewById(R.id.textViewGenderValue);
        imageProfile = (ImageView) findViewById(R.id.imageView1);

        Intent in = getIntent();
        textEmail = in.getStringExtra(AbstractGetNameTask.EXTRA_EMAIL);

        System.out.print(textEmail);
        Log.e("textEmail", textEmail);

        textViewEmail.setText(textEmail);

        try {
            JSONObject profileData = new JSONObject(AbstractGetNameTask.GOOGLE_USER_DATA);

            if (profileData.has("picture")) {
                userImageUrl = profileData.getString("picture");
                new GetImageFromUrl().execute(userImageUrl);
            }
            if (profileData.has("name")) {
                textName = profileData.getString("name");
                textViewName.setText(textName);

            }
            if (profileData.has("gender")) {

                textGender = profileData.getString("gender");
                textViewGender.setText(textGender);
            }


        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    class GetImageFromUrl extends AsyncTask<String, Void, Bitmap>
    {
        @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap map = null;
                for (String url : params) {
                    map = downloadImage(url);
                }
                return map;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                imageProfile.setImageBitmap(bitmap);
            }

            private Bitmap downloadImage(String url) {
                Bitmap bitmap = null;
                InputStream inputStream = null;
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();

                bmOptions.inSampleSize = 1;
                try {
                    inputStream = getHttpConnection(url);
                    bitmap = BitmapFactory.decodeStream(inputStream, null, bmOptions);

                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            private InputStream getHttpConnection(String urlString) throws IOException {
                InputStream inputStream = null;
                URL url = new URL(urlString);

                URLConnection urlConnection = url.openConnection();

                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        throw new Exception();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return inputStream;
            }
        }

}