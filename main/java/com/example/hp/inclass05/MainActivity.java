/*
In-Class Assignment #5
MainActivity.java
Ryan Harris
 */
package com.example.hp.inclass05;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {
    LinkedList<String> keywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");

        Button goButton = findViewById(R.id.go_button);
        Button nextImg = findViewById(R.id.next_image);
        Button lastImg = findViewById(R.id.last_image);




        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()) {
                    Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                    new GetDataAsync().execute("http://dev.theappsdr.com/apis/photos/keywords.php");
                    //new GetKeywordsAsyncTask((GetKeywordsAsyncTask.IKeyword) MainActivity.this).execute("http://dev.theappsdr.com/apis/photos/keywords.php");
                    new GetImageAsync((ImageView) findViewById(R.id.imageView2)).execute("http://dev.theappsdr.com/apis/photos/index.php");
                } else {
                    Toast.makeText(MainActivity.this, "Not Connected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        lastImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI)){
            return false;
        }
        return true;
    }

    public void handleKeywords(LinkedList<String> keywords){
        this.keywords = keywords;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose a Keyword")
                .setItems(keywords.toArray(new CharSequence[keywords.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("demo", "Clicked Go");
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private class GetImageAsync extends AsyncTask<String, Void, Void> {
        ImageView imageView;
        Bitmap bitmap = null;
        public GetImageAsync(ImageView iv){
            imageView = iv;
        }
        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection connection = null;
            //Bitmap image = null;
            bitmap = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                }

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(bitmap != null && imageView != null){
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private class GetDataAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null){
                    stringBuilder.toString();
                }

                result = stringBuilder.toString();

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null){
                Log.d("demo", result);
            } else {
                Log.d("demo", "null result");
            }
            super.onPostExecute(result);
        }
    }

}
