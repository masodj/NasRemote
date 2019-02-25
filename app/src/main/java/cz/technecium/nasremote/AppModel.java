package cz.technecium.nasremote;

import android.content.Context;

public class AppModel {
    private final MainActivity mainActivity;
    private final NasConnector nasConnector = new NasConnector();
    private final ApplicationDataRepository applicationDataRepository;

    private NasStateResolver nasStateResolver;

    public AppModel(Context applicationContext, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.applicationDataRepository = new ApplicationDataRepository(applicationContext);
    }

    /**
     * Turn NAS OFF
     *
     * @param ipAddress
     * @param username
     * @param password
     */
    public void turnOffNas(String ipAddress, String username, String password) {
        nasConnector.turnOffNas(ipAddress, username, password);
    }

    /**
     * Turn NAS on
     *
     * @param ipAddress
     * @param nasMacAddress
     */
    public void turnOnNas(String ipAddress, String nasMacAddress) {
        nasConnector.turnOnNas(ipAddress, nasMacAddress);
    }

    /**
     * Stats NAS state resolver and adds main activity as listener to stage changes
     *
     */
    public void startNasStateResolving(String nasIpAddress) {
        nasStateResolver = new NasStateResolver(nasIpAddress);
        nasStateResolver.addNasStatusChangeListener(mainActivity);
        nasStateResolver.start();
    }

    /**
     * Stops NAS state resolver.
     *
     */
    public void stopNasStateResolving() {
        if (nasStateResolver != null) {
            nasStateResolver.stopStateResolving();
        }
    }

    /**
     * Gets store Ui state.
     *
     * @return
     */
    public UiData getStoredUiState() {
        return applicationDataRepository.getUiData();
    }

    /**
     * Stores current Ui state.
     *
     * @return
     */
    public void storeUiState(UiData data) {
        applicationDataRepository.storeUiData(data);
    }
}
