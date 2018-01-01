package de.ub0r.android.choosebrowser;

import android.content.ComponentName;
import android.content.SharedPreferences;

class PreferenceStore {
    private final SharedPreferences mBackend;

    PreferenceStore(final SharedPreferences backend) {
        mBackend = backend;
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

    void remove(final String key) {
        mBackend.edit().remove(key).apply();
    }
}
