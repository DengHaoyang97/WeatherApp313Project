package dhy.hkmu.weather.responsehandle;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GeoLocationHandler {
    public Double lat;
    public Double lon;
    public String cityName;



    public void findCityName(String res, String language) throws JSONException {
        Log.d("Here Received Three!", res);
        JSONArray jsonArray=new JSONArray(res);
        JSONObject cityNames=jsonArray.getJSONObject(0).getJSONObject("local_names");
        cityName=cityNames.getString(language);

//
//    public void findCoordinates(String res) throws JSONException {
//        JSONArray jsonArray=new JSONArray(res);
//        lat= jsonArray.getJSONObject(0).getDouble("lat");
//        lon= jsonArray.getJSONObject(0).getDouble("lon");
//    }
//
////    http://api.openweathermap.org/geo/1.0/direct?q={city name},{state code},{country code}&limit={limit}&appid={API key}
////    http://api.openweathermap.org/geo/1.0/reverse?lat={lat}&lon={lon}&limit={limit}&appid={API key}
////    [{"name":"Hong Kong Island","local_names":{"ka":"ჰონგ-კონგი","bg":"Хонконг","fi":"Hongkongin saari","sv":"Hongkongön","eo":"Honkonga Insulo","vi":"Đảo Hồng Kông","et":"Hongkongi saar","ms":"Pulau Hong Kong","ml":"ഹോങ്കോങ്","hu":"Hongkong-sziget","mk":"Хонгконг","uk":"Гонконг","eu":"Hong Kong uhartea","zh":"香港島","ur":"جزیرہ ہانگ کانگ","sr":"Хонгконг","he":"הונג קונג","my":"ဟောင်ကောင်ကျွန်း","lv":"Honkonga","ja":"香港島","id":"Pulau Hong Kong","da":"Hong Kong Island","ko":"홍콩섬","gl":"Illa de Hong Kong","la":"Insula Hongcongensis","ru":"Гонконг","ta":"ஒங்கொங் தீவு","br":"Enez Hong Kong","hi":"हाँगकाँग द्वीप","be":"Ганконг","ca":"Illa de Hong Kong","kn":"ಹಾಂಗ್ಕಾಂಗ್","th":"เกาะฮ่องกง","pt":"Ilha de Hong Kong","fr":"Île de Hong Kong","ar":"جزيرة هونغ كونغ","ku":"Hong Kong","de":"Hong Kong Island","no":"Hongkongøya","es":"Isla de Hong Kong","en":"Hong Kong Island","it":"Hong Kong","fa":"جزیره هنگ کنگ","nl":"Hongkong","lt":"Honkongas","el":"Χονγκ Κονγκ","tl":"Pulo ng Hong Kong","oc":"Hongkong","bn":"হংকং দ্বীপ","pl":"Hongkong","cs":"Hongkong","tr":"Hong Kong Adası"},"lat":22.2793278,"lon":114.1628131,"country":"CN","state":"Hong Kong"}]
//


    }

}






