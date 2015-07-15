package com.sidelance.weather.weather360.Commons.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *Utility Methods
 */
public class Tools {

    private static final String TAG = "Tools";


    public static boolean saveCipheredValue(Context context, SharedPreferences preferences, String key, String value){

        boolean result = false;

        try{

            String savedValue = (value != null) ? value : "";
            byte[] cipheredValue = Crypto.encryptValue(context, savedValue);

        }catch(){

        }

        return result;
    }
}
