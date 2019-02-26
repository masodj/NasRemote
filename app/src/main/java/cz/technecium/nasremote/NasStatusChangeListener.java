package cz.technecium.nasremote;

/**
 * Interface which have to be implemented by NAS status change listeners.
 *
 */
public interface NasStatusChangeListener {
    void nasStatusChanged(NasStatusChangeEvent event);
}
