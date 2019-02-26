package cz.technecium.nasremote;

import android.widget.TextView;

import java.util.Set;

import lombok.Getter;

/**
 * Holder for UI validation results.
 */
@Getter
public class UiValidationResult {
    private final Set<TextView> invalidElements;
    private final Set<TextView> validElements;

    public UiValidationResult(Set<TextView> invalidElements, Set<TextView> validElements) {
        this.invalidElements = invalidElements;
        this.validElements = validElements;
    }
}
