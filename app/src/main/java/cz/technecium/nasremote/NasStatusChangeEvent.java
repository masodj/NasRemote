package cz.technecium.nasremote;

import lombok.Data;

/**
 * NAS status change event object
 *
 */
@Data
public class NasStatusChangeEvent {
    private final NasStatusChangeEventType eventType;
}

