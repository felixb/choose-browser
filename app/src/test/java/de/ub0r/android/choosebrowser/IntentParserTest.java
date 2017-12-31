package de.ub0r.android.choosebrowser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
}