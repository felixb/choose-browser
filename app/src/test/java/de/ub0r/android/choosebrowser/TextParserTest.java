package de.ub0r.android.choosebrowser;

import android.content.ClipData;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TextParserTest {

    @Test
    public void returnsNullOnMissingUrl() throws Exception {
        assertNull(new TextParser().parseUrl("Some text w/o url"));
    }

    @Test
    public void parsesUrlFromTextHttp() throws Exception {
        assertEquals("http://example.com/foo.html", new TextParser().parseUrl("Long text, with words http://example.com/foo.html"));
    }

    @Test
    public void parsesUrlFromTextHttps() throws Exception {
        assertEquals("https://example.com", new TextParser().parseUrl("Long text, with words https://example.com"));
    }

    @Test
    public void parsesUrlFromTextSlashAtEnd() throws Exception {
        assertEquals("https://example.com/", new TextParser().parseUrl("Long text, with words https://example.com/"));
    }

    @Test
    public void parsesUrlFromTextWordsAfterUrl() throws Exception {
        assertEquals("https://example.com/", new TextParser().parseUrl("Long text, with words https://example.com/ and more words"));
    }
}