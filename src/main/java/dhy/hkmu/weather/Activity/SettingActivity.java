package dhy.hkmu.weather.Activity;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.View;
import android.os.Bundle;

import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Locale;

import dhy.hkmu.weather.AppCompat;
import dhy.hkmu.weather.DrawerHandler;
import dhy.hkmu.weather.LanguageManager;
import dhy.hkmu.weather.R;


public class SettingActivity extends AppCompat {                                                                     //public class头痛
    DrawerHandler drawerHandler;
    TextView txtResult;
    DrawerLayout drawerLayout;

    private RadioButton metricButton;
    private RadioButton imperialButton;
    private RadioButton eng_Set;
    private RadioButton cnTr_set;
    private RadioButton cnSim_set;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        LanguageManager lang=new LanguageManager(this);

        eng_Set=findViewById(R.id.eng_set);
        cnTr_set=findViewById(R.id.cnTr_set);
        cnSim_set=findViewById(R.id.cnSimp_set);

        metricButton=findViewById(R.id.metric_set);
        imperialButton=findViewById(R.id.imperial_set);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean engSelected=sharedPreferences.getBoolean("eng_selected",true);
        boolean cnTrSelected=sharedPreferences.getBoolean("cnTr_selected",true);
        boolean cnSimSelected=sharedPreferences.getBoolean("cnSim_Selected",true);

        if(engSelected){
            eng_Set.setChecked(true);
            eng_Set.setEnabled(false);

            cnTr_set.setChecked(false);
            cnTr_set.setEnabled(true);

            cnSim_set.setChecked(false);
            cnSim_set.setEnabled(true);

        }else if(cnTrSelected){
            eng_Set.setChecked(false);
            eng_Set.setEnabled(true);

            cnTr_set.setChecked(true);
            cnTr_set.setEnabled(false);

            cnSim_set.setChecked(false);
            cnSim_set.setEnabled(true);

        }else if(cnSimSelected){

            eng_Set.setChecked(false);
            eng_Set.setEnabled(true);


            cnTr_set.setChecked(false);
            cnTr_set.setEnabled(true);

            cnSim_set.setChecked(true);
            cnSim_set.setEnabled(false);
        }


        eng_Set.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lang.updateResource("en");
                recreate();

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("eng_selected",true);
                editor.putBoolean("cnTr_selected",false);
                editor.putBoolean("cnSim_Selected",false);
                editor.apply();}
        });
        cnTr_set.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lang.updateResource("zh");
                recreate();

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("eng_selected",false);
                editor.putBoolean("cnTr_selected",true);
                editor.putBoolean("cnSim_Selected",false);
                editor.apply();}
        });
        cnSim_set.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lang.updateResource("es");
                recreate();

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("eng_selected",false);
                editor.putBoolean("cnTr_selected",false);
                editor.putBoolean("cnSim_Selected",true);
                startActivity(getIntent());
                editor.apply();}

        });


        boolean  metricSelected= sharedPreferences.getBoolean("metric_selected", true);
        if (metricSelected) {
            metricButton.setChecked(true);
            metricButton.setEnabled(false);

            imperialButton.setChecked(false);
            imperialButton.setEnabled(true);
        } else {
            metricButton.setChecked(false);
            metricButton.setEnabled(true);

            imperialButton.setChecked(true);
            imperialButton.setEnabled(false);
        }




        metricButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("metric_selected", true);
                editor.putBoolean("imperial_selected", false);
                editor.apply();

//                metricButton.setChecked(true);
//                metricButton.setEnabled(false);
//
//                imperialButton.setChecked(false);
//                imperialButton.setEnabled(true);
            }
        });

        imperialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("metric_selected", false);
                editor.putBoolean("imperial_selected", true);
                editor.apply();

//                metricButton.setChecked(false);
//                metricButton.setEnabled(true);
//
//                imperialButton.setChecked(true);
//                imperialButton.setEnabled(false);
            }
        });

        EdgeToEdge.enable(this);
        drawerHandler=new DrawerHandler(this);
        drawerLayout=findViewById(R.id.drawer_layout);

    }
}







