package com.zybooks.matt_tranchina_project_two_weight_app.ui.profile;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.zybooks.matt_tranchina_project_two_weight_app.R;
import com.zybooks.matt_tranchina_project_two_weight_app.databinding.FragmentProfileBinding;
import com.zybooks.matt_tranchina_project_two_weight_app.ui.database.ProfileDatabase;
import com.zybooks.matt_tranchina_project_two_weight_app.ui.login.LoginActivity;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileDatabase profileDB;
    private ProfileDataModel profileData;
    private String username;
    private static final String PREFERENCE_LOGIN_NAME = "login_preference";
    private static final String KEY_REMEMBER_ME = "remember_me";

    private static final String KEY_USERNAME = "username";
    EditText firstNameHolder, lastNameHolder, emailHolder, phoneHolder,
    heightHolder, startWeightHolder, goalWeightHolder, ageHolder;

    RadioGroup genderGroup;
    RadioButton maleButton, femaleButton, neutralButton;

    Button confirmButton, deleteProfileButton;
    String genderString;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        // Place holders
        firstNameHolder = view.findViewById(R.id.firstNameCardEnterText);
        lastNameHolder = view.findViewById(R.id.lastNameCardEnterText);
        emailHolder = view.findViewById(R.id.emailCardEnterText);
        phoneHolder = view.findViewById(R.id.phoneNumberCardEnterText);
        heightHolder = view.findViewById(R.id.heightCardEnterText);
        startWeightHolder = view.findViewById(R.id.weightStartCardEnterText);
        goalWeightHolder = view.findViewById(R.id.weightGoalCardEnterText);
        ageHolder = view.findViewById(R.id.ageCardEnterText);
        genderGroup = view.findViewById(R.id.radioGroupCardView);
        maleButton = view.findViewById(R.id.maleRadioButtonCardView);
        femaleButton = view.findViewById(R.id.femaleRadioButtonCardView);
        neutralButton = view.findViewById(R.id.neutralRadioButtonCardView);

        confirmButton = view.findViewById(R.id.confirmButtonCardView);
        deleteProfileButton = view.findViewById(R.id.deleteButtonCardView);

        //Context context = getContextFromActivity();
        profileDB = new ProfileDatabase(requireContext());
        profileData = new ProfileDataModel();

        username = LoginActivity.PASS_USERNAME;

        if (username != null){
            loadProfileData(username);
        } else {
            Log.e("ProfileFragment", "USERNAME: " + username);
            Log.e("ProfileFragment", "ERROR: No username provided");
        }



        // _________________________________________________________________________________________
        //----------------------------------- CONFIRM CLICK ----------------------------------------
        // _________________________________________________________________________________________

        // Update profile when Confirm is clicked
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = firstNameHolder.getText().toString().trim();
                String lastName = lastNameHolder.getText().toString().trim();
                String email = emailHolder.getText().toString().trim();
                String phoneNumber = phoneHolder.getText().toString().trim();
                String height = heightHolder.getText().toString().trim();
                String startWeight = startWeightHolder.getText().toString().trim();
                String goalWeight = goalWeightHolder.getText().toString().trim();
                String age = ageHolder.getText().toString().trim();
                String gender = getGenderSelection();

                // If important information is not blank, update user Profile database
                if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() &&
                        phoneNumber.length() == 10 && !height.isEmpty() && !startWeight.isEmpty()
                        && !goalWeight.isEmpty() && !age.isEmpty() && !gender.isEmpty()) {

                    if (updateProfile()) {
                        Toast.makeText(getContext(), "Profile updated successfully" , Toast.LENGTH_SHORT).show();
                        // Clear the name holders
                        firstNameHolder.setText("");
                        lastNameHolder.setText("");
                        emailHolder.setText("");
                        phoneHolder.setText("");
                        heightHolder.setText("");
                        startWeightHolder.setText("");
                        goalWeightHolder.setText("");
                        ageHolder.setText("");
                        loadProfileData(username);
                    }

                } else if (firstName.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your first name", Toast.LENGTH_SHORT).show();
                }else if (lastName.isEmpty()) {
                    Toast.makeText(getContext(),"Please enter your last name", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()){
                    Toast.makeText(getContext(),"Please enter a valid email", Toast.LENGTH_SHORT).show();
                } else if (phoneNumber != null && phoneNumber.length() != 10){
                    Toast.makeText(getContext(), "Please enter a valid 10 digit phone number", Toast.LENGTH_SHORT).show();
                } else if (startWeight.isEmpty()){
                    Toast.makeText(getContext(),"Please enter your starting weight", Toast.LENGTH_SHORT).show();
                } else if (goalWeight.isEmpty()){
                    Toast.makeText(getContext(), "Please enter your goal weight", Toast.LENGTH_SHORT).show();
                } else if (age.isEmpty()){
                    Toast.makeText(getContext(), "Please enter your age", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error creating user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // _________________________________________________________________________________________
        // ---------------------------------- DELETE CLICK -----------------------------------------
        // _________________________________________________________________________________________

        deleteProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Are You Sure?")
                        .setMessage("Accepting will permanently delete your profile.")
                        .setPositiveButton("I'm sure", (dialog, which) -> {
                            if (profileDB.deleteUser(username)) {
                                Toast.makeText(getContext(), "Profile deleted successfully", Toast.LENGTH_SHORT).show();
                                // Clear the "remember me" preference (and any credentials if needed)
                                SharedPreferences prefs = requireContext().getSharedPreferences(PREFERENCE_LOGIN_NAME, Context.MODE_PRIVATE);
                                prefs.edit().putBoolean(KEY_REMEMBER_ME, false).apply(); // Turn off remember me

                                // Launch LoginActivity and clear the task
                                Intent intent = new Intent(requireContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getContext(), "ERROR: Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .setNegativeButton("Nevermind", (dialog, which) -> {
                            dialog.cancel();
                        })
                        .show();

            }
        });


    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        return root;

    }


    public RadioButton getSelectedRadioButton(@NonNull RadioGroup radioGroup){
        int selectedID = radioGroup.getCheckedRadioButtonId();
        if (selectedID == -1){
            // No item was selected
            return null;
        }else {
            return radioGroup.findViewById(selectedID);
        }
    }

    public void setSelectedRadioButton(String gender){
        if (Objects.equals(gender, maleButton.getText().toString())){
            maleButton.setChecked(true);
        }
        if (Objects.equals(gender, femaleButton.getText().toString())){
            femaleButton.setChecked(true);
        }
        if (Objects.equals(gender, neutralButton.getText().toString())){
            neutralButton.setChecked(true);
        }
    }

    public String getGenderSelection(){
        RadioButton selectedButton = getSelectedRadioButton(genderGroup);

        if (selectedButton != null){
            genderString = selectedButton.getText().toString();
        } else {
            genderString = "";
        }
        return genderString;
    }

    public static String formatPhoneNumber(@NonNull String phoneNumber){
        String areaCode = phoneNumber.substring(0,3);
        String prefix = phoneNumber.substring(3,6);
        String lineNumber = phoneNumber.substring(6,10);

        return String.format("(%s)-%s-%s", areaCode, prefix, lineNumber);
    }

    private void loadProfileData(String username){
        SQLiteDatabase db = profileDB.getReadableDatabase();

        String[] projection = {
                ProfileDatabase.COL_4_FIRST_NAME,
                ProfileDatabase.COL_5_LAST_NAME,
                ProfileDatabase.COL_6_EMAIL,
                ProfileDatabase.COL_7_PHONENUMBER,
                ProfileDatabase.COL_8_HEIGHT,
                ProfileDatabase.COL_9_START_WEIGHT,
                ProfileDatabase.COL_10_GOAL_WEIGHT,
                ProfileDatabase.COL_11_AGE,
                ProfileDatabase.COL_12_GENDER
        };
        String selection = ProfileDatabase.COL_2_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                ProfileDatabase.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()){
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(ProfileDatabase.COL_4_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(ProfileDatabase.COL_5_LAST_NAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(ProfileDatabase.COL_6_EMAIL));
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ProfileDatabase.COL_7_PHONENUMBER));
            String height = cursor.getString(cursor.getColumnIndexOrThrow(ProfileDatabase.COL_8_HEIGHT));
            String startWeight = cursor.getString(cursor.getColumnIndexOrThrow(ProfileDatabase.COL_9_START_WEIGHT));
            String goalWeight = cursor.getString(cursor.getColumnIndexOrThrow(ProfileDatabase.COL_10_GOAL_WEIGHT));
            String age = cursor.getString(cursor.getColumnIndexOrThrow(ProfileDatabase.COL_11_AGE));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(ProfileDatabase.COL_12_GENDER));

            if (Objects.equals(firstName, null)) {
                firstNameHolder.setHint("Michael");
            } else{firstNameHolder.setHint(firstName);}
            if (Objects.equals(lastName, null)) {
                lastNameHolder.setHint("Scarn");
            } else {lastNameHolder.setHint(lastName);}
            if (Objects.equals(email, null)){
                emailHolder.setHint("ThreatLevel@Midnight.com");
            } else {emailHolder.setHint(email);}
            if (Objects.equals(phoneNumber, null)){
                phoneHolder.setHint("(555)-555-5555");
            } else {
                String formattedNumber = formatPhoneNumber(phoneNumber);
                phoneHolder.setHint(formattedNumber);}
            if (Objects.equals(height, null)){
                heightHolder.setHint("5'9\"");
            } else {heightHolder.setHint(height);}
            if (Objects.equals(startWeight, null)){
                startWeightHolder.setHint("Your starting weight");
            } else {
                startWeightHolder.setHint(startWeight);}
            if (Objects.equals(goalWeight, null)){
                goalWeightHolder.setHint("Your goal weight");
            } else { goalWeightHolder.setHint(goalWeight);}
            if (Objects.equals(age, null)){
                ageHolder.setHint("48");
            } else {ageHolder.setHint(age);}
            if (!Objects.equals(gender, null)){
                setSelectedRadioButton(gender);
            }
        } else {
            Log.e("ProfileFragment", "No profile data found for user: " + username);
        }
        cursor.close();
        db.close();
    }

    private Boolean updateProfile(){
        if (username == null){
            Log.e("ProfileFragment", "Username is null, can't update profile");
            return false;
        }

        String firstName = firstNameHolder.getText().toString().trim();
        String lastName = lastNameHolder.getText().toString().trim();
        String email = emailHolder.getText().toString().trim();
        String phoneNumber = phoneHolder.getText().toString().trim();
        String height = heightHolder.getText().toString().trim();
        String startWeight = startWeightHolder.getText().toString().trim();
        String goalWeight = goalWeightHolder.getText().toString().trim();
        String age = ageHolder.getText().toString().trim();
        String gender = getGenderSelection();

        SQLiteDatabase db = profileDB.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ProfileDatabase.COL_4_FIRST_NAME, firstName);
        values.put(ProfileDatabase.COL_5_LAST_NAME, lastName);
        values.put(ProfileDatabase.COL_6_EMAIL, email);
        values.put(ProfileDatabase.COL_7_PHONENUMBER, phoneNumber);
        values.put(ProfileDatabase.COL_8_HEIGHT, height);
        values.put(ProfileDatabase.COL_9_START_WEIGHT, startWeight);
        values.put(ProfileDatabase.COL_10_GOAL_WEIGHT, goalWeight);
        values.put(ProfileDatabase.COL_11_AGE, age);
        values.put(ProfileDatabase.COL_12_GENDER, gender);

        int rowsAffected = db.update(ProfileDatabase.TABLE_NAME, values, ProfileDatabase.COL_2_USERNAME + " = ?",
                new String[]{username}
        );
        db.close();

        if (rowsAffected > 0){
            return true;
        }
         return false;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}