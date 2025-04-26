package com.zybooks.matt_tranchina_project_two_weight_app.ui.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;

import com.zybooks.matt_tranchina_project_two_weight_app.MainActivity;
import com.zybooks.matt_tranchina_project_two_weight_app.R;
import com.zybooks.matt_tranchina_project_two_weight_app.ui.database.ProfileDatabase;
import com.zybooks.matt_tranchina_project_two_weight_app.ui.profile.ProfileDataModel;
import com.zybooks.matt_tranchina_project_two_weight_app.ui.profile.ProfileFragment;

public class LoginActivity extends AppCompatActivity implements TextWatcher {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private Button registerButton;
    private Switch rememberMeSwitch;
    private ProfileDatabase ProfileDB;
    private SharedPreferences sharedPreferences;
    public int loginError = 0;

    private static final String PREF_NAME = "login_preference";
    private static final String KEY_REMEMBER_ME = "remember_me";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    public static String PASS_USERNAME = "username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.textUsername);
        passwordEditText = findViewById(R.id.textPassword);
        signInButton = findViewById(R.id.buttonSignIn);
        registerButton = findViewById(R.id.buttonRegister);
        rememberMeSwitch = findViewById(R.id.switchRemember);
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if (shouldAutoLogin()) {
            autoLogin();
            return;
        }
        loadSavedCredentials();

        ProfileDB = new ProfileDatabase(this);

        usernameEditText.addTextChangedListener((TextWatcher) this);
        passwordEditText.addTextChangedListener((TextWatcher) this);


    }

    public void buttonClick(View v) {
        if (v.getId() == R.id.buttonSignIn) {
            onLoginClick(v);
        } else if (v.getId() == R.id.buttonRegister) {
            onRegisterClick(v);
        }
    }




    public void onLoginClick(View view) {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (ProfileDB.searchProfile(username, password)) {
            if (rememberMeSwitch.isChecked()) {
                saveCredentials(username, password);
            } else {
                clearCredentials();
            }
            PASS_USERNAME = username;
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Toast.makeText(this, "Welcome back " + username + "!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            loginError += 1;
        }
        if (loginError == 3) {
            checkLoginError();
            loginError += 1;
        } else if (loginError == 6) {
            checkLoginError();
            loginError = 0;
        }
        rememberMeSwitch.setChecked(sharedPreferences.getBoolean(KEY_REMEMBER_ME, false));
    }

    public void onRegisterClick(View view) {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (isValidPassword(password)) {
            try {
                if (!ProfileDB.searchProfile(username, password)) {
                    ProfileDataModel newUser = new ProfileDataModel(username, password,null, null,
                            null, null, null, null, null, null, null);
                    if (ProfileDB.createUser(newUser) > 0) {
                        if (rememberMeSwitch.isChecked()) {
                            saveCredentials(username, password);
                        } else {
                            clearCredentials();
                        }
                        PASS_USERNAME = username;
                        Intent intent = new Intent(this, MainActivity.class);
                        Toast.makeText(this, "Register successful, welcome " + username + "!", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error registering new user", Toast.LENGTH_SHORT).show();
            }
        } else {
            loginError += 1;
        }
        if (loginError == 3) {
            checkLoginError();
            loginError += 1;
        } else if (loginError == 6) {
            checkLoginError();
            loginError = 0;
        }
        rememberMeSwitch.setChecked(sharedPreferences.getBoolean(KEY_REMEMBER_ME, false));
    }


    public Boolean isValidPassword(String password) {
        int len = password.length();
        boolean length = false;
        boolean hasDigit = false;
        boolean hasSpecialCharacter = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialCharacter = true;
            } else if (len >= 10) {
                length = true;
            }
        }

        if (!length) {
            Toast.makeText(this, "Password must be at least 10 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!hasDigit && !hasSpecialCharacter) {
            Toast.makeText(this, "Password must contain (1) number and (1) special character" +
                    " (!, #, $, %, &, *)", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!hasDigit && hasSpecialCharacter) {
            Toast.makeText(this, "Password must contain (1) number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (hasDigit && !hasSpecialCharacter) {
            Toast.makeText(this, "Password must contain (1) " +
                    "special character (!, #, $, %, &, *)", Toast.LENGTH_LONG).show();
            return false;
        }
        return length && hasDigit && hasSpecialCharacter;
    }

    public Boolean checkLoginError() {
        if (loginError == 3) {
            Toast.makeText(this, "WARNING: Two (2) attempts remaining", Toast.LENGTH_LONG).show();
            return false;
        }
        if (loginError == 6) {
            signInButton.setEnabled(false);
            registerButton.setEnabled(false);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                signInButton.setEnabled(true);
            }, 300000);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                registerButton.setEnabled(true);
            }, 300000);
            Toast.makeText(this, "Try again in 5 minutes", Toast.LENGTH_LONG).show();

            return false;
        }
        return true;
    }

    private boolean shouldAutoLogin() {
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
    }

    private void autoLogin() {
        String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
        String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");

        if (isValidPassword(savedPassword)) {
            PASS_USERNAME = savedUsername;
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("username", savedUsername);
            startActivity(intent);
            finish();
        } else {
            clearCredentials();
            loadSavedCredentials();
        }
    }

    private void loadSavedCredentials() {
        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
        rememberMeSwitch.setChecked(rememberMe);

        if (rememberMe) {
            String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
            PASS_USERNAME = savedUsername;
            usernameEditText.setText(savedUsername);
            passwordEditText.setHint(savedPassword);
        }
    }

    private void saveCredentials(String username, String password) {
        PASS_USERNAME = username;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_REMEMBER_ME, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public void clearCredentials() {
        PASS_USERNAME = "";
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_REMEMBER_ME, false);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    // Once username has been typed, enable Login and Register buttons
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (checkLoginError()) {
            signInButton.setEnabled(!usernameEditText.getText().toString().trim().isEmpty() &&
                    !passwordEditText.getText().toString().trim().isEmpty());
            registerButton.setEnabled(!usernameEditText.getText().toString().trim().isEmpty() &&
                    !passwordEditText.getText().toString().trim().isEmpty());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}