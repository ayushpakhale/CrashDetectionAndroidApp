package com.example.nav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;


import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    public String name1, name2, contact1, contact2;


    private DrawerLayout drawer;


    private SensorManager sensorManager;
    private SensorManager sensorManager2;
    private SensorEventListener sensorEventListener;
    private SensorEventListener sensorEventListener2;
    private Sensor accelerometer, gyroscope;
    private TextView editText1, editText2, editText3, editText4, editText5, editText6, editText7;
    //TextView meditText_Timer;
    private EditText ediTextTemp;


    float[] movArray = new float[10];
    int count = 0, n = 0;
    double addition = 0, additionAcc = 0;
    public boolean isActivityTriggered;

    public boolean isButtonClicked=false;

    fragment_fullscreen fm = (fragment_fullscreen) getSupportFragmentManager().findFragmentById(R.id.linearLayout_invisible);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WAKE_LOCK}, 100);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 100);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 100);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 100);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.FOREGROUND_SERVICE}, 1);
        }
        statusCheck();



        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        isActivityTriggered = false;




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        Toast.makeText(this, "activity running", Toast.LENGTH_SHORT).show();
        loadData();
        checkContactsCondtion();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_profile()).commit();
                break;
            case R.id.nav_showdata:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_showdata()).commit();
                break;
            case R.id.nav_aboutus:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_aboutUs()).commit();
                break;
            case R.id.nav_contactus:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_contactUs()).commit();
                break;
            case R.id.nav_shareapp:
                shareAppIntent();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);



        return true;
    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }





    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //TODO: get values
            operationX(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
            Log.d(TAG, "onSensorChanged: x:" + sensorEvent.values[0] + " Y: " + sensorEvent.values[1] + " Z: " + sensorEvent.values[2]);
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            //TODO: get values
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void operationX(float n1, float n2, float n3) {

        if (count > 6) {
            count = 0;
        }
        movArray[count] = n1;
        count++;
        movArray[count] = n2;
        count++;
        movArray[count] = n3;
        count++;
        additionAcc = n1 + n2 + n3;
        additionAcc = additionAcc / 3;
        additionX();
    }

    public void additionX() {
        for (int i = 0; i < 10; i++) {
            addition = addition + movArray[i];
        }
        addition = addition / 10;
        checkCondition();
    }

    public void checkCondition() {
        if (additionAcc > addition + 5 || additionAcc < addition - 5) {
            if (isActivityTriggered == false) {
                // editText7.setText("Activity Triggered" + n);
                n++;
                isActivityTriggered = true;
                activityTriggered();
            }
        }
    }

    public void activityTriggered() {
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_fullscreen()).commit();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(constants.XSHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(constants.XCONTACT1, contact1);
        editor.putString(constants.XCONTACT2, contact2);
        editor.putString(constants.XNAME1, name1);
        editor.putString(constants.XNAME2, name2);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(constants.XSHARED_PREFS, MODE_PRIVATE);
        contact1 = sharedPreferences.getString(constants.XCONTACT1, "");
        contact2 = sharedPreferences.getString(constants.XCONTACT2, "");
        name1 = sharedPreferences.getString(constants.XNAME1, "");
        name2 = sharedPreferences.getString(constants.XNAME2, "");
    }

    public void checkContactsCondtion()
    {
        if( contact1 == "" || contact2 == "")
        {
            buildAlertMessageNoContacts();
        }
    }

    private void buildAlertMessageNoContacts()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Didn't add any contacts until now, do you want to add them? \n This app wont work without contacts.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_profile()).commit();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void shareAppIntent()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app at: https://www71.zippyshare.com/v/3y4CMyqC/file.html" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

}