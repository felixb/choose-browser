package de.ub0r.android.choosebrowser;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class IntentParserInstrumentedTest {

    private static final String EXPECTED_URL = "http://example.com/url";
    private final IntentParser mParser = new IntentParser();

    @Rule
    public ActivityTestRule<ChooserActivity> mActivityRule =
            new ActivityTestRule<>(ChooserActivity.class);

    @Test
    public void parseIntentWithData() throws Exception {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXPECTED_URL));
        assertEquals(EXPECTED_URL, mParser.parseIntent(intent).toString());
    }

    @Test
    public void parseIntentWithExtraText() throws Exception {
        final Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_TEXT, "some text with http://example.com/url");
        assertEquals(EXPECTED_URL, mParser.parseIntent(intent).toString());
    }

    @Test
    public void parseIntentWithClipDataTextAndUrl() throws Exception {
        ClipData clipData = ClipData.newPlainText("some-label", "some text with http://example.com/url");
        Intent intent = new Intent();
        intent.setClipData(clipData);
        assertEquals(EXPECTED_URL, mParser.parseIntent(intent).toString());
    }

    @Test
    public void parseIntentWithClipDataTextAndNoUrl() throws Exception {
        ClipData clipData = ClipData.newPlainText("some-label", "some text");
        Intent intent = new Intent();
        intent.setClipData(clipData);
        assertNull(mParser.parseIntent(intent));
    }

    @Test
    public void parseIntentWithClipDataRawUri() throws Exception {
        ClipData clipData = ClipData.newRawUri("some-label", Uri.parse(EXPECTED_URL));
        Intent intent = new Intent();
        intent.setClipData(clipData);
        assertEquals(EXPECTED_URL, mParser.parseIntent(intent).toString());
    }

    @Test
    public void parseIntentWithClipDataRawContentUri() throws Exception {
        ClipData clipData = ClipData.newRawUri("some-label", Uri.parse("content://some.provider/foo"));
        Intent intent = new Intent();
        intent.setClipData(clipData);
        assertNull(mParser.parseIntent(intent));
    }
}
