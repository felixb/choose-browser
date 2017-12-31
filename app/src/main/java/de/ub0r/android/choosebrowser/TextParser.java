package de.ub0r.android.choosebrowser;

import android.content.ClipData;
import android.net.Uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParser {
    private final static String PATTERN = "https?://[^\\s]*";
    private final Pattern mPattern = Pattern.compile(PATTERN);

    public Uri parseClipData(final ClipData clipData) {
        if (clipData != null && clipData.getItemCount() > 0) {
            final ClipData.Item item = clipData.getItemAt(0);

            final String url = parseUrl(item.getText());
            if (url != null) {
                return Uri.parse(url);
            }
        }
        return null;
    }

    public String parseUrl(final CharSequence text) {
        final Matcher matcher = mPattern.matcher(text);
        if (matcher.find()) {
            return text.subSequence(matcher.start(), matcher.end()).toString();
        } else {
            return null;
        }
    }
}
