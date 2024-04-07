package dhy.hkmu.weather.responsehandle;

import android.util.Log;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.Iterator;

public class CurrentWeatherHandler {
    public String districtName;
    public double tempMin;
    public double tempMax;
    public double currentTemp;
    public int humidity;
    public int pressure;

    public long sunRise;
    public long sunSet;
    public double lon;
    public double lat;


    public Double windSpeed;
    public int timeZone;

    public String description;
    public String icon;

    public CurrentWeatherHandler(String res) {
        Log.d("HAHA",res);

        try {

            JSONObject jsonObject = new JSONObject(res);
            districtName = jsonObject.getString("name");
            timeZone=jsonObject.getInt("timezone");

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();

                switch (key) {
                    case "coord":
                        JSONObject coordinateObject=jsonObject.getJSONObject("coord");
                        lon=coordinateObject.getDouble("lon");
                        lat=coordinateObject.getDouble("lat");

                        break;
                    case "weather":
                        JSONObject weatherObject = jsonObject.getJSONArray("weather").getJSONObject(0);
                         description= weatherObject.getString("description");
                         icon=weatherObject.getString("icon");

                        Log.d("HAHA", "description: " + icon);
//                        Log.d("HAHA", "city name: " + districtName);

                        break;

                    case "main":

                       tempMin =jsonObject.getJSONObject("main").getDouble("temp_min");
                       tempMax=jsonObject.getJSONObject("main").getDouble("temp_max");

                        Log.d("HAHA", "temp min: " + tempMin+"temp max: "+tempMax);//test
                       currentTemp=jsonObject.getJSONObject("main").getDouble("temp");
                       humidity=jsonObject.getJSONObject("main").getInt("humidity");
                       pressure=jsonObject.getJSONObject("main").getInt("pressure");
                        break;

                    case "sys":
                        sunRise =jsonObject.getJSONObject("sys").getLong("sunrise");
                        sunSet =jsonObject.getJSONObject("sys").getLong("sunset");
                        Log.d("CheckSunR", String.valueOf(sunRise));
                        Log.d("CheckSunS", String.valueOf(sunSet));


                        break;

                    case "wind":
                        //wind metric meter/sec Imperial: miles/hour
                        windSpeed =jsonObject.getJSONObject("wind").getDouble("speed");
                        break;
                }
            }

        } catch (JSONException e) {
            Log.e("HAHA", e.getMessage(),e);}

    }
}