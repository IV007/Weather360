package com.sidelance.weather.weather360.commons.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sidelance.weather.weather360.AppConstants;

import java.util.Map;
import java.util.Set;

/**
 * Class that handles all saved data
 */
public class AppPreferences implements SharedPreferences {

    private static final String TAG = AppPreferences.class.getSimpleName();

    private static final String mLastUserLocation = "saveUserLocation";

    private String location;

    public AppPreferences(String location) {
        this.location = location;
    }


    public void saveToPersistence(Context context){

        SharedPreferences preferences = context.getSharedPreferences(AppConstants.STORE_NAME, Context.MODE_PRIVATE);
        boolean result = Screw.saveCipheredValue(context, preferences, mLastUserLocation, getLocation());
        if (!result){
            Log.e(TAG, "Failed to save user Location");
        }

    }

    public AppPreferences loadFromPersistence(Context context){

        AppPreferences result;

        SharedPreferences preferences = context.getSharedPreferences(AppConstants.STORE_NAME, Context.MODE_PRIVATE);
        String location = Screw.loadAndDecipherValue(context, preferences,mLastUserLocation);

        result = new AppPreferences(location);

        return result;
    }

    /**
     * Retrieve all values from the preferences.
     * <p/>
     * <p>Note that you <em>must not</em> modify the collection returned
     * by this method, or alter any of its contents.  The consistency of your
     * stored data is not guaranteed if you do.
     *
     * @return Returns a map containing a list of pairs key/value representing
     * the preferences.
     * @throws NullPointerException
     */
    @Override
    public Map<String, ?> getAll() {
        return null;
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a String.
     * @throws ClassCastException
     */
    @Nullable
    @Override
    public String getString(String key, String defValue) {
        return null;
    }

    /**
     * Retrieve a set of String values from the preferences.
     * <p/>
     * <p>Note that you <em>must not</em> modify the set instance returned
     * by this call.  The consistency of the stored data is not guaranteed
     * if you do, nor is your ability to modify the instance at all.
     *
     * @param key       The name of the preference to retrieve.
     * @param defValues Values to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defValues.
     * Throws ClassCastException if there is a preference with this name
     * that is not a Set.
     * @throws ClassCastException
     */
    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return null;
    }

    /**
     * Retrieve an int value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * an int.
     * @throws ClassCastException
     */
    @Override
    public int getInt(String key, int defValue) {
        return 0;
    }

    /**
     * Retrieve a long value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a long.
     * @throws ClassCastException
     */
    @Override
    public long getLong(String key, long defValue) {
        return 0;
    }

    /**
     * Retrieve a float value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a float.
     * @throws ClassCastException
     */
    @Override
    public float getFloat(String key, float defValue) {
        return 0;
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a boolean.
     * @throws ClassCastException
     */
    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return false;
    }

    /**
     * Checks whether the preferences contains a preference.
     *
     * @param key The name of the preference to check.
     * @return Returns true if the preference exists in the preferences,
     * otherwise false.
     */
    @Override
    public boolean contains(String key) {
        return false;
    }

    /**
     * Create a new Editor for these preferences, through which you can make
     * modifications to the data in the preferences and atomically commit those
     * changes back to the SharedPreferences object.
     * <p/>
     * <p>Note that you <em>must</em> call {@link Editor#commit} to have any
     * changes you perform in the Editor actually show up in the
     * SharedPreferences.
     *
     * @return Returns a new instance of the {@link Editor} interface, allowing
     * you to modify the values in this SharedPreferences object.
     */
    @Override
    public Editor edit() {
        return null;
    }

    /**
     * Registers a callback to be invoked when a change happens to a preference.
     * <p/>
     * <p class="caution"><strong>Caution:</strong> The preference manager does
     * not currently store a strong reference to the listener. You must store a
     * strong reference to the listener, or it will be susceptible to garbage
     * collection. We recommend you keep a reference to the listener in the
     * instance data of an object that will exist as long as you need the
     * listener.</p>
     *
     * @param listener The callback that will run.
     * @see #unregisterOnSharedPreferenceChangeListener
     */
    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    /**
     * Unregisters a previous callback.
     *
     * @param listener The callback that should be unregistered.
     * @see #registerOnSharedPreferenceChangeListener
     */
    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    public String getLocation() {
        return location;
    }
}
