package com.salesmanagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Location();
        this.registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);



            Toast.makeText(context, "level - "+level, Toast.LENGTH_SHORT).show();

        }
    };

    void Location(){
        TrackGPS gps = new TrackGPS(MainActivity.this);


        if(gps.canGetLocation()){


            double longitude = gps.getLongitude();
            double latitude = gps.getLatitude();

            Toast.makeText(getApplicationContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude),Toast.LENGTH_SHORT).show();
        }
        else
        {

            gps.showSettingsAlert();
        }

    }

    private void turnGPSOff(){

        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        turnGPSOff();
    }

    @Override
    public boolean isDestroyed() {
        turnGPSOff();
        return super.isDestroyed();

    }

    @Override
    protected void onPause() {
        super.onPause();
        turnGPSOff();
    }


}