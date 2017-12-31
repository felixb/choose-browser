package de.ub0r.android.choosebrowser;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import de.ub0r.android.logg0r.Log;

public class ChooserActivity extends AppCompatActivity {

    private static final String TAG = "ChooserActivity";
    private IntentParser mParser = new IntentParser();
    private RecyclerView mList;
    private ResolverAdapter mAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        final Uri uri = mParser.parseIntent(this, getIntent());
        if (uri != null) {
            showChooser(uri);
        } else {
            Toast.makeText(this, R.string.error_unsupported_link, Toast.LENGTH_LONG).show();
        }
    }

    private void showChooser(final Uri uri) {
        Log.d(TAG, "showChooser for uri ", uri);

        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);

        mList = findViewById(android.R.id.list);
        mAdapter = new ResolverAdapter(this, intent, infos);
        mList.setAdapter(mAdapter);
        mList.setLayoutManager(new LinearLayoutManager(this));
    }
}
