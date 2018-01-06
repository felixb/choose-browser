package de.ub0r.android.choosebrowser;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.List;

import de.ub0r.android.logg0r.Log;

public class ChooserFragment extends AppCompatDialogFragment {

    private static final String TAG = "ChooserFragment";
    private static final String EXTRA_URI = "uri";
    private static final String EXTRA_SHOW_AS_DIALOG = "showAsDialog";

    private CheckedTextView mRememberPreference;
    private RecyclerView mList;
    private ChooserAdapter mAdapter;

    static ChooserFragment newInstance(@NonNull final Uri uri, final boolean showAsDialog) {
        ChooserFragment f = new ChooserFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_URI, uri.toString());
        args.putBoolean(EXTRA_SHOW_AS_DIALOG, showAsDialog);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments().getBoolean(EXTRA_SHOW_AS_DIALOG)) {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog);
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chooser, container, false);
    }

    @Override
    public void onViewCreated(final View container, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);
        showChooser(container);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.activity_chooser, menu);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(getContext(), SettingsActivity.class));
                return true;
            case R.id.menu_preferred_apps:
                startActivity(new Intent(getContext(), PreferredAppsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void finish() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            activity.finish();
        }
    }

    private void setTitle(@StringRes final int title) {
        final FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setTitle(title);
        }
        final Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setTitle(title);
        }
    }

    private void setSubtitle(final String subtitle) {
        final FragmentActivity activity = getActivity();
        if (activity != null) {
            final AppCompatActivity compatActivity = (AppCompatActivity) activity;
            if (compatActivity.getSupportActionBar() != null) {
                compatActivity.getSupportActionBar().setSubtitle(subtitle);
            }
        }
        final Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setTitle(String.format("%s - %s", getString(R.string.app_name), subtitle));
        }
    }

    private void showChooser(final View container) {
        final Uri uri = Uri.parse(getArguments().getString(EXTRA_URI));
        Log.d(TAG, "showChooser for uri ", uri);

        setTitle(R.string.app_name);
        setSubtitle(uri.toString());

        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        final PackageManager pm = getContext().getPackageManager();
        final List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        mRememberPreference = container.findViewById(R.id.remember_preference);
        mRememberPreference.setChecked(prefs.getBoolean(SettingsActivity.PREF_NAME_REMEMBER_BY_DEFAULT, SettingsActivity.PREF_DEFAULT_REMEMBER_BY_DEFAULT));
        mRememberPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mRememberPreference.toggle();
            }
        });

        mList = container.findViewById(android.R.id.list);
        mAdapter = new ChooserAdapter(getContext(), new ChooserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final ComponentName component) {
                optionallyStorePreferredApp(uri, component);
                startActivity(uri, component);
            }
        }, infos);
        mList.setAdapter(mAdapter);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void startActivity(@NonNull final Uri uri, @NonNull final ComponentName component) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setComponent(component);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void optionallyStorePreferredApp(final Uri uri, final ComponentName component) {
        if (mRememberPreference.isChecked()) {
            final PreferredAppsStore store = new PreferredAppsStore(getContext());
            store.put(store.uriToKey(uri), component);
        }
    }
}
