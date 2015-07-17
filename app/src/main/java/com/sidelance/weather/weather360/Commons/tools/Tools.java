package com.sidelance.weather.weather360.Commons.tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    /**
     * Gets the a digest from data
     * @param data the input data
     * @return the digest
     */
    public static byte[] getDigest(byte[] data) {
        byte[] result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            digest.update(data);
            result = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            Tools.logThrowable(TAG, e);
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(data);
                result = digest.digest();
            } catch (NoSuchAlgorithmException e2) {
                Tools.logThrowable(TAG, e2);
            }
        }
        return result;
    }

}
