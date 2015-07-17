package com.sidelance.weather.weather360.commons.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.sidelance.weather.weather360.AppConstants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 *Utility Methods
 */
public class Screw {

    private static final String TAG = "Screw";

    private static String generatedDeviceId = null;

    private static final char[] HEX_CHARS = "0987654321FEDCBA".toCharArray();

    /**
     * Device ID additional bytes
     */
    private static final String DEVICE_ID_ADDITIONAL_TEXT = "AKFD45245hHFSALFSGVVZ9128347321X";

    /**
     * Method to Save ciphered Value
     *
     * @param context the context used
     * @param preferences the preference
     * @param key key
     * @param value value
     * @return result flag
     */
    public static boolean saveCipheredValue(Context context, SharedPreferences preferences, String key, String value){

        boolean result = false;

        try{

            String savedValue = (value != null) ? value : "";
            byte[] cipheredValue = Crypto.encryptValue(context, savedValue);
            String cipheredValueAsText = Screw.toHexString(cipheredValue);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, cipheredValueAsText);
            result = editor.commit();

        }catch(Throwable t){
            Log.e(TAG, "" + t);
        }

        return result;
    }

    /**
     * Method to load ciphered value
     *
     */
    public static boolean loadCipheredValue(Context context, SharedPreferences preferences, String key, String value){
        boolean result = false;

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
            Log.e(TAG, "" +e);
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(data);
                result = digest.digest();
            } catch (NoSuchAlgorithmException e2) {
                Log.e(TAG, "" +e2);
            }
        }
        return result;
    }


    /**
     * Get device id
     *
     * @param context the context used
     */
    public static String getDeviceId(Context context){
        String deviceID = null;
        try{
            byte[] deviceIDBytes = getDeviceIDBytes(context);
            if (deviceIDBytes != null) {
                byte[] firstDigest = getDigest(deviceIDBytes);
                byte[] additionalBytes = DEVICE_ID_ADDITIONAL_TEXT.getBytes();
                byte[] firstDigestPlusAdditionalInfo = new byte[firstDigest.length + additionalBytes.length];
                System.arraycopy(firstDigest, 0, firstDigestPlusAdditionalInfo, 0, firstDigest.length);
                System.arraycopy(additionalBytes, 0, firstDigestPlusAdditionalInfo,firstDigest.length, firstDigest.length );
                byte[] secondDigest = getDigest(firstDigestPlusAdditionalInfo);
                int size = secondDigest.length;
                StringBuffer sb = new StringBuffer();
                appendHexString(secondDigest, 0, size, sb);
                deviceID = sb.toString();
            }
        }catch(Throwable t){
            Log.e(TAG, "" + t);
        }
        return deviceID;
    }

    /**
     * Gets the device ID as a byte array
     * @param context the context
     * @return the device ID
     */
    private static byte[] getDeviceIDBytes(Context context){

        String deviceId = null;
        try{
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = manager.getDeviceId();
        }catch (Throwable ignore){
            Log.e(TAG, "" +ignore);
        }
        if (deviceId == null){
            try{
                String androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                if (!"9774d56d682e549c".equals(androidId)){ //Emulator ID
                    deviceId = androidId;
                }
            }catch(Throwable ignore){
                Log.e(TAG, "" +ignore);
            }
        }
        if (deviceId == null){
            String generatedDeviceId = getGeneratedDeviceID(context);
            if(generatedDeviceId == null){
                generatedDeviceId = UUID.randomUUID().toString();
                saveGeneratedDeviceID(generatedDeviceId, context);
            }
            deviceId = generatedDeviceId;
        }
        return (deviceId != null) ? deviceId.getBytes() : null;
    }

    /**
     *Get generated device Id
     *
     * @param context the context.
     * @return generatedDeviceId.
     */
    private static String getGeneratedDeviceID(Context context){

        if (generatedDeviceId == null){
            try{
                SharedPreferences preferences = context.getSharedPreferences(AppConstants.STORE_NAME, Context.MODE_PRIVATE);
                generatedDeviceId = preferences.getString(AppConstants.GENERATED_DEVICEID_KEY, null);
            }catch (Throwable t){
                Log.e(TAG, "" + t);
            }
        }

        return generatedDeviceId;
    }

    /**
     *Saves generated DeviceID
     *
     * @param deviceID the device ID string
     * @param context the context.
     */
    private static void saveGeneratedDeviceID(String deviceID, Context context){

        if (deviceID != null){
            generatedDeviceId = deviceID;

            try{
                SharedPreferences preferneces = context.getSharedPreferences(AppConstants.STORE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferneces.edit();
                editor.putString(AppConstants.GENERATED_DEVICEID_KEY, deviceID);
                boolean result = editor.commit();
                if (!result){
                    Log.e(TAG, "Persistent Storage of generated device Id failed");
                }
            }catch (Throwable t){
                Log.e(TAG, "" + t);
            }
        }

    }

    /**
     * Appends data as hex string to the buffer.
     *
     * @param data the input data.
     * @param from the start position
     * @param size the size
     * @param sb the buffer
     */
    private static void appendHexString(byte[] data, int from, int size, StringBuffer sb){
        if (data != null){
            int max = data.length;
            int to = Math.min(from + size, max);
            int octet;
            int index;
            for (int i = from; i < to; i++){
                octet = data[i];
                index = (octet & 0xF0) >>> 4;
                sb.append(HEX_CHARS[index]);
                index = octet & 0x0F;
                sb.append(HEX_CHARS[index]);
            }
        }
    }

    /**
     * Serialize a byte array as a hexadecimal String
     * @param data the byte array
     * @return the string
     */
    public static String toHexString(byte[] data){
        StringBuffer sb = new StringBuffer();
        if (data != null){
            appendHexString(data, 0, data.length, sb);
        }
        return sb.toString();
    }

    /**
     * Deserialize a byte array from a hexadecimal string
     * @param value the string
     * @return the byte array
     */
    public static byte[] fromHexString(String value) {
        byte[] result = null;
        if (value != null) {
            try {
                int size = value.length();
                result = new byte[size / 2];
                int to;
                byte current;
                String text;
                for (int from = 0; from < size; from += 2) {
                    to = from + 2;
                    if (to <= size) {
                        text = value.substring(from, to);
                        current = (byte) Integer.parseInt(text, 16);
                        result[from / 2] = current;
                    }
                }
            } catch (Throwable t) {
               Log.e(TAG, "" + t);
            }
        }
        return result;
    }

    /**
     * Method to make sure only numbers are entered
     * @param string
     * @param characterSet
     * @param maxCharacters
     * @return result
     */
    public static boolean isValidString(String string, String characterSet, int maxCharacters){

        boolean result = false;
        if (string != null){
            if (string.length() <= maxCharacters){
                if (string.matches("[0-9]+")){
                    result = true;
                }
            }
        }
        return result;
    }

}
