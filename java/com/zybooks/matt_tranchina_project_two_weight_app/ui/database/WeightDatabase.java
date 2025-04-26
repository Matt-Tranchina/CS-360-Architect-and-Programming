package com.zybooks.matt_tranchina_project_two_weight_app.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.zybooks.matt_tranchina_project_two_weight_app.WeightEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeightDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weight_tracker.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_WEIGHTS = "weights";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_WEIGHT = "weight";

    //Date format for database
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public WeightDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    public WeightDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_WEIGHTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_WEIGHT + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHTS);
        onCreate(db);
    }

    public long addWeight(WeightEntry weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, dateFormat.format(weight.getDate()));
        values.put(COLUMN_WEIGHT, weight.getWeight());
        long id = db.insert(TABLE_WEIGHTS, null, values);
        db.close();
        return id;
    }

    public List<WeightEntry> getAllWeights() {
        List<WeightEntry> weights = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_WEIGHTS + " ORDER BY " + COLUMN_DATE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    Date date = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                    float weight = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT));
                    weights.add(new WeightEntry(id, date, weight));
                } catch (ParseException e) {
                    // Handle date parsing errors, log them
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return weights;
    }
}
