package dhy.hkmu.weather.responsehandle;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class ForecastHandler {

    public ArrayList<JSONObject> dailyInfoList;
    public int timeOffset;


    //    https://api.openweathermap.org/data/3.0/onecall?lat=33.44&lon=-94.04&exclude=hourly,daily&appid={API key}
    public ForecastHandler(String res) {
        handleJSON(res);
    }

    public void handleJSON(String res) {

        try {
            JSONObject jsonObject = new JSONObject(res);
            Iterator<String> keys = jsonObject.keys();
            timeOffset=jsonObject.getInt("timezone_offset");

            while (keys.hasNext()) {
                String key = keys.next();

                switch (key) {

                    //                case "current":
                    //                  break;
                    //                case "hourly":
                    //                  break;

                    case "daily":
                        JSONArray dailyArray = jsonObject.getJSONArray("daily");

                        dailyInfoList = new ArrayList<>();

                        for (int i = 0; i < dailyArray.length(); i++) {
                            JSONObject dailyObject = dailyArray.getJSONObject(i);
                            long dt = dailyObject.getLong("dt");
                            double tempDay = dailyObject.getJSONObject("temp").getDouble("day");
                            String weatherIcon = dailyObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                            String description=dailyObject.getJSONArray("weather").getJSONObject(0).getString("description");

                            JSONObject infoObject = new JSONObject();
                            infoObject.put("dt", dt);
                            infoObject.put("temp", tempDay);
                            infoObject.put("icon", weatherIcon);
                            infoObject.put("description",description);
                            dailyInfoList.add(infoObject);

                            Log.d("WAH", "dt: " + dt);
                            Log.d("WAH", "temp: " + tempDay);
                            Log.d("WAH", "icon: " + weatherIcon);
                        }
//                        dailyInfo(dailyInfoList);
                        break;
                }
            }
        } catch (JSONException e) {
            Log.e("WAH", e.getMessage(), e);
        }
    }

//    private void dailyInfo(ArrayList<JSONObject> dailyInfoList) {
//       try {
//
//
//        for(int i=0;i<dailyInfoList.size();i++){
//
//            JSONObject info = dailyInfoList.get(0);
//            long dt = info.getLong("dt");
//            double tempDay=info.getDouble("tempDay");
//            String weatherIcon=info.getString("weatherIcon");
//
//            Log.d("WAH", String.valueOf(dt));
//
//        }
//        Log.d("HAHA", String.valueOf(dailyInfoList.size()));
//       }
//       catch (JSONException e) {
//           Log.e("WAH", e.getMessage(),e);}
//    }

}
