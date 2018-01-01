package de.ub0r.android.choosebrowser;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.Toast;

import java.util.List;

import de.ub0r.android.logg0r.Log;

public class ChooserActivity extends AppCompatActivity {

    private static final String TAG = "ChooserActivity";
    private static final String PREF_STORE = "preference_store";

    private IntentParser mParser = new IntentParser();
    private PreferenceStore mStore;

    private CheckedTextView mRemeberPreference;
    private RecyclerView mList;
    private ResolverAdapter mAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        mStore = new PreferenceStore(getSharedPreferences(PREF_STORE, MODE_PRIVATE));

        final Uri uri = mParser.parseIntent(this, getIntent());
        if (uri != null) {
            final ComponentName component = getPreferredApp(uri);
            if (component != null) {
                startActivity(uri, component);
            } else
                showChooser(uri);
        } else {
            Toast.makeText(this, R.string.error_unsupported_link, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_chooser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showChooser(final Uri uri) {
        Log.d(TAG, "showChooser for uri ", uri);

        //noinspection ConstantConditions
        getSupportActionBar().setSubtitle(uri.toString());

        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mRemeberPreference = findViewById(R.id.remember_preference);
        mRemeberPreference.setChecked(prefs.getBoolean(SettingsActivity.PREF_NAME_REMEMBER_BY_DEFAULT, SettingsActivity.PREF_DEFAULT_REMEMBER_BY_DEFAULT));
        mRemeberPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mRemeberPreference.toggle();
            }
        });

        mList = findViewById(android.R.id.list);
        mAdapter = new ResolverAdapter(this, new ResolverAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final ComponentName component) {
                optionallyStorePreferredApp(uri, component);
                startActivity(uri, component);
            }
        }, infos);
        mList.setAdapter(mAdapter);
        mList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void startActivity(final Uri uri, final ComponentName component) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setComponent(component);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private String uriToKey(final Uri uri) {
        return uri.getHost();
    }

    private void optionallyStorePreferredApp(final Uri uri, final ComponentName component) {
        if (mRemeberPreference.isChecked()) {
            mStore.put(uriToKey(uri), component);
        }
    }

    private ComponentName getPreferredApp(final Uri uri) {
        return mStore.get(uriToKey(uri));
    }
}
