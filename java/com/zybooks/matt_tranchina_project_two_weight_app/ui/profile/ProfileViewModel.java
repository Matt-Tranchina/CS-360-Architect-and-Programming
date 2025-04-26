package com.zybooks.matt_tranchina_project_two_weight_app.ui.profile;

import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zybooks.matt_tranchina_project_two_weight_app.R;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;



    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");

    }

    public LiveData<String> getText() {
        return mText;
    }

}
