package cz.technecium.nasremote;

/**
 * Holder for UI data.
 *
 */
public class UiData {
    private String username;
    private String password;
    private String ipAddress;
    private String macAddress;
    private boolean storeData;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public boolean isStoreData() {
        return storeData;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setStoreData(boolean storeData) {
        this.storeData = storeData;
    }
}
