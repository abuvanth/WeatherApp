package com.abutech.root.resultapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
   TextView t1;
    RelativeLayout relativeLayout;
    EditText e1;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1=(TextView)findViewById(R.id.t1);
        t1.setVisibility(View.INVISIBLE);
        e1=(EditText)findViewById(R.id.editText);
        relativeLayout=(RelativeLayout)findViewById(R.id.activity_main);
    }
    class Getresult extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            String result="";
            URL url;
            HttpURLConnection connection=null;
            try{
                url=new URL(params[0]);
                connection=(HttpURLConnection)url.openConnection();
                InputStream in=connection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                StringBuilder builder=new StringBuilder();
                while((result=reader.readLine())!=null){
                    builder.append(result+"\n");
                }
                Thread.sleep(4000);
                return builder.toString().trim();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog=new ProgressDialog(MainActivity.this);
            dialog.setTitle("Please wait");
            dialog.setMessage("Loading");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            String main="";
            String description="";
            String message="";
            try{
                JSONObject server_data=new JSONObject(s);
                String data=server_data.getString("weather");
                JSONArray array=new JSONArray(data);
                JSONObject weather_data=array.getJSONObject(0);
                main=weather_data.getString("main");
                description=weather_data.getString("description");
                message=main+":"+description;

                t1.setText(message);
                if(main.equals("Clouds")) {
                    relativeLayout.setBackgroundResource(R.drawable.cloud);
                }
                else if (main.equals("Clear")){
                    relativeLayout.setBackgroundResource(R.drawable.clear);
                }
                else if (main.equals("Haze")){
                    relativeLayout.setBackgroundResource(R.drawable.haze);
                }
                else if(main.equals("Mist")){
                    relativeLayout.setBackgroundResource(R.drawable.mist);
                }
                else {
                    relativeLayout.setBackgroundResource(R.drawable.back);
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
            t1.setVisibility(View.VISIBLE);
        }
    }
    public void Check_Weather(View view){
        Getresult task=new Getresult();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+e1.getText().toString()+"&appid=YOUR API KEY");
    }

}
