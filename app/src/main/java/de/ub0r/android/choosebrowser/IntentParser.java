package de.ub0r.android.choosebrowser;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.ub0r.android.logg0r.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;

class IntentParser {
    private final static String TAG = "IntentParser";
    private final static String PATTERN = "https?://[^\\s]*";
    private static final OkHttpClient httpClient =
            new OkHttpClient.Builder().callTimeout(3, SECONDS).followRedirects(false).build();
    private static final ExecutorService executor = newFixedThreadPool(3);

    private final Pattern mPattern = Pattern.compile(PATTERN);

    Uri parseIntent(final Intent intent) {
        final Uri data = intent.getData();
        Log.d(TAG, "parsing intent with action %s, data %s", intent.getAction(), data);

        Uri parsed = null;

        if (data != null && data.getScheme() != null && data.getScheme().startsWith("http")) {
            parsed = data;
        } else if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            final String url = parseText(intent.getStringExtra(Intent.EXTRA_TEXT));
            if (url != null) {
                parsed = Uri.parse(url);
            }
        } else if (intent.getClipData() != null) {
            parsed = parseClipData(intent.getClipData());
        }

        return removeRedirect(parsed);
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
            if (item.getUri().getScheme() != null && item.getUri().getScheme().startsWith("http")) {
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

    Uri removeRedirect(Uri uri) {
        if (uri == null || uri.getHost() == null || uri.getEncodedPath() == null) {
            return uri;
        }

        if (uri.getHost().endsWith("google.com")
                && uri.getEncodedPath().equals("/url")
                && uri.getQueryParameter("q") != null) {
            uri = parseUri(uri.getQueryParameter("q"));
        }

        if (uri.getHost().endsWith("bit.ly")
                && uri.getEncodedPath().length() > 0
                && !uri.getEncodedPath().substring(1).contains("/")) {
            try {
                final Request request =
                        new Request.Builder().url(uri.toString().replace("http:", "https:")).head().build();
                String location = doHttp(request).header("location");
                if (location != null) {
                    uri = parseUri(location);
                }
            } catch (Exception e) {
                Log.i(TAG, "Could not fetch " + uri, e);
            }
        }
        return uri;
    }

    Uri parseUri(final String uri) {
        Uri redirect = Uri.parse(uri);
        Log.d(TAG, "Extracting %s from %s with %s", redirect, uri);
        return redirect;
    }

    Response doHttp(final Request request) throws ExecutionException, InterruptedException {
        return executor.submit(new Callable<Response>() {
            public Response call() throws IOException {
                return httpClient.newCall(request).execute();
            }
        }).get();
    }
}
