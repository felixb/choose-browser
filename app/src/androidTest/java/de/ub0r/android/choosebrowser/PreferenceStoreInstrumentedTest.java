package de.ub0r.android.choosebrowser;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PreferenceStoreInstrumentedTest {

    @Rule
    public ActivityTestRule<ChooserActivity> mActivityRule =
            new ActivityTestRule<>(ChooserActivity.class);

    private SharedPreferences getPreferences() {
        return mActivityRule.getActivity().getSharedPreferences("test", Context.MODE_PRIVATE);
    }

    @Test
    public void isEmptyOnEmptyStore() throws Exception {
        final SharedPreferences preferences = getPreferences();
        final PreferenceStore store = new PreferenceStore(preferences);

        final String key = "some-key";
        assertFalse(store.contains(key));
        assertNull(store.get(key));
    }

    @Test
    public void addPreferredAppAndRetrieve() throws Exception {
        final SharedPreferences preferences = getPreferences();
        final PreferenceStore store = new PreferenceStore(preferences);
        final ComponentName componentName = new ComponentName("pkg", "cls");

        final String key = "some-other-key";
        store.put(key, componentName);
        assertTrue(store.contains(key));
        assertEquals(componentName, store.get(key));

        store.remove(key);
        assertFalse(store.contains(key));
        assertNull(store.get(key));
    }

    @Before
    public void tearUp() {
        getPreferences().edit().clear().apply();
    }

    @After
    public void tearDown() {
        getPreferences().edit().clear().apply();
    }
}
