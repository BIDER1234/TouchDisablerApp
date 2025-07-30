package com.example.touchdisabler;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.net.Uri;
import android.widget.Toast;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean isBlocking = false;
    private Button startButton, stopButton;
    private Switch autoStartSwitch;
    private int navigationIndex = 0;
    private final int TOTAL_OPTIONS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            Toast.makeText(this, "יש לאפשר הרשאת 'מעל אפליקציות אחרות'", Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        autoStartSwitch = findViewById(R.id.autoStartSwitch);

        startButton.setOnClickListener(v -> startTouchBlock());
        stopButton.setOnClickListener(v -> stopTouchBlock());

        if (getIntent().getBooleanExtra("AUTO_START", false)) {
            startTouchBlock();
        }

        startButton.requestFocus();
    }

    private void startTouchBlock() {
        if (!isBlocking) {
            isBlocking = true;
            startService(new Intent(this, TouchBlockService.class));
        }
    }

    private void stopTouchBlock() {
        if (isBlocking) {
            isBlocking = false;
            stopService(new Intent(this, TouchBlockService.class));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            navigationIndex = (navigationIndex + 1) % TOTAL_OPTIONS;
            updateFocus();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            navigationIndex = (navigationIndex - 1 + TOTAL_OPTIONS) % TOTAL_OPTIONS;
            updateFocus();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            switch (navigationIndex) {
                case 0: startButton.performClick(); break;
                case 1: stopButton.performClick(); break;
                case 2: autoStartSwitch.toggle(); break;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void updateFocus() {
        switch (navigationIndex) {
            case 0: startButton.requestFocus(); break;
            case 1: stopButton.requestFocus(); break;
            case 2: autoStartSwitch.requestFocus(); break;
        }
    }
}