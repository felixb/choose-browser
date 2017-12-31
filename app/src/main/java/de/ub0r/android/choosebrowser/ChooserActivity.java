package de.ub0r.android.choosebrowser;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.ub0r.android.logg0r.Log;

public class ChooserActivity extends AppCompatActivity {

    private static final String TAG = "ChooserActivity";
    private TextParser mParser = new TextParser();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        Log.d(TAG, "launched with intent action %s, data %s", intent.getAction(), intent.getData());
        final ClipData clipData = intent.getClipData();
        final Uri uri = mParser.parseClipData(clipData);
        if (uri != null) {
            showChooser(uri);
        }
        setContentView(R.layout.activity_chooser);
    }

    private void showChooser(final Uri uri) {
        Log.d(TAG, "showChooser for uri ", uri);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        final Intent chooser = Intent.createChooser(intent, getString(R.string.chooser_title));

        startActivity(chooser);
        finish();
    }
}
