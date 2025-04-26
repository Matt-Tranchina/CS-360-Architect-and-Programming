package com.zybooks.matt_tranchina_project_two_weight_app.ui.settings;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.zybooks.matt_tranchina_project_two_weight_app.R;
import com.zybooks.matt_tranchina_project_two_weight_app.databinding.FragmentSettingsBinding;
import com.zybooks.matt_tranchina_project_two_weight_app.ui.login.LoginActivity;

public class SettingsFragment extends Fragment {
    private static final String SMS_PERMISSION = Manifest.permission.SEND_SMS;
    private boolean smsAuthorized = false;
    private static final String PREFERENCE_THEME_NAME = "theme_preference";
    private static final String THEME_KEY = "theme_mode";
    private static final int THEME_SYSTEM = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
    private static final int THEME_LIGHT = AppCompatDelegate.MODE_NIGHT_NO;
    private static final int THEME_DARK = AppCompatDelegate.MODE_NIGHT_YES;
    private static final String PREFERENCE_LOGIN_NAME = "login_preference";
    private static final String KEY_REMEMBER_ME = "remember_me";
    private Switch notificationSwitch, darkThemeSwitch, rememberMeSwitch;
    private Button logoutButton, resetPasswordButton;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    private FragmentSettingsBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the permission launcher
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted.
                        notificationSwitch.setChecked(true);
                        Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        // TODO: Enable text function
                    } else {
                        // Permission is denied.
                        notificationSwitch.setChecked(false);
                        if (smsAuthorized){
                            showPermissionDialog();
                        }else {
                            showPermissionDialog();
                            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSettings;
        settingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
        
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // --------------------------      NOTIFICATION SWITCH ------------------------------------
        notificationSwitch = view.findViewById(R.id.notificationSwitch);

        //Check for existing permission
        if (ContextCompat.checkSelfPermission(requireContext(), SMS_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            notificationSwitch.setChecked(true);
            // TODO: Enable text function
        } else {
            notificationSwitch.setChecked(false);
        }

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (ContextCompat.checkSelfPermission(requireContext(), SMS_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                        smsAuthorized = shouldShowRequestPermissionRationale(SMS_PERMISSION);
                        requestPermissionLauncher.launch(SMS_PERMISSION);
                    } else {
                        // Permission already granted
                        Toast.makeText(getContext(), "Notifications Enabled", Toast.LENGTH_SHORT).show();
                        // TODO: Enable text function
                    }
                } else {
                    Toast.makeText(getContext(), "Notifications Disabled", Toast.LENGTH_SHORT).show();//

                }
            }
        });

        // ------------------------------------ THEME SWITCH --------------------------------------
        darkThemeSwitch = view.findViewById(R.id.themeSwitch);

        // Initialize switch state based on previous state or system default
        int currentTheme = getSavedThemeMode();
        updateSwitchState(currentTheme);

        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    setThemeMode(THEME_DARK);
                } else {
                    setThemeMode(THEME_LIGHT);
                }
            }
        });

        // ------------------------------------- LOGOUT BUTTON -------------------------------------
        logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

    }

    private void showLogoutDialog(){
        new AlertDialog.Builder(requireContext())
                .setTitle("Log out of account")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    try {
                        logout();

                    } catch (Exception e) {
                        Log.e("SettingsFragment", "ERROR: Fragent not hosted by LoginActivity");
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                })
                .show();

    }

    private void showPermissionDialog(){

        new AlertDialog.Builder(requireContext())
                .setTitle("SMS Permission Required")
                .setMessage("Please enable permissions for SMS messaging.")
                .setPositiveButton("Accept", (dialog, which) -> {
                    if (!shouldShowRequestPermissionRationale(SMS_PERMISSION)){
                        openAppSettings();
                        dialog.cancel();
                    }
                    requestPermissionLauncher.launch(SMS_PERMISSION);
                    dialog.cancel();
                })
                .setNegativeButton("Deny", (dialog, which) -> {
                    notificationSwitch.setChecked(false);
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                })
                .show();

    }

    private void openAppSettings(){

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private int getDefaultThemeMode(){

        // Check if device is already in Dark theme
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES ? THEME_DARK : THEME_LIGHT;
    }

    private int getSavedThemeMode(){
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFERENCE_THEME_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(THEME_KEY, getDefaultThemeMode());
    }

    private void saveThemeMode(int mode){
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFERENCE_THEME_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(THEME_KEY, mode).apply();
    }

    private void setThemeMode(int mode){
        saveThemeMode(mode);
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    private void updateSwitchState(int mode){
        darkThemeSwitch.setChecked(mode == THEME_DARK);
    }

    private void logout(){
        // Clear the "remember me" preference (and any credentials if needed)
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFERENCE_LOGIN_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_REMEMBER_ME, false).apply(); // Turn off remember me

        // Launch LoginActivity and clear the task
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}