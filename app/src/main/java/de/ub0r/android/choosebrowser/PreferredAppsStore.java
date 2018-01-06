package de.ub0r.android.choosebrowser;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import java.util.Set;

class PreferredAppsStore {
    private static final String PREF_STORE = "preferred_apps_store";

    private final SharedPreferences mBackend;

    PreferredAppsStore(final Context context) {
        this(context.getSharedPreferences(PREF_STORE, Context.MODE_PRIVATE));
    }

    PreferredAppsStore(final SharedPreferences backend) {
        mBackend = backend;
    }


    String uriToKey(final Uri uri) {
        return uri.getHost();
    }

    boolean contains(final String key) {
        return mBackend.contains(key);
    }

    ComponentName get(final String key) {
        final String component = mBackend.getString(key, null);
        return component != null ? ComponentName.unflattenFromString(component) : null;
    }

    void put(final String key, final ComponentName component) {
        mBackend.edit().putString(key, component.flattenToShortString()).apply();
    }

    Set<String> list() {
        return mBackend.getAll().keySet();
    }

    void remove(final String key) {
        mBackend.edit().remove(key).apply();
    }
}
