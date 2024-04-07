package dhy.hkmu.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LanguageManager {
    private SharedPreferences sharedPreferences;
    private Context ct;
    public LanguageManager(Context ctx){
        ct=ctx;
        sharedPreferences=ct.getSharedPreferences("Lang",Context.MODE_PRIVATE);
    }

    public void updateResource(String code){
        Locale locale=new Locale(code);

        Resources resources=ct.getResources();
        Configuration configuration=resources.getConfiguration();
        configuration.locale=locale;
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        setLang(code);
    }

    public String getLang(){
        return sharedPreferences.getString("lang","en");
    }


    public void setLang(String code){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("lang",code);
        editor.apply();

    }
}
