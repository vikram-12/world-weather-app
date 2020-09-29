package com.example.worldweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editText);
        resultTextView=findViewById(R.id.resultTextView);

    }
    public void getWeather(View view) throws UnsupportedEncodingException {

        DownloadTask task = new DownloadTask();

        task.execute("https://openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&appid=439d4b804bc8187953eb36d2a8c26a02");
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpsURLConnection urlConnection=null;
            try {
                url=new URL(urls[0]);
                urlConnection=(HttpsURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONObject jsonObject=new JSONObject(s);
                String weatherInfo=jsonObject.getString("weather");
                Log.i("weather content",weatherInfo);
                JSONArray arr=new JSONArray(weatherInfo);
                String message="";
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonPart=arr.getJSONObject(i);
                    String main=jsonPart.getString("main");
                    String description=jsonPart.getString("description");
                   if(!main.equals("")&&!description.equals("")){
                       message +=main + ": " + "\n"+description +"\r\n";
                   }
                }
                if(!message.equals("")){
                    resultTextView.setText(message);
                }
            }catch (Exception e){
                e.printStackTrace();


            }
        }
}
}
