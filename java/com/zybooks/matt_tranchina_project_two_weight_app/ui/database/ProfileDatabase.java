package com.zybooks.matt_tranchina_project_two_weight_app.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zybooks.matt_tranchina_project_two_weight_app.ui.profile.ProfileDataModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileDatabase extends SQLiteOpenHelper {

    // Static variables for database table
    static final String DATABASE_NAME = "Weight_Tracker.db";
    static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "UserTable";
    public static final String COL_1_ID = "_id";
    public static final String COL_2_USERNAME = "username";
    public static final String COL_3_PASSWORD = "password";
    public static final String COL_4_FIRST_NAME = "first_name";
    public static final String COL_5_LAST_NAME = "last_name";
    public static final String COL_6_EMAIL = "email";
    public static final String COL_7_PHONENUMBER = "phonenumber";
    public static final String COL_8_HEIGHT = "height";
    public static final String COL_9_START_WEIGHT = "start_weight";
    public static final String COL_10_GOAL_WEIGHT = "goal_weight";
    public static final String COL_11_AGE = "age";
    public static final String COL_12_GENDER = "gender";


    // Create data table for user profile
    private static final String SQL_CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " ( " +
            COL_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_2_USERNAME + " TEXT UNIQUE, " +
            COL_3_PASSWORD + " TEXT, " +
            COL_4_FIRST_NAME + " TEXT, " +
            COL_5_LAST_NAME + " TEXT, " +
            COL_6_EMAIL + " TEXT, " +
            COL_7_PHONENUMBER + " TEXT, " +
            COL_8_HEIGHT + " TEXT, " +
            COL_9_START_WEIGHT + " TEXT, " +
            COL_10_GOAL_WEIGHT + " TEXT, " +
            COL_11_AGE + " TEXT, " +
            COL_12_GENDER + " TEXT)";

    // Constructor
    public ProfileDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // CREATE
    public long createUser(ProfileDataModel profileDataModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_2_USERNAME, profileDataModel.getUser_username());
        values.put(COL_3_PASSWORD, profileDataModel.getUser_password());
        values.put(COL_4_FIRST_NAME, profileDataModel.getUser_firstName());
        values.put(COL_5_LAST_NAME, profileDataModel.getUser_lastName());
        values.put(COL_6_EMAIL, profileDataModel.getUser_email());
        values.put(COL_7_PHONENUMBER, profileDataModel.getUser_phonenumber());
        values.put(COL_8_HEIGHT, profileDataModel.getUser_height());
        values.put(COL_9_START_WEIGHT, profileDataModel.getUser_start_weight());
        values.put(COL_10_GOAL_WEIGHT, profileDataModel.getUser_goal_weight());
        values.put(COL_11_AGE, profileDataModel.getUser_age());
        values.put(COL_12_GENDER, profileDataModel.getUser_gender());


        long newUser = db.insert(TABLE_NAME, null, values);
        db.close();
        return newUser;

    }

    public boolean updateUser(ProfileDataModel profileDataModel, String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_4_FIRST_NAME, profileDataModel.getUser_firstName());
        values.put(COL_5_LAST_NAME, profileDataModel.getUser_lastName());
        values.put(COL_6_EMAIL, profileDataModel.getUser_email());
        values.put(COL_7_PHONENUMBER, profileDataModel.getUser_phonenumber());
        values.put(COL_8_HEIGHT, profileDataModel.getUser_height());
        values.put(COL_9_START_WEIGHT, profileDataModel.getUser_start_weight());
        values.put(COL_10_GOAL_WEIGHT, profileDataModel.getUser_goal_weight());
        values.put(COL_11_AGE, profileDataModel.getUser_age());
        values.put(COL_12_GENDER, profileDataModel.getUser_gender());

        int updated = db.update(TABLE_NAME, values, COL_2_USERNAME + " =?", new String[]{username});
        db.close();
        if (updated > 0){
            return true;
        }
        return false;
    }

    // DELETE
    public boolean deleteUser(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        long rowDeleted = db.delete(TABLE_NAME, COL_2_USERNAME + " = ? ",
                new String[]{username});
        db.close();
        return rowDeleted > 0;
    }

    // READ for login activity
    public boolean searchProfile(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[] {COL_1_ID, COL_2_USERNAME, COL_3_PASSWORD, COL_4_FIRST_NAME, COL_5_LAST_NAME,
                        COL_6_EMAIL, COL_7_PHONENUMBER, COL_8_HEIGHT, COL_9_START_WEIGHT, COL_10_GOAL_WEIGHT,
                        COL_11_AGE, COL_12_GENDER},
                COL_2_USERNAME + " =? AND " + COL_3_PASSWORD + "=?",
                new String[] {username, password}, null, null, null);

        boolean inDatabase = cursor.getCount() > 0;
        cursor.close();
        return inDatabase;
    }

    public ProfileDataModel getProfileData(String username){
        SQLiteDatabase db = this. getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COL_2_USERNAME, COL_3_PASSWORD, COL_4_FIRST_NAME, COL_5_LAST_NAME,
                        COL_6_EMAIL, COL_7_PHONENUMBER, COL_8_HEIGHT, COL_9_START_WEIGHT,COL_10_GOAL_WEIGHT,
                        COL_11_AGE, COL_12_GENDER},
                COL_2_USERNAME + " =?",
                new String[]{username}, null, null, null);
        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COL_4_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COL_5_LAST_NAME));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(COL_6_EMAIL));
        String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COL_7_PHONENUMBER));
        String height = cursor.getString(cursor.getColumnIndexOrThrow(COL_8_HEIGHT));
        String startWeight = cursor.getString(cursor.getColumnIndexOrThrow(COL_9_START_WEIGHT));
        String goalWeight = cursor.getString(cursor.getColumnIndexOrThrow(COL_10_GOAL_WEIGHT));
        String age = cursor.getString(cursor.getColumnIndexOrThrow(COL_11_AGE));
        String gender = cursor.getString(cursor.getColumnIndexOrThrow(COL_12_GENDER));

        ProfileDataModel profileDataModel = new ProfileDataModel(firstName, lastName, email, phoneNumber, height,
                startWeight,goalWeight, age, gender);

        cursor.close();
        db.close();

        return profileDataModel;
    }

    // Method to read all data from the table and return it as a List
public List<ProfileDataModel> getAllData() {
        List<ProfileDataModel> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {

            cursor = db.query(TABLE_NAME,
                    new String[]{COL_1_ID, COL_2_USERNAME, COL_3_PASSWORD, COL_4_FIRST_NAME, COL_5_LAST_NAME,
                    COL_6_EMAIL, COL_7_PHONENUMBER, COL_8_HEIGHT, COL_9_START_WEIGHT,COL_10_GOAL_WEIGHT,
                            COL_11_AGE, COL_12_GENDER},
                    null, null, null, null, null); // No WHERE clause, all rows

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Get the data for each column from the current row.
                    // Use getColumnIndexOrThrow() to get the column index by name
                    String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COL_4_FIRST_NAME));
                    String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COL_5_LAST_NAME));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(COL_6_EMAIL));
                    String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COL_7_PHONENUMBER));
                    String height = cursor.getString(cursor.getColumnIndexOrThrow(COL_8_HEIGHT));
                    String startWeight = cursor.getString(cursor.getColumnIndexOrThrow(COL_9_START_WEIGHT));
                    String goalWeight = cursor.getString(cursor.getColumnIndexOrThrow(COL_10_GOAL_WEIGHT));
                    String age = cursor.getString(cursor.getColumnIndexOrThrow(COL_11_AGE));
                    String gender = cursor.getString(cursor.getColumnIndexOrThrow(COL_12_GENDER));

                    ProfileDataModel dataModel = new ProfileDataModel(firstName, lastName, email, phoneNumber,
                            height, startWeight, goalWeight, age, gender);
                    dataList.add(dataModel);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProfileDataBase", "Error reading data: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return dataList;
    }

}