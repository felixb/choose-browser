package de.ub0r.android.choosebrowser;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.ub0r.android.logg0r.Log;

class IntentParser {
    private final static String TAG = "IntentParser";
    private final static String PATTERN = "https?://[^\\s]*";
    private final Pattern mPattern = Pattern.compile(PATTERN);

    Uri parseIntent(final Intent intent) {
        final Uri data = intent.getData();
        Log.d(TAG, "parsing intent with action %s, data %s", intent.getAction(), data);

        if (data != null && data.getScheme().startsWith("http")) {
            return data;
        }

        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            final String url = parseText(intent.getStringExtra(Intent.EXTRA_TEXT));
            if (url != null) {
                return Uri.parse(url);
            }
        }

        if (intent.getClipData() != null) {
            return parseClipData(intent.getClipData());
        }

        return null;
    }

    private Uri parseClipData(@NonNull final ClipData clipData) {
        if (clipData.getItemCount() == 0) {
            return null;
        }
        final ClipData.Item item = clipData.getItemAt(0);

        if (item.getText() != null) {
            return uriOrNull(parseText(item.getText()));
        }

        if (item.getUri() != null) {
            if (item.getUri().getScheme().startsWith("http")) {
                return item.getUri();
            }
        }

        return null;
    }

    String parseText(final CharSequence text) {
        final Matcher matcher = mPattern.matcher(text);
        if (matcher.find()) {
            return text.subSequence(matcher.start(), matcher.end()).toString();
        } else {
            return null;
        }
    }

    private Uri uriOrNull(final String url) {
        if (url != null) {
            return Uri.parse(url);
        }
        return null;
    }
}
