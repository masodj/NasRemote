package cz.technecium.nasremote;

import android.content.Context;
import android.graphics.Color;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NasStatusChangeListener {
    private static final String MAC_REGEXP = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
    private static final String IP_REGXP = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)" +
            "{3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    private AppModel model;
    private EditText username;
    private EditText password;
    private EditText ipAddress;
    private EditText macAddress;
    private TextView status;
    private CheckBox storeData;
    private Button turnOn;
    private Button turnOff;

    private static final String STATUS_OFF = "Status: OFF";
    private static final String STATUS_ON = "Status: ON";

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                handleValidations();
            }
        }
    };

    private TextWatcherAdapter textWatcherAdapter = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            handleValidations();
            if (validateInputs().getInvalidElements().isEmpty()) {
                turnOff.setEnabled(true);
                turnOn.setEnabled(true);
                model.stopNasStateResolving();
                model.startNasStateResolving(ipAddress.getText().toString());
            } else {
                turnOff.setEnabled(false);
                turnOn.setEnabled(false);
                model.stopNasStateResolving();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = new AppModel(getApplicationContext(), this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        ipAddress = findViewById(R.id.ipAddress);
        storeData = findViewById(R.id.storeData);
        turnOff = findViewById(R.id.turnOffButton);
        macAddress = findViewById(R.id.macAddress);
        turnOn = findViewById(R.id.turnOnButton);
        status = findViewById(R.id.nasStatus);

        UiData storedUiState = model.getStoredUiState();
        if (storedUiState != null && storedUiState.isStoreData()) {
            fillFromUiData(storedUiState);
        }

        model.startNasStateResolving(ipAddress.getText().toString());
        addListeners();
    }

    private void addListeners() {
        storeData.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        model.storeUiState(buildUiData());
                    }
                }
        );

        turnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.turnOnNas(ipAddress.getText().toString(),
                        macAddress.getText().toString());
                handleStateStoring();
            }
        });

        turnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.turnOffNas(ipAddress.getText().toString(),
                        username.getText().toString(),
                        password.getText().toString());
                handleStateStoring();
            }
        });

        ipAddress.setOnFocusChangeListener(focusChangeListener);
        macAddress.setOnFocusChangeListener(focusChangeListener);
        username.setOnFocusChangeListener(focusChangeListener);
        password.setOnFocusChangeListener(focusChangeListener);

        ipAddress.addTextChangedListener(textWatcherAdapter);
        macAddress.addTextChangedListener(textWatcherAdapter);
        username.addTextChangedListener(textWatcherAdapter);
        password.addTextChangedListener(textWatcherAdapter);
    }

    @Override
    public void nasStatusChanged(NasStatusChangeEvent event) {
        if (event.getEventType() == NasStatusChangeEventType.NAS_OFF) {
            status.setText(STATUS_OFF);
            status.setTextColor(Color.RED);
        } else {
            status.setText(STATUS_ON);
            status.setTextColor(Color.GREEN);
        }
    }

    @Override
    protected void onDestroy() {
        model.stopNasStateResolving();
        super.onDestroy();
    }

    private UiData buildUiData() {
        UiData uiData = new UiData();
        uiData.setStoreData(storeData.isChecked());
        uiData.setMacAddress(macAddress.getText().toString());
        uiData.setIpAddress(ipAddress.getText().toString());
        uiData.setPassword(password.getText().toString());
        uiData.setUsername(username.getText().toString());
        return uiData;
    }

    private void fillFromUiData(UiData uiData) {
        storeData.setChecked(uiData.isStoreData());
        macAddress.setText(uiData.getMacAddress());
        ipAddress.setText(uiData.getIpAddress());
        username.setText(uiData.getUsername());
        password.setText(uiData.getPassword());
    }

    private UiValidationResult validateInputs() {
        Set<TextView> invalidElements = new HashSet<>();
        Set<TextView> validElements = new HashSet<>();

        String ipAddress = this.ipAddress.getText().toString();
        String macAddress = this.macAddress.getText().toString();
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        if (!ipAddress.matches(IP_REGXP)) {
            invalidElements.add(this.ipAddress);
        } else {
            validElements.add(this.ipAddress);
        }

        if (!macAddress.matches(MAC_REGEXP)) {
            invalidElements.add(this.macAddress);
        } else {
            validElements.add(this.macAddress);
        }

        if (username == null || username.isEmpty()) {
            invalidElements.add(this.username);
        } else {
            validElements.add(this.username);
        }

        if (password == null || password.isEmpty()) {
            invalidElements.add(this.password);
        } else {
            validElements.add(this.password);
        }
        return new UiValidationResult(invalidElements, validElements);
    }

    private void handleValidations() {
        UiValidationResult uiValidationResult = validateInputs();
        for (TextView view : uiValidationResult.getInvalidElements()) {
            view.setTextColor(Color.RED);
        }

        for (TextView view : uiValidationResult.getValidElements()) {
            view.setTextColor(Color.BLACK);
        }
    }

    private void handleStateStoring(){
        UiData storedUiState = model.getStoredUiState();
        if (storedUiState != null && storedUiState.isStoreData()) {
            model.storeUiState(buildUiData());
        }
    }
}
