package cz.technecium.nasremote;

public class NasStatusChangeEvent {
   private final NasStatusChangeEventType eventType;

    public NasStatusChangeEvent(NasStatusChangeEventType eventType) {
        this.eventType = eventType;
    }

    public NasStatusChangeEventType getEventType() {
        return eventType;
    }
}

