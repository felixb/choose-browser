package de.ub0r.android.choosebrowser;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import de.ub0r.android.logg0r.Log;

public class ChooserActivity extends AppCompatActivity {

    private static final String TAG = "ChooserActivity";

    private final IntentParser mParser = new IntentParser();
    private PreferredAppsStore mStore;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStore = new PreferredAppsStore(this);
        final Uri uri = mParser.parseIntent(getIntent());

        if (uri == null) {
            Toast.makeText(this, R.string.error_unsupported_link, Toast.LENGTH_LONG).show();
            finish();
        } else if (!startPreferredApp(uri)) {
            showChooser(uri);
        }
    }

    private boolean startPreferredApp(@NonNull final Uri uri) {
        final ComponentName component = getPreferredApp(uri);
        if (component != null) {
            try {
                startActivity(uri, component);
                return true;
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "error starting app: ", component, e);
                Toast.makeText(this, R.string.error_app_not_found, Toast.LENGTH_LONG).show();
                removePreferredApp(uri);
            }
        }
        return false;
    }

    private void showChooser(@NonNull Uri uri) {
        if (getResources().getBoolean(R.bool.showChooserAsDialog)) {
            showChooserAsDialog(uri);
        } else {
            showChooserFullscreen(uri);
        }
    }

    private void showChooserAsDialog(@NonNull final Uri uri) {
        ChooserFragment.newInstance(uri, true).show(getSupportFragmentManager(), "dialog");
    }

    private void showChooserFullscreen(@NonNull final Uri uri) {
        final Intent intent = new Intent(getIntent());
        intent.setClass(this, ChooserFullscreenActivity.class);
        intent.setData(uri);
        startActivity(intent);
        finish();
    }

    private void startActivity(@NonNull final Uri uri, @NonNull final ComponentName component) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setComponent(component);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private ComponentName getPreferredApp(@NonNull final Uri uri) {
        return mStore.get(mStore.uriToKey(uri));
    }

    private void removePreferredApp(@NonNull final Uri uri) {
        mStore.remove(mStore.uriToKey(uri));
    }
}
