package com.example.nav;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_showdata#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_showdata extends Fragment implements SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "MainActivity";

    private SensorManager sensorManager;
    private SensorManager sensorManager2;
    private SensorEventListener sensorEventListener;
    private SensorEventListener sensorEventListener2;

    private Sensor accelerometer, gyroscope;
    private TextView editText1, editText2, editText3, editText4, editText5, editText6, editText7;
    private TextView meditText_Timer;
    private EditText ediTextTemp;

    float[] movArray = new float[10];
    int count = 0, n = 0;
    double addition = 0, additionAcc = 0;

    private boolean isActivityTriggered = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_showdata.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_showdata newInstance(String param1, String param2) {
        fragment_showdata fragment = new fragment_showdata();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public fragment_showdata() {
        // Required empty public constructor
    }


    public void onCreate(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_showdata, container, false);
        View view = inflater.inflate(R.layout.fragment_showdata, container,
                false);
        Toast.makeText(getActivity(), "Data has been written to File", Toast.LENGTH_SHORT).show();
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        editText5 = view.findViewById(R.id.editText5);
        editText6 = view.findViewById(R.id.editText6);
        editText7 = view.findViewById(R.id.editText7);

        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        //Log.d(TAG, "onCreate: initializing sensor services");

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        return view;
    }


    public void saveData(float n1, float n2, float n3) {
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File roots = new File(filepath, "BOAtes");
        if (!roots.exists()) {
            roots.mkdirs();
        }
        try {
            File wrt = new File(roots, "text3.txt");
            FileWriter writer = new FileWriter(wrt, true);
            writer.append("  X:" + n1 + "   Y:" + n2 + "   Z:" + n3 + "\n");
            writer.flush();
            writer.close();
            Toast.makeText(getActivity(), "Data has been written to File", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // Toast.makeText(MainActivity.this, "Data has", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //TODO: get values
            //Log.d(TAG, "onSensorChanged: X :" + sensorEvent.values[0] + "Y:" + sensorEvent.values[1] + "Z:" + sensorEvent.values[2]);
            editText1.setText("" + sensorEvent.values[0]);
            editText2.setText("" + sensorEvent.values[1]);
            editText3.setText("" + sensorEvent.values[2]);
            //saveData(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
            // operationX(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            //TODO: get values
            editText4.setText("" + sensorEvent.values[0]);
            editText5.setText("" + sensorEvent.values[1]);
            editText6.setText("" + sensorEvent.values[2]);
            //saveData(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
            //Log.d(TAG, "onGyroChanged: X :" + sensorEvent.values[0] + "Y:" + sensorEvent.values[1] + "Z:" + sensorEvent.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}