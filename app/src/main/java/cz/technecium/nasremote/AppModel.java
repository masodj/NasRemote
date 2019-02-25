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

    public void turnOffNas(String ipAddress, String username, String password) {
        nasConnector.turnOffNas(ipAddress, username, password);
    }

    public void turnOnNas(String ipAddress, String nasMacAddress) {
        nasConnector.turnOnNas(ipAddress, nasMacAddress);
    }

    public void startNasStateResolving(String nasIpAddress) {
        nasStateResolver = new NasStateResolver(nasIpAddress);
        nasStateResolver.addNasStatusChangeListener(mainActivity);
        nasStateResolver.start();
    }

    public void stopNasStateResolving() {
        if (nasStateResolver != null) {
            nasStateResolver.stopStateResolving();
        }
    }

    public UiData getStoredUiState() {
        return applicationDataRepository.getUiData();
    }

    public void storeUiState(UiData data) {
        applicationDataRepository.storeUiData(data);
    }
}
