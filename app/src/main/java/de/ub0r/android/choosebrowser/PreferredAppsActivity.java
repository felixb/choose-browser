package de.ub0r.android.choosebrowser;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class PreferredAppsActivity extends AppCompatActivity implements PreferredAppsAdapter.DeleteItemListener {

    private PreferredAppsStore mStore;
    private RecyclerView mList;
    private View mEmptyListView;
    private PreferredAppsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_apps);

        //noinspection ConstantConditions
        getSupportActionBar().setSubtitle(R.string.preferred_apps);

        mStore = new PreferredAppsStore(this);
        mList = findViewById(android.R.id.list);
        mEmptyListView = findViewById(R.id.empty_list);
        mAdapter = new PreferredAppsAdapter(this, mStore);
        mAdapter.setOnItemDeleteListener(this);
        mList.setAdapter(mAdapter);
        mList.setLayoutManager(new LinearLayoutManager(this));

        onItemDeleted();
    }

    @Override
    public void onItemDeleted() {
        if (mAdapter.getItemCount() > 0) {
            mList.setVisibility(View.VISIBLE);
            mEmptyListView.setVisibility(View.GONE);
        } else {
            mList.setVisibility(View.GONE);
            mEmptyListView.setVisibility(View.VISIBLE);
        }
    }
}
