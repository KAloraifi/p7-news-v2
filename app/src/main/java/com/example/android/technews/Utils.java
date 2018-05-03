package com.example.android.technews;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Aloraifi on 02/05/2018.
 */

public final class Utils {

    public static final String LOG_TAG = MainActivity.class.getName();

    private Utils(Context context) {
    }

    // Performs HTTP request for a given url and assign the response to Article objects.
    public static ArrayList<Article> fetchArticleData(String responseURL) {
        URL url = createUrl(responseURL);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        ArrayList<Article> earthquakes = extractArticles(jsonResponse);

        return earthquakes;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // Handles the input stream and converts it to readable string.
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return builder.toString();
    }

    private static ArrayList<Article> extractArticles(String articleJSON) {
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        ArrayList<Article> articles = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(articleJSON);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            Log.e("Utils", "The length of the result is: " + results.length());

            Article currentArticle = null;
            for (int i = 0; i < results.length(); i++) {
                JSONObject article = results.getJSONObject(i);

                String title = article.getString("webTitle");
                String section = article.getString("sectionName");
                String url = article.getString("webUrl");
                String date = article.getString("webPublicationDate");
                JSONArray tags = article.getJSONArray("tags");

                JSONObject profile;
                String name;
                if (tags.length() == 0) {
                    currentArticle = new Article(title, section, R.string.no_author, date, url);
                } else {
                    profile = tags.getJSONObject(0);
                    name = profile.getString("webTitle");
                    currentArticle = new Article(title, section, name, date, url);
                }

                articles.add(currentArticle);
            }
        } catch (JSONException e) {
            Log.e("Utils", "Problem parsing the article JSON result", e);
        }

        return articles;
    }
}
