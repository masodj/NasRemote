package cz.technecium.nasremote;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Adapter to get rid of not used methods of TextWatcher interface.
 *
 */
public class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
