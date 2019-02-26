package cz.technecium.nasremote;

import android.widget.TextView;

import java.util.Set;

import lombok.Data;

/**
 * Holder for UI validation results.
 */
@Data
public class UiValidationResult {
    private final Set<TextView> invalidElements;
    private final Set<TextView> validElements;
}
