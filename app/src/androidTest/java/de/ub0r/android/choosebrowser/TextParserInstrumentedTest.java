package de.ub0r.android.choosebrowser;

import android.content.ClipData;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TextParserInstrumentedTest {
    @Test
    public void parsesTextFromClipData() throws Exception {
        ClipData clipData = ClipData.newPlainText("some-label", "some text with http://example.com/url");
        assertEquals("http://example.com/url", new TextParser().parseClipData(clipData).toString());
    }

    @Test
    public void parsesTextFromClipDataNoUrl() throws Exception {
        ClipData clipData = ClipData.newPlainText("some-label", "some text");
        assertNull(new TextParser().parseClipData(clipData));
    }
}
