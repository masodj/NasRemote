package cz.technecium.nasremote;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Facade over SharedPreferences to have possibility to store UI state.
 */
public class ApplicationDataRepository {

    private final SharedPreferences sharedPref;
    private final SharedPreferences.Editor editor;

    public ApplicationDataRepository(Context applicationContext) {
        sharedPref = applicationContext.getSharedPreferences("APP", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    /**
     * Stores UI state to SharedPreferences
     *
     * @param data
     */
    public void storeUiData(UiData data) {
        editor.putString("username", data.getUsername());
        editor.putString("password", data.getPassword());
        editor.putString("ipAddress", data.getIpAddress());
        editor.putString("macAddress", data.getMacAddress());
        editor.putBoolean("storeData", data.isStoreData());
        editor.commit();
    }

    /**
     * Gets UI state from SharedPreferences
     *
     * @return
     */
    public UiData getUiData() {
        UiData uiData = new UiData();
        uiData.setUsername(sharedPref.getString("username", ""));
        uiData.setPassword(sharedPref.getString("password", ""));
        uiData.setIpAddress(sharedPref.getString("ipAddress", ""));
        uiData.setMacAddress(sharedPref.getString("macAddress", ""));
        uiData.setStoreData(sharedPref.getBoolean("storeData", false));
        return uiData;
    }
}