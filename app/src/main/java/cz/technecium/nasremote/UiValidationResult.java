package cz.technecium.nasremote;

import android.widget.TextView;

import java.util.Set;

public class UiValidationResult {
    private final Set<TextView> invalidElements;
    private final Set<TextView> validElements;

    public UiValidationResult(Set<TextView> invalidElements, Set<TextView> validElements) {
        this.invalidElements = invalidElements;
        this.validElements = validElements;
    }

    public Set<TextView> getInvalidElements() {
        return invalidElements;
    }

    public Set<TextView> getValidElements() {
        return validElements;
    }
}
