package cz.technecium.nasremote;

/**
 * NAS status change event object
 *
 */
public class NasStatusChangeEvent {
   private final NasStatusChangeEventType eventType;

    public NasStatusChangeEvent(NasStatusChangeEventType eventType) {
        this.eventType = eventType;
    }

    public NasStatusChangeEventType getEventType() {
        return eventType;
    }
}

