package cz.technecium.nasremote;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Resolves NAS state every 10 seconds.
 */
public class NasStateResolver extends Thread {
    private static final String NAS_URL_PATTERN = "http://%s/r51009,/adv,/loginwrap.html";

    private final String nasIpAddress;
    private final List<NasStatusChangeListener> nasStatusChangeListeners = new CopyOnWriteArrayList<>();
    private volatile boolean running = false;

    public NasStateResolver(String nasIpAddress) {
        super();
        this.nasIpAddress = nasIpAddress;
    }

    @Override
    public void run() {
        if (nasIpAddress == null || nasIpAddress.isEmpty()) {
            return;
        }

        running = true;
        while (running) {
            try {
                CloseableHttpClient client = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(String.format(NAS_URL_PATTERN, nasIpAddress));
                CloseableHttpResponse response = client.execute(httpGet);
                NasStatusChangeEvent event;
                if (response == null) {
                    event = new NasStatusChangeEvent(NasStatusChangeEventType.NAS_OFF);
                } else if (response.getStatusLine().getStatusCode() > 399) {
                    event = new NasStatusChangeEvent(NasStatusChangeEventType.NAS_OFF);
                } else {
                    event = new NasStatusChangeEvent(NasStatusChangeEventType.NAS_ON);
                }
                fireNasStatusChangedEvent(event);
                sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds NAS status change listener.
     *
     * @param listener
     */
    public void addNasStatusChangeListener(NasStatusChangeListener listener) {
        nasStatusChangeListeners.add(listener);
    }

    private void fireNasStatusChangedEvent(NasStatusChangeEvent event) {
        for (NasStatusChangeListener listener : nasStatusChangeListeners) {
            listener.nasStatusChanged(event);
        }
    }

    /**
     * Stops state resolving thead.
     */
    public synchronized void stopStateResolving() {
        this.running = false;
    }
}