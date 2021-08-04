package com.example.nav;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_fullscreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_fullscreen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_fullscreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_fullscreen.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_fullscreen newInstance(String param1, String param2) {
        fragment_fullscreen fragment = new fragment_fullscreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    private TextView meditTextTimer;
    private Button buttonResetTimer;
    private EditText editTextTemp;
    private static long starttime_in_millis = 60000;
    private CountDownTimer mcountDownTimer;
    private boolean mTimerRunning = false;
    private long mTimeLeftInMillis = starttime_in_millis;
    private boolean smssent = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double lat, lon;
    double latitude, longitude;
    String s, ss;
    MediaPlayer player;
    //private boolean isButtonClicked = ((MainActivity) getActivity()).isButtonClicked;


   /* MainActivity isAc =(MainActivity)getActivity();
    private boolean isActivityTriggered = false;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fullscreen, container, false);


        meditTextTimer = view.findViewById(R.id.editText_Timer);
        buttonResetTimer = view.findViewById(R.id.buttonResetTimer);


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(constants.sendlat));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(nMessageReceiver, new IntentFilter(constants.sendlon));


        buttonResetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
        if (((MainActivity) getActivity()).isButtonClicked) {
            startTimer();
        }else{
            playSound();
            startTimer();
        }
        return view;
    }

    public void editTextTimer(String text) {
        meditTextTimer.setText(text);
    }

    public void startTimer() {
        if (!isLocationServiceRunning()) {
            startLocationService();
        }

        if (!mTimerRunning) {
            mTimeLeftInMillis = starttime_in_millis;
        }

        mcountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                sosTriggered();
                stopSound();
            }
        }.start();
        mTimerRunning = true;
    }

    public void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int hours = minutes / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        meditTextTimer.setText(timeLeftFormatted);
        Log.d(TAG, "startTimer: inside update count down timer" + timeLeftFormatted);

    }

    public void resetTimer() {
        mTimeLeftInMillis = starttime_in_millis;
        ((MainActivity) getActivity()).isActivityTriggered = false;
        mcountDownTimer.cancel();
        mTimerRunning = false;
        updateCountDownText();
        stopLocationService();
        stopSound();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit();
    }

    public void sendSMS(String lat, String lon) {
        ((MainActivity) getActivity()).loadData();
        String phn1 = ((MainActivity) getActivity()).contact1;
        String phn2 = ((MainActivity) getActivity()).contact2;
        Log.d(TAG, "phn 1=" + phn1 + "phn2 = " + phn2);
        String msg = "Hey There, I think i might be in trouble, my location https://www.google.com/maps/search/" + lat + "++" + lon;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phn1, null, msg, null, null);
        smsManager.sendTextMessage(phn2, null, msg, null, null);
        Toast.makeText(getActivity(), "sms sent successfully", Toast.LENGTH_SHORT);
    }

    public void sosTriggered() {
        smssent = true;
        Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG);
        // editTextTemp.append("latitude   " + s + "Longitude  " + ss);
        Log.d(TAG, "activityTriggered: latitude = " + s + "longitude = " + ss);
        sendSMS(s, ss);
    }


    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {

                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getActivity().getApplicationContext(), LocationService.class);
            intent.setAction(constants.ACTION_START_LOCATION_SERVICE);
            getActivity().startService(intent);
            Toast.makeText(getActivity(), "Location Services Running", Toast.LENGTH_LONG).show();
        }
    }

    private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getActivity().getApplicationContext(), LocationService.class);
            intent.setAction(constants.ACTION_STOP_LOCATION_SERVICE);
            getActivity().startService(intent);
            Toast.makeText(getActivity(), "Location Services Stopped", Toast.LENGTH_LONG).show();
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            double lt = 0;
            Double message = intent.getDoubleExtra("key", lt);
            s = message.toString();
        }
    };

    private BroadcastReceiver nMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            double lo = 0;
            Double message = intent.getDoubleExtra("key1", lo);
            ss = message.toString();
        }
    };


    private void playSound() {
        if (player == null) {
            player = MediaPlayer.create(getActivity(), R.raw.alert_sound);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playSound();
                }
            });
        }
        player.start();
    }

    private void pauseSound() {

        if (player != null) {
            player.pause();
        }
    }

    private void stopSound() {
        if (player != null) {
            player.release();
            player = null;
            Toast.makeText(getActivity(), "Released", Toast.LENGTH_SHORT).show();
        }
    }

    //Experimental
    @Override
    public void onStop() {
        super.onStop();
        stopSound();
    }
}