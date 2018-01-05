package de.ub0r.android.choosebrowser;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PreferredAppsStoreInstrumentedTest {

    @Rule
    public ActivityTestRule<ChooserActivity> mActivityRule =
            new ActivityTestRule<>(ChooserActivity.class);

    private SharedPreferences getPreferences() {
        final SharedPreferences preferences = mActivityRule.getActivity()
                .getSharedPreferences("test", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        return preferences;
    }

    @Test
    public void isEmptyOnEmptyStore() throws Exception {
        final SharedPreferences preferences = getPreferences();
        final PreferredAppsStore store = new PreferredAppsStore(preferences);

        final String key = "some-key";
        assertFalse(store.contains(key));
        assertNull(store.get(key));
    }

    @Test
    public void addPreferredAppAndRetrieve() throws Exception {
        final SharedPreferences preferences = getPreferences();
        final PreferredAppsStore store = new PreferredAppsStore(preferences);
        final ComponentName componentName = new ComponentName("pkg", "cls");

        final String key = "some-other-key";
        store.put(key, componentName);
        assertTrue(store.contains(key));
        assertEquals(componentName, store.get(key));

        store.remove(key);
        assertFalse(store.contains(key));
        assertNull(store.get(key));
    }

    @Test
    public void listKeys() throws Exception {
        final SharedPreferences preferences = getPreferences();
        final PreferredAppsStore store = new PreferredAppsStore(preferences);
        final ComponentName componentName = new ComponentName("pkg", "cls");

        assertEquals(0, store.list().size());

        final String key = "some-other-key";
        store.put(key, componentName);

        assertEquals(1, store.list().size());
        assertTrue(store.list().contains(key));
    }

    @After
    public void tearDown() {
        getPreferences();
    }
}
