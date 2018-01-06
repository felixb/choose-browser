package de.ub0r.android.choosebrowser;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ChooserFullscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        final Uri uri = getIntent().getData();
        assert uri != null; // this activity is called only by ChooserActivity with data set
        final ChooserFragment f = ChooserFragment.newInstance(uri, false);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, f)
                .commit();
    }
}
