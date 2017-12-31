package de.ub0r.android.choosebrowser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import de.ub0r.android.logg0r.Log;

public class ChooserActivity extends AppCompatActivity {

    private static final String TAG = "ChooserActivity";
    private IntentParser mParser = new IntentParser();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Uri uri = mParser.parseIntent(this, getIntent());
        if (uri != null) {
            showChooser(uri);
            finish();
        } else {
            setContentView(R.layout.activity_chooser);
            Toast.makeText(this, R.string.error_unsupported_link, Toast.LENGTH_LONG).show();
        }
    }

    private void showChooser(final Uri uri) {
        Log.d(TAG, "showChooser for uri ", uri);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        final Intent chooser = Intent.createChooser(intent, getString(R.string.chooser_title));

        startActivity(chooser);
    }
}
