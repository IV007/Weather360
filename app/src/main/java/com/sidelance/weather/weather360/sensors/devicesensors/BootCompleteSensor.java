package com.sidelance.weather.weather360.sensors.devicesensors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

/**
 * Service Helper tracks various device states.
 */

public class BootCompleteSensor extends BroadcastReceiver {

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent
     * broadcast.
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast messageOne = Toast.makeText(context, "INITIALIZING 95%... ", Toast.LENGTH_SHORT);
        messageOne.show();

    }

}
