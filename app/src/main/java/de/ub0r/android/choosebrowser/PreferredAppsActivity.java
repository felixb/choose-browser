package de.ub0r.android.choosebrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class PreferredAppsActivity extends AppCompatActivity {

    private PreferenceStore mStore;
    private RecyclerView mList;
    private PreferredAppsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_apps);

        //noinspection ConstantConditions
        getSupportActionBar().setSubtitle(R.string.preferred_apps);

        mStore = new PreferenceStore(this);
        mList = findViewById(android.R.id.list);
        mAdapter = new PreferredAppsAdapter(this, mStore);
        mList.setAdapter(mAdapter);
        mList.setLayoutManager(new LinearLayoutManager(this));
    }
}
