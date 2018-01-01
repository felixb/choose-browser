package de.ub0r.android.choosebrowser;

import android.annotation.SuppressLint;
import android.os.Bundle;

@SuppressLint("ExportedPreferenceActivity")
public class SettingsActivity extends AppCompatPreferenceActivity {

    static final String PREF_NAME_REMEMBER_BY_DEFAULT = "remember_by_default";
    static final Boolean PREF_DEFAULT_REMEMBER_BY_DEFAULT = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setSubtitle(R.string.settings);
        //noinspection deprecation
        addPreferencesFromResource(R.xml.pref_general);
    }
}
