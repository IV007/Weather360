package com.sidelance.weather.weather360.Commons.tools;

import android.content.Context;

import com.sidelance.weather.weather360.AppConstants;

/**
 * Class used for Encryption and Decryption of texts
 */
public class Crypto {

    private static final byte[] LOCK = new byte[]{(byte) 0XFB, (byte) 0xFC, (byte) 0xFA, (byte) 0xFD};

    /**
     * Inner Encryption Key.
     * */
    private static byte[] INNER_KEY;

    /**
     * Initial Vector
     * */
    private static final byte[] IV = new byte[]{
            (byte) 0x85, (byte) 0x4f, (byte) 0xec, (byte) 0x10, (byte) 0xa4, (byte) 0x22, (byte) 0xca, (byte) 0xfd
    };


    public static byte[] retrieveInnerCryptoKey(Context context){

        byte[] key = null;

        try{
            String deviceId = Tools.getDeviceId(context);
            byte[] scrt = AppConstants.
        }
    }

}
