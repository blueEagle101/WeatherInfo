package com.homecredit.weatherinfo;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {


    String API_key = "6710122dfcc0fa0b99618b4689240aa9";
    String vURL = "http://api.openweathermap.org/data/2.5/forecast?id=1270260&APPID=6710122dfcc0fa0b99618b4689240aa9";

    String[] cities = new String[] {"2643743","3067696","1689973"};
    String[] data = new String[] {"","",""};

    ArrayList<Weather> weatherList = new ArrayList<>();

    ListView lView;

    TextView txtRes;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        lView = (ListView)findViewById(R.id.listView);

        for(int i = 0; i < cities.length; i++){

            String nUrl = "http://api.openweathermap.org/data/2.5/forecast?id="+cities[i].trim()+"&APPID="+API_key;

            new weatherTask().execute(nUrl, data[i]);

        }

    }

    class weatherTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            String pUrl = params[0];
            String pData = params[1];

            try {

                URL mUrl = new URL(pUrl);
                HttpURLConnection httpConnection = (HttpURLConnection) mUrl.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.setRequestProperty("Content-type", "application/json");

                httpConnection.connect();

                int responseCode = httpConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    pData =  sb.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Log.d("ldata..", "pData: " + pData);
            return pData;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            counter++;
                Log.d("ldata" , "exec: " + result);
                processData(result);

        }
    }

    private void processData(String lData){

        Log.d("lData...", "lData: " + lData);

        weatherList = new ArrayList<>();
        
            try{

                JSONObject jsonObject = new JSONObject(lData);

                String jLocation = jsonObject.getJSONObject("city").getString("name");
                String jTemp = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp");
                String jWeather = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("weather").getString("main");

                Weather weather = new Weather();
                weather.setLocation(jLocation);
                weather.setTemperature(jTemp);
                weather.setWeather(jWeather);

                weatherList.add(weather);


            }catch (JSONException e){
                e.printStackTrace();
            }

            ArrayAdapter<Weather> adapter = new ArrayAdapter<Weather>(this.getApplicationContext(), R.layout.list_item, weatherList);
            lView.setAdapter(adapter);



    }

    public class MyAdapter extends ArrayAdapter {

        public MyAdapter(Context context, int resource, ArrayList<Weather> data) {
            super(context, resource, data);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent);

            TextView txtLocation = (TextView)convertView.findViewById(R.id.wLocation);
            TextView txtWeather = (TextView)convertView.findViewById(R.id.wWeather);
            TextView txtTemp = (TextView)convertView.findViewById(R.id.wTemp);

            return super.getView(position, convertView, parent);
        }
    }

}
