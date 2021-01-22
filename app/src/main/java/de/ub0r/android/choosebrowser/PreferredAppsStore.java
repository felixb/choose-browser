package de.ub0r.android.choosebrowser;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.annotation.NonNull;

import java.util.Set;

class PreferredAppsStore {
    private static final String PREF_STORE = "preferred_apps_store";

    private final SharedPreferences mBackend;

    PreferredAppsStore(@NonNull final Context context) {
        this(context.getSharedPreferences(PREF_STORE, Context.MODE_PRIVATE));
    }

    PreferredAppsStore(@NonNull final SharedPreferences backend) {
        mBackend = backend;
    }

    @NonNull
    private String uriToKey(@NonNull final Uri uri) {
        return uri.getHost();
    }

    boolean contains(@NonNull final String key) {
        return mBackend.contains(key);
    }

    ComponentName get(@NonNull final Uri uri) {
        return get(uriToKey(uri));
    }

    ComponentName get(@NonNull final String key) {
        final String component = mBackend.getString(key, null);
        return component != null ? ComponentName.unflattenFromString(component) : null;
    }

    void put(@NonNull final Uri uri, @NonNull final ComponentName component) {
        put(uriToKey(uri), component);
    }

    void put(@NonNull final String key, @NonNull final ComponentName component) {
        mBackend.edit().putString(key, component.flattenToShortString()).apply();
    }

    @NonNull
    Set<String> list() {
        return mBackend.getAll().keySet();
    }

    void remove(@NonNull final Uri uri) {
        remove(uriToKey(uri));
    }

    void remove(@NonNull final String key) {
        mBackend.edit().remove(key).apply();
    }
}
