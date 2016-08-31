package com.example.android.finalprojectmal;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by michael on 16/08/16.
 */

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }
}
