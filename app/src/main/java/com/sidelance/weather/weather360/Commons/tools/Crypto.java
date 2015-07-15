package com.sidelance.weather.weather360.Commons.tools;

import android.content.Context;
import android.util.Log;

import com.sidelance.weather.weather360.AppConstants;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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
            byte[] scrt = AppConstants.APP_SCRT.getBytes();
            if ((deviceId != null) && (scrt != null)) {
                byte[] deviceIdBytes = deviceId.getBytes();
                byte[] toBeHashed = new byte[deviceIdBytes.length + scrt.length];
                System.arraycopy(deviceIdBytes, 0, toBeHashed, 0, deviceIdBytes.length);
                System.arraycopy(scrt, 0, toBeHashed, deviceIdBytes.length, scrt.length);
                byte[] digest = Tools.getDigest(toBeHashed);
                if (digest != null) {
                    key = new byte[16];
                    System.arraycopy(digest, 0, key, 0, 16);
                }
            }

        }catch (Throwable t){
            Log.e("Crypto", "" + t);
        }
        return key;
    }

    /**
     * Encrypt any text
     *
     * @param context The context to obtain the device ID (from IMEI)
     * @param plainText the input plain text
     * @return the encrypted text
     * @throws GeneralSecurityException on cryptography failure
     * @throws IOException on input/output failure
     * */
    public static byte[] encryptValue(Context context, String plainText) throws IOException, GeneralSecurityException{
        String contextText = plainText.trim();
        byte[] content = contextText.getBytes("ASCII");
        byte[] ciphered = encrypt(context, content);
        return ciphered;
    }

    /**
     * Encrypt any text
     *
     * @param context The context to obtain the device ID (from IMEI)
     * @param encryptedText the encrypted input text
     * @return the encrypted text
     * @throws GeneralSecurityException on cryptography failure
     * @throws IOException on input/output failure
     * */
    public static String decryptValue(Context context, byte[] encryptedText) throws IOException, GeneralSecurityException{
        byte[] plainBytes = decrypt(context, encryptedText);
        String plainText = new String(plainBytes, "ASCII");
        plainText = plainText.trim();
        return  plainText;

    }

    /**
     * Encrypt a text
     * @param context The context to obtain the device ID(from IMEI)
     * @param content the input content
     * @return the encrypted text
     * @throws GeneralSecurityException on cryptography failure
     * @throws IOException on input/output failure
     */
    public static byte[] encrypt(Context context, byte[] content) throws IOException, GeneralSecurityException {

        if (INNER_KEY == null) {
            INNER_KEY = retrieveInnerCryptoKey(context);
        }

        int prfxSize = LOCK.length;
        int contentSize = content.length;
        int size = prfxSize + contentSize;

        int blocks = size / 8;
        if (size % 8 > 0) {
            blocks++;
        }
        int messageSize = blocks *8;
        byte[] m = new byte[messageSize];
        int padLen = messageSize - size;
        for (int i = padLen; i < messageSize; i++) {
            m[i] = 32;
        }

        System.arraycopy(LOCK, 0, m, 0, prfxSize);
        System.arraycopy(content, 0, m, prfxSize, contentSize);

        byte[] cipheredContent = new byte[messageSize];
        tripleDESCBCEncryption(INNER_KEY, IV, m, cipheredContent, messageSize);

        return cipheredContent;

    }

    /**
     * Decrypt a encrypted text
     * @param context The context
     * @param encrypted the encrypted input text
     * @return the decrypted text
     * @throws GeneralSecurityException on cryptography failure
     * @throws IOException on input/output failure
     */
    public static byte[] decrypt(Context context, byte[] encrypted) throws GeneralSecurityException, IOException {
        if (INNER_KEY == null) {
            INNER_KEY = retrieveInnerCryptoKey(context);
        }

        int messageSize = encrypted.length;

        byte[] decrypted = tripleDESCBCDecryption(INNER_KEY, IV, encrypted, messageSize);

        boolean valid = true;
        int decryptedSize = decrypted.length;
        int prfxSize = LOCK.length;
        if ((decrypted != null) && (decryptedSize > prfxSize)) {
            for (int i = 0; (i < prfxSize) && (valid); i++) {
                if (decrypted[i] != LOCK[i]) {
                    valid = false;
                }
            }
        } else {
            valid = false;
        }
        if (!valid) {
            throw new IOException("Invalid prefix");
        }

        int contentSize = decryptedSize - prfxSize;
        byte[] contentBytes = new byte[contentSize];
        System.arraycopy(decrypted, prfxSize, contentBytes, 0, contentSize);

        return contentBytes;
    }

    /**
     * Method to perform a triple DES encryption
     * @param secretKey The secret key to encrypt
     * @param initVector Vector
     * @param plainText The text to be encrypted
     * @param cipherText Will be filled whith the encrypted text
     * @param dataLength The length of the plain text
     * @return int The length of the encrypted text
     */
    private static int tripleDESCBCEncryption(
            byte[] secretKey,
            byte[] initVector,
            byte[] plainText,
            byte[] cipherText,
            int dataLength ) throws IOException, GeneralSecurityException {
        int finalLength = 0;
        SecretKeySpec skeySpec = new SecretKeySpec(secretKey, "DESede");
        IvParameterSpec ivSpec = new IvParameterSpec(initVector);
        Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(plainText, 0, dataLength);
        if (encrypted != null) {
            finalLength = dataLength;
            System.arraycopy(encrypted, 0, cipherText, 0, dataLength);
        }
        return finalLength;
    }

    /**
     * Method to perform a triple DES decryption
     * @param secretKey The secret key to decrypt
     * @param initVector Vector
     * @param cipherText The ciphered text to be decrypted
     * @param dataLength The length of the plain text
     * @return byte[] Array of bytes containing the decrypted text
     */
    private static byte[] tripleDESCBCDecryption(
            byte[] secretKey,
            byte[] initVector,
            byte[] cipherText,
            int dataLength ) throws IOException, GeneralSecurityException {
        SecretKeySpec skeySpec = new SecretKeySpec(secretKey, "DESede");
        IvParameterSpec ivSpec = new IvParameterSpec(initVector);
        Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
        byte[] decrypted = cipher.doFinal(cipherText, 0, dataLength);
        return decrypted;
    }

}
