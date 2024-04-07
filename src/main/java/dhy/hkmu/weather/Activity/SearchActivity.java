package dhy.hkmu.weather.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import dhy.hkmu.weather.Adapters.SearchForecastAdapters;
import dhy.hkmu.weather.AppCompat;
import dhy.hkmu.weather.Domains.SearchForecast;
import dhy.hkmu.weather.DrawerHandler;
import dhy.hkmu.weather.R;
import dhy.hkmu.weather.responsehandle.CurrentWeatherHandler;
import dhy.hkmu.weather.responsehandle.ForecastHandler;
import dhy.hkmu.weather.responsehandle.GeoLocationHandler;



public class SearchActivity extends AppCompat {
    DrawerHandler drawerHandler;
    EditText cityName,countryCode;
    DrawerLayout drawerLayout;
    private final String url="https://api.openweathermap.org/data/2.5/weather";
    private final String appId="4e321041ce1209f2e85130c6d552b8eb";
    String  forecastQ;
    private SharedPreferences sharedPreferences;
    private RecyclerView.Adapter adapterForecast;
    public String queryLanguage;
    public String symbol;
    DecimalFormat dfTo1 = new DecimalFormat("#.#");
    private RecyclerView recyclerView;
    public String description;
    public String queryUnits;
    public String picPath;
    public String forecastTemp;
    public String dayOfWeek;
    public double lon;
    public double lat;
    public String dateLanguage;
    public String geo_Q;
    String cityLang;
    String ctName;
    private final String url_forecast="https://api.openweathermap.org/data/3.0/onecall";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean engSelected=sharedPreferences.getBoolean("eng_selected",true);
        boolean cnTrSelected=sharedPreferences.getBoolean("cnTr_selected",true);
        boolean cnSimSelected=sharedPreferences.getBoolean("cnSim_Selected",true);
        if(engSelected){
            queryLanguage="";
            dateLanguage="en";
            cityLang="en";
        } else if (cnTrSelected) {
            queryLanguage="&lang=zh_tw";
            dateLanguage="zh-TW";
            cityLang="zh";
        } else if (cnSimSelected) {
            queryLanguage="&lang=zh_cn";
            dateLanguage="zh-CN";
            cityLang="zh";
        }else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("eng_selected",true);
            editor.apply();
        }

        boolean  metricSelected= sharedPreferences.getBoolean("metric_selected", true);
        boolean  imperialSelected= sharedPreferences.getBoolean("imperial_selected", true);

        if(metricSelected){
            symbol="°C";
            queryUnits="&units=metric";


        } else if (imperialSelected) {
            symbol="℉";
            queryUnits="&units=imperial";
        }else {SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("metric_selected",true);
            editor.apply();
        }


        EdgeToEdge.enable(this);
        drawerHandler=new DrawerHandler(this);
        drawerLayout=findViewById(R.id.drawer_layout);

    }

    public void searchWeather(View view) {
        String query="";
        EditText cityName = findViewById(R.id.input);
        String city = cityName.getText().toString().trim();
        TextView textResult=findViewById(R.id.textResult);
        geo_Q="";
        textResult.setText("");

        if (city.equals("")) {
            textResult.setText(R.string.empty_input);
        }

        else {
            query=url+"?q="+city+"&appid="+appId+queryUnits+queryLanguage;
//            http://api.openweathermap.org/geo/1.0/direct?q={city name},{state code},{country code}&limit={limit}&appid={API key}

        }


        StringRequest req = new StringRequest(Request.Method.GET, query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setResult(response);
                forecast(city);
                Log.d("Search666", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    int statusCode = error.networkResponse.statusCode;
                    if (statusCode == 404) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                                builder.setTitle(getString(R.string.title404));
                                builder.setMessage(getString(R.string.msg404));
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();}
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }});
                    }
                }
            }});

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);

    }

    public void forecast(String city){

        geo_Q="https://api.openweathermap.org/geo/1.0/direct?q="+city+"&limit=5&appid="+appId;

        StringRequest req_cityName= new StringRequest(Request.Method.GET, geo_Q, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Here Received!", response);
                try {
                    setCityName(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });


        forecastQ=url_forecast+"?lat="+lat+"&lon="+lon+"&exclude=minutely,hourly,alerts&appid="+appId+queryUnits+queryLanguage;
        StringRequest req_forecast = new StringRequest(Request.Method.GET, forecastQ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Forecast", response);
                setForecast(response);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req_cityName);
        requestQueue.add(req_forecast);


    }


    public void setCityName(String res) throws JSONException {
        Log.d("Here Received Too!", res);
        runOnUiThread(new Runnable() { //For Thread Exception
            @Override
            public void run() {
                GeoLocationHandler geoLocationHandler = new GeoLocationHandler();

                try {
                    geoLocationHandler.findCityName(res, cityLang);
                    ctName = geoLocationHandler.cityName;

                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                            builder.setTitle("Hahhhh!");
                            builder.setMessage(" Nice!! This Is A Place Where Its Name Cant Be Translated By OpenWeather!Plz Try Another");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();}
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }});
                }
                TextView updateLocation = findViewById(R.id.result);
                updateLocation.setText(ctName);
            }});
    }



    public void setResult(String res){
        CurrentWeatherHandler resultWeather=new CurrentWeatherHandler(res);
        lat=resultWeather.lat;
        lon=resultWeather.lon;
        runOnUiThread(new Runnable() { //For Thread Exception
            @Override
            public void run(){

//                String district=resultWeather.districtName;
//                TextView updateDistrict=findViewById(R.id.result);
//                updateDistrict.setText(district);

                double currentTemp= resultWeather.currentTemp;
                String tempInt = String.valueOf((int) Math.round(currentTemp))+symbol;
                TextView updateTemp=findViewById(R.id.result_temp);
                updateTemp.setText(tempInt);

                String icon=resultWeather.icon;
                ImageView weatherIcon=findViewById(R.id.weatherIcon);
                String path="p"+icon;
                int pathId = getResources().getIdentifier(path, "drawable", getPackageName());
                weatherIcon.setImageResource(pathId);

                String description=resultWeather.description;
                TextView updateDescription=findViewById(R.id.description);
                updateDescription.setText(description);

                TextView updateMaxTemp=findViewById(R.id.maxTemp);
                String maxTempMetric=dfTo1.format(resultWeather.tempMax)+symbol;
                updateMaxTemp.setText(maxTempMetric);

                TextView updateMaxH=findViewById(R.id.headerMax);
                TextView updateMinH=findViewById(R.id.headerMin);

                updateMaxH.setText(getString(R.string.max_temp));
                updateMinH.setText(getString(R.string.min_temp));

                TextView updateMinTemp=findViewById(R.id.minTemp);
                String minTempMetric=dfTo1.format(resultWeather.tempMin)+symbol;
                updateMinTemp.setText(minTempMetric);


                updateMaxH.setText(getString(R.string.min_temp));

            }

        });
    }


    public void setForecast(String res){
        ForecastHandler forecastHandler=new ForecastHandler(res);
        ArrayList<JSONObject> dailyInfo = forecastHandler.dailyInfoList;
        ArrayList<SearchForecast> items= new ArrayList<>();
        int timeOffset=forecastHandler.timeOffset;

        try {

            for (int i = 0; i < dailyInfo.size(); i++) {
                JSONObject info = dailyInfo.get(i);

                long dt = info.getLong("dt");
                double temp = info.getDouble("temp");
                String weatherIcon = info.getString("icon");
                description =info.getString("description");

                picPath="p"+weatherIcon;
                int rTemp=(int)temp;
                forecastTemp=String.valueOf(rTemp)+symbol;


                if(i==0){
                    i+=1;
                }
                if (i==1) {
                    dayOfWeek=getResources().getString(R.string.tomorrow);
                } else {

                    long localTime=(dt)*1000;
//                    long localTime = (dt + timeOffset) * 1000; // only for virtual machine
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.forLanguageTag(dateLanguage));
                    dayOfWeek = sdf.format(localTime);}


//                (String dayOfWeek, String forecastTemp, String picPath,String forecastDescription)
                items.add(new SearchForecast(dayOfWeek, forecastTemp, picPath, description));

                recyclerView = findViewById(R.id.forecast);

                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

                adapterForecast = new SearchForecastAdapters(items);
                recyclerView.setAdapter(adapterForecast);

                Log.d("HaHa", dayOfWeek);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);}
    }





//
//    private void initForecast(){
//        ArrayList<SearchForecast> items= new ArrayList<>();
//
//        recyclerView=findViewById(R.id.forecast);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        adapterForecast=new SearchForecastAdapters(items);
//        recyclerView.setAdapter(adapterForecast);}




    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        drawerHandler.handleOnPause();
    }

}
