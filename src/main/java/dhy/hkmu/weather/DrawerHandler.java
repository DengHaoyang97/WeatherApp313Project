package dhy.hkmu.weather;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import dhy.hkmu.weather.Activity.MainActivity;
import dhy.hkmu.weather.Activity.SearchActivity;
import dhy.hkmu.weather.Activity.SettingActivity;

public class DrawerHandler {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    public DrawerHandler(AppCompatActivity activity){
        drawerLayout=activity.findViewById(R.id.drawer_layout);
        navigationView=activity.findViewById(R.id.nav_view);
        toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    Toast.makeText(activity, "Home Selected", Toast.LENGTH_SHORT).show();
                        if (!(activity instanceof MainActivity)) {
                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);
                        }
                        else {drawerLayout.closeDrawer(GravityCompat.START);}
                    }

                else if (itemId == R.id.find) {
                    Toast.makeText(activity, "Search Selected", Toast.LENGTH_SHORT).show();
                        if (!(activity instanceof SearchActivity)) {
                            Intent intent = new Intent(activity, SearchActivity.class);
                            activity.startActivity(intent);
                        }
                        else {drawerLayout.closeDrawer(GravityCompat.START);}
                }

                else if (itemId == R.id.settings) {
                    Toast.makeText(activity, "Settings Selected", Toast.LENGTH_SHORT).show();
                    if (!(activity instanceof SettingActivity)) {
                        Intent intent = new Intent(activity, SettingActivity.class);
                        activity.startActivity(intent);
                    } else {drawerLayout.closeDrawer(GravityCompat.START);}
                }

                return false;
            }
        });
    }
    public void handleOnPause() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}
