package cz.technecium.nasremote;

import lombok.Data;

/**
 * Holder for UI data.
 *
 */
@Data
public class UiData {
    private String username;
    private String password;
    private String ipAddress;
    private String macAddress;
    private boolean storeData;
}
