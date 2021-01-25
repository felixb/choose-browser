package de.ub0r.android.choosebrowser;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class IntentParserTest {

    private final IntentParser intentParser = new IntentParser();

    @Test
    public void returnsNullOnMissingUrl() throws Exception {
        assertNull(intentParser.parseText("Some text w/o url"));
    }

    @Test
    public void parsesUrlFromTextHttp() throws Exception {
        assertEquals("http://example.com/foo.html", new IntentParser().parseText("Long text, with words http://example.com/foo.html"));
    }

    @Test
    public void parsesUrlFromTextHttps() throws Exception {
        assertEquals("https://example.com", new IntentParser().parseText("Long text, with words https://example.com"));
    }

    @Test
    public void parsesUrlFromTextSlashAtEnd() throws Exception {
        assertEquals("https://example.com/", new IntentParser().parseText("Long text, with words https://example.com/"));
    }

    @Test
    public void parsesUrlFromTextWordsAfterUrl() throws Exception {
        assertEquals("https://example.com/", new IntentParser().parseText("Long text, with words https://example.com/ and more words"));
    }

    @Test
    public void extractGoogleRedirectUrl() throws Exception {
        assertEquals(Uri.parse("https://foo.bar/j/123?pwd=usr"),
                new IntentParser().resolveRedirect(Uri.parse("https://www.google.com/url?q=https://foo.bar/j/123?pwd%3Dusr&sa=D")));
    }
}