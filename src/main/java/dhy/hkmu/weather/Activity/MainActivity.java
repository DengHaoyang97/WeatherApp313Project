package dhy.hkmu.weather.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import dhy.hkmu.weather.Adapters.MainForecastAdapters;
import dhy.hkmu.weather.AppCompat;
import dhy.hkmu.weather.Domains.MainForecast;
import dhy.hkmu.weather.DrawerHandler;
import dhy.hkmu.weather.R;
import dhy.hkmu.weather.responsehandle.CurrentWeatherHandler;
import dhy.hkmu.weather.responsehandle.ForecastHandler;
import dhy.hkmu.weather.responsehandle.GeoLocationHandler;


public class MainActivity extends AppCompat implements LocationListener {
    DrawerHandler drawerHandler;
    DrawerLayout drawerLayout;


    private final String url_current="https://api.openweathermap.org/data/2.5/weather";
    private final String url_forecast="https://api.openweathermap.org/data/3.0/onecall";
    private final String appId="8da0990bc66926637ea3b7ba6bb40a7e";

//    4e321041ce1209f2e85130c6d552b8eb
//      8da0990bc66926637ea3b7ba6bb40a7e

    public String dayOfWeek;
    public String forecastTemp;
    public String picPath;

    public String queryUnits;

    public String queryLanguage;

    public String symbol;

    public String dateLanguage;

    public String windSpeedUnit;
    public String pressureUnit;
    public String cityLang;


    String test;

    private LocationManager locationManager;
    Timer timer;
    DecimalFormat dfTo1 = new DecimalFormat("#.#");
    private ImageButton refreshButton;
    private Animation rotation;

    private RecyclerView.Adapter adapterForecast;
    private RecyclerView recyclerView;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean engSelected=sharedPreferences.getBoolean("eng_selected",true);
        boolean cnTrSelected=sharedPreferences.getBoolean("cnTr_selected",true);
        boolean cnSimSelected=sharedPreferences.getBoolean("cnSim_Selected",true);
        if(engSelected){
            queryLanguage="";
            dateLanguage="en";
            pressureUnit="hpa";
            cityLang="en";
        } else if (cnTrSelected) {
            queryLanguage="&lang=zh_tw";
            dateLanguage="zh-TW";
            pressureUnit="百帕斯卡";
            cityLang="zh";
        } else if (cnSimSelected) {
            queryLanguage="&lang=zh_cn";
            dateLanguage="zh-CN";
            pressureUnit="百帕斯卡";
            cityLang="zh";
        }else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("eng_selected",true);
            editor.apply();
            }


//        https://api.openweathermap.org/data/2.5/weather?lat=57&lon=-2.15&appid={API key}&units=metric
        //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}&lang={lang}
        //zh_cn Chinese Simplified zh_tw Chinese Traditional unit->lang
        boolean  metricSelected= sharedPreferences.getBoolean("metric_selected", true);
        boolean  imperialSelected= sharedPreferences.getBoolean("imperial_selected", true);


        if(metricSelected){
            symbol="°C";
            queryUnits="&units=metric";
            if(cnTrSelected){
                windSpeedUnit="米/秒";

            }
            else if(cnSimSelected){
                windSpeedUnit="米/秒";
            }
            else {
                windSpeedUnit="meter/sec";
            }
        }


         else if (imperialSelected) {
            symbol="℉";
            queryUnits="&units=imperial";
            if(cnTrSelected){
                windSpeedUnit="英里/小時";
            }
            else if(cnSimSelected){
                windSpeedUnit="英里/小时";
            }
            else {
                windSpeedUnit="miles/hour";
            }




        }else {SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("metric_selected",true);
                editor.apply();
        }


        initForecast();
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerHandler = new DrawerHandler(this);


        refreshButton = findViewById(R.id.refresh_button);
        rotation = AnimationUtils.loadAnimation(this, R.anim.rotation_anim);

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

//for night view
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            refreshButton.setColorFilter(ContextCompat.getColor(this, R.color.white));
        } else {
            refreshButton.setColorFilter(ContextCompat.getColor(this, R.color.black));
        }

//refresh btn
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(rotation);
                vibrator.vibrate(250);
                timer=null;
            }
        });

//location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }


    public void timeStamp(){
        SimpleDateFormat sdfUs = new SimpleDateFormat("dd-MMMM-yyy HH:mm", Locale.forLanguageTag(dateLanguage));

        String stamp="Updated: "+sdfUs.format(new Date());

        runOnUiThread(new Runnable() { //For Thread Exception
            @Override
            public void run() {
                TextView updatedTimeText= findViewById(R.id.updated_time);
                updatedTimeText.setText(stamp);
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    requestWeatherInfo(lat, lon);
                    timeStamp();
                }
            }, 0, 600000); // ms
        }
    }


    public void requestWeatherInfo(double lat,double lon) {
        String query_current=url_current+"?lat="+lat+"&lon="+lon+"&appid="+appId+queryUnits+queryLanguage;
        String query_forecast=url_forecast+"?lat="+lat+"&lon="+lon+"&exclude=minutely,hourly,alerts&appid="+appId+queryUnits+queryLanguage;
//        api.openweathermap.org/data/2.5/forecast/daily?q={city name},{state code},{country code}&cnt={cnt}&appid={API key}

        StringRequest req_forecast = new StringRequest(Request.Method.GET, query_forecast, new Response.Listener<String>() {
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


        StringRequest req_current = new StringRequest(Request.Method.GET, query_current, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Current", response);
                setCurrent(response);


            }
        }
        ,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();}
        });
//        http://api.openweathermap.org/geo/1.0/reverse?lat={lat}&lon={lon}&limit={limit}&appid={API key}
        StringRequest req_cityName = new StringRequest(Request.Method.GET, "https://api.openweathermap.org/geo/1.0/reverse?lat="+lat+"&lon="+lon+"&limit=1&appid="+appId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CityNames", response);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req_cityName);
        requestQueue.add(req_current);
        requestQueue.add(req_forecast);

    }



    public void setCityName(String res) throws JSONException {
        runOnUiThread(new Runnable() { //For Thread Exception
            @Override
            public void run() {
        GeoLocationHandler geoLocationHandler=new GeoLocationHandler();
                try {
                    geoLocationHandler.findCityName(res,cityLang);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                TextView updateLocation= findViewById(R.id.location);
                updateLocation.setText(geoLocationHandler.cityName);}});
    }




public void setCurrent(String res){
    CurrentWeatherHandler jsHandler=new CurrentWeatherHandler(res);
    runOnUiThread(new Runnable() { //For Thread Exception
        @Override
        public void run() {

            String description=jsHandler.description;
            TextView updateDescription=findViewById(R.id.description);
            updateDescription.setText(description);


            int currentTemp=(int)jsHandler.currentTemp;
            String tempToShowMetric=String.valueOf(currentTemp)+symbol;
            TextView updateCurrentTemp=findViewById(R.id.result_temp);
            updateCurrentTemp.setText(tempToShowMetric);


            TextView updateMaxTemp=findViewById(R.id.maxTemp);
            String maxTempMetric=dfTo1.format(jsHandler.tempMax)+symbol;
            updateMaxTemp.setText(maxTempMetric);

            TextView updateMinTemp=findViewById(R.id.minTemp);
            String minTempMetric=dfTo1.format(jsHandler.tempMin)+symbol;
            updateMinTemp.setText(minTempMetric);

            int offSet= jsHandler.timeZone;

            long utcSunriseTime= jsHandler.sunRise;
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.forLanguageTag(dateLanguage));

            long unixSunriseTime=(utcSunriseTime)*1000;
//            long unixSunriseTime=(utcSunriseTime+offSet)*1000; //only work in virtual machine
            String LocalSunriseTime = format.format(new Date(unixSunriseTime));
            TextView updateSunrise=findViewById(R.id.sunrise_time);
            updateSunrise.setText(LocalSunriseTime);

            long utcSunsetTime= jsHandler.sunSet;
            long unixSunsetTime=(utcSunsetTime)*1000;
//            long unixSunsetTime=(utcSunsetTime+offSet)*1000; //only work in virtual machine
            String LocalSunsetTime = format.format(new Date(unixSunsetTime));
            TextView updateSunset=findViewById(R.id.sunset_time);
            updateSunset.setText(LocalSunsetTime); //finally.................



            String windSpeedMetric=String.valueOf(jsHandler.windSpeed)+windSpeedUnit;
            TextView updateWindSpeed=findViewById(R.id.wind_speed);
            updateWindSpeed.setText(windSpeedMetric);

            String pressure=String.valueOf(jsHandler.pressure)+pressureUnit;
            TextView updatePressure=findViewById(R.id.pressure_value);
            updatePressure.setText(pressure);

            String humidity=String.valueOf(jsHandler.humidity)+"%";
            TextView updateHumidity=findViewById(R.id.humidity_value);
            updateHumidity.setText(humidity);

        }});
    }

    public void setForecast(String res){
        ForecastHandler forecastHandler=new ForecastHandler(res);
        ArrayList<JSONObject> dailyInfo = forecastHandler.dailyInfoList;
        ArrayList<MainForecast> items= new ArrayList<>();
        int timeOffset=forecastHandler.timeOffset;

        try {

            for (int i = 0; i < dailyInfo.size(); i++) {
                JSONObject info = dailyInfo.get(i);

                long dt = info.getLong("dt");
                double temp = info.getDouble("temp");
                String weatherIcon = info.getString("icon");

                picPath="p"+weatherIcon;
                int rTemp=(int)temp;
                forecastTemp=String.valueOf(rTemp)+symbol;

                if(i==0){
//
//                    long localTime=(dt+timeOffset)*1000;
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm", Locale.ENGLISH);
//                    dayOfWeek=sdf.format(localTime);
//                    Log.d("Today", dayOfWeek);
                    dayOfWeek=getResources().getString(R.string.today);
                } else if (i==1) {
                    dayOfWeek=getResources().getString(R.string.tomorrow);
                } else {

                    long localTime=(dt)*1000;
//                    long localTime=(dt+timeOffset)*1000; // only for virtual machine
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.forLanguageTag(dateLanguage));
                    dayOfWeek=sdf.format(localTime);
               }

                items.add(new MainForecast(dayOfWeek,forecastTemp,picPath));
                recyclerView=findViewById(R.id.forecast);
                recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

                adapterForecast=new MainForecastAdapters(items);
                recyclerView.setAdapter(adapterForecast);

                Log.d("HaHa", dayOfWeek);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);}
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}//dont delete
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}



    private void initForecast(){
        ArrayList<MainForecast> items= new ArrayList<>();


        items.add(new MainForecast("ahh?","23","p10d"));
        items.add(new MainForecast("ahh?","97","p03d"));
        items.add(new MainForecast("ahh?","10","p01d"));
        items.add(new MainForecast("ahh?","22","p04d"));
        items.add(new MainForecast("ahh?","30","p09d"));

        recyclerView=findViewById(R.id.forecast);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        adapterForecast=new MainForecastAdapters(items);
        recyclerView.setAdapter(adapterForecast);}


    public void showAlertDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(666);


        builder.setTitle("About Us");
        builder.setMessage(getString(R.string.aboutUsMsg));
        builder.setPositiveButton(" O(∩_∩)O   All The Best   O(∩_∩)O", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }



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
        timer=null;
    }
}