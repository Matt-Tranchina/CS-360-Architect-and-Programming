package com.zybooks.matt_tranchina_project_two_weight_app.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zybooks.matt_tranchina_project_two_weight_app.R;
import com.zybooks.matt_tranchina_project_two_weight_app.WeightEntry;
import com.zybooks.matt_tranchina_project_two_weight_app.databinding.FragmentHomeBinding;
import com.zybooks.matt_tranchina_project_two_weight_app.ui.database.ProfileDatabase;
import com.zybooks.matt_tranchina_project_two_weight_app.ui.database.WeightDatabase;
import com.zybooks.matt_tranchina_project_two_weight_app.ui.login.LoginActivity;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    private LineChart lineChart;
    private WeightDatabase weightDB;
    private ProfileDatabase profileDB;
    private String finalStartWeight;
    private String finalGoalWeight;
    FloatingActionButton addWeightbutton;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lineChart = view.findViewById(R.id.weight_chart);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        weightDB = new WeightDatabase(getContext());
        profileDB = new ProfileDatabase(getContext());
        String username = LoginActivity.PASS_USERNAME;
        addWeightbutton = view.findViewById(R.id.floatingAddWeightButton);

        getUserWeights(username);

        if (Objects.equals(finalStartWeight, null) && Objects.equals(finalGoalWeight, null)) {
            showSetUpDialog(view);
        } else {
            loadData();
        }

        addWeightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddWeightDialog();
            }
        });

    }
    public void showAddWeightDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle("Add Weight");

        View dialogView = getLayoutInflater().inflate(R.layout.add_weight_dialog, null);
        builder.setView(dialogView);

        EditText weightEditText = dialogView.findViewById(R.id.weightEditText);
        TextView dateTextView = dialogView.findViewById(R.id.dateTextView);

        // Set date to today initially
        Calendar calendar = Calendar.getInstance();
        updateDateTextView(dateTextView, calendar);

        dateTextView.setOnClickListener(v -> showDatePickerDialog(dateTextView, calendar));

        builder.setPositiveButton("Save", (dialog, which) -> {
            String weightStr = weightEditText.getText().toString();
            if (!weightStr.isEmpty()) {
                try {
                    java.util.Date utilDate = new java.util.Date();
                    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                    float weight = Float.parseFloat(weightStr);
                    saveWeightEntry(sqlDate, weight);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Error saving date", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    private void getUserWeights(String username) {
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

        if (cursor.moveToFirst()) {
            finalGoalWeight = cursor.getString(cursor.getColumnIndexOrThrow(ProfileDatabase.COL_10_GOAL_WEIGHT));
            finalStartWeight = cursor.getString(cursor.getColumnIndexOrThrow(ProfileDatabase.COL_9_START_WEIGHT));

        }
        cursor.close();
    }

    private void saveWeightEntry(Date time, float weight) {
        try {
            WeightEntry newEntry = new WeightEntry(time, weight);
            if (weightDB.addWeight(newEntry) > 0) {
                loadData();
            } else {
                Toast.makeText(getContext(), "Error saving date", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.d("HomeFragment", "ERROR: " + e);
        }
    }

    private void drawLineChart() {
        int min = Integer.parseInt(finalGoalWeight);
        int max = Integer.parseInt(finalStartWeight);
        int length = 10;

        List<Entry> lineEntries = new ArrayList<>();
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Weight Loss");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setVisibleXRangeMaximum(31);
        lineChart.setData(lineData);

        // Setup X Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.DKGRAY);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(31);

        // Setup Y Axis
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(min);
        yAxis.setAxisMaximum(max);
        yAxis.setTextColor(Color.DKGRAY);


        lineChart.getAxisLeft().setCenterAxisLabels(false);
        lineChart.getAxisLeft().setValueFormatter(new ValueFormatter() {

        });
        lineChart.invalidate();
    }

    private void showSetUpDialog(View view){
        new AlertDialog.Builder(requireContext())
                .setTitle("Set up your profile!")
                .setMessage("Please set up your profile to start tracking your weight")
                .setPositiveButton("Let's go!", (dialog, which) -> {
                    try {
                        NavController navController = Navigation.findNavController(view);
                        navController.navigate(R.id.navigation_profile);
                        dialog.cancel();

                    } catch (Exception e) {
                        Log.e("HomeFragment", "ERROR: Fragent not hosted by LoginActivity");
                    }
                })
                .setNegativeButton("Not now", (dialog, which) -> {
                    dialog.cancel();
                })
                .show();

    }


    private void loadData() {
        int min = Integer.parseInt(finalGoalWeight);
        int max = Integer.parseInt(finalStartWeight);

        List<WeightEntry> weights = weightDB.getAllWeights();
        if (!weights.isEmpty()) {
            List<Entry> entries = new ArrayList<>();
            for (WeightEntry weight : weights) {
                entries.add(new Entry(weight.getDate().getTime(), weight.getWeight()));
            }
            lineChart.getDescription().setEnabled(false);
            lineChart.getAxis(YAxis.AxisDependency.LEFT);
            lineChart.getAxisRight().setEnabled(false);

            LineDataSet dataSet = new LineDataSet(entries, "Weight Over Time");
            dataSet.setColor(Color.MAGENTA);
            dataSet.setValueTextSize(12f);
            dataSet.setLineWidth(4f);
            dataSet.setCircleRadius(8f);
            dataSet.setCircleColor(Color.MAGENTA);

            LineData lineData = new LineData(dataSet);
            // Setup X Axis
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextColor(Color.DKGRAY);
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(31);

            // Setup Y Axis
            YAxis yAxis = lineChart.getAxisLeft();
            yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            yAxis.setAxisMinimum(min);
            yAxis.setAxisMaximum(max);
            yAxis.setTextColor(Color.DKGRAY);

            lineChart.setData(lineData);
            lineChart.invalidate();
        } else {
            drawLineChart();
        }


    }


    private void showDatePickerDialog(final TextView dateTextView, final Calendar calendar) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    updateDateTextView(dateTextView, calendar);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }



    private void updateDateTextView(TextView dateTextView, Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        dateTextView.setText(dateFormat.format(calendar.getTime()));
    }




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        View weightChart = view.findViewById(R.id.weight_chart);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}