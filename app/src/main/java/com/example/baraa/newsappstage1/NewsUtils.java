package com.example.baraa.newsappstage1;


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
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from Guardian Api.
 */
public final class NewsUtils {

    public static final int URL_READ_TIME_OUT = 10000;

    public static final int URL_CONNECT_TIME_OUT = 15000;

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = NewsUtils.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link NewsUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name NewsUtils.
     */
    private NewsUtils() {

    }

    /**
     * Query the Guardian Api data and return a list of {@link News} objects.
     */

    public static List<News> fetchNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }


        // Return the list of {@link News}
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            // In case that request failed, print the error message into log
            Log.e(LOG_TAG, "Problem building the URL ", e);
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
            urlConnection.setReadTimeout(URL_READ_TIME_OUT /* milliseconds */);
            urlConnection.setConnectTimeout(URL_CONNECT_TIME_OUT /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            // Send a request to connect
            urlConnection.connect();

            // If the request was successful (response code 200 which is HTTP_OK),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                // If the response failed, print it to the Log
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        // Create new StringBuilder
        StringBuilder output = new StringBuilder();

        // If the InputStream exists, create an InputStreamReader from it and a BufferedReader from the InputStreamReader
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        // Convert the output into String and return it
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> news = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Extract the JSONObject associated with the key called "response",
            JSONObject responseJsonNews = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results"
            JSONArray newsArray = responseJsonNews.getJSONArray("results");

            // For each news in the JsonNewsArray create an {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news article at position i within the list of news
                JSONObject currentNews = newsArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String newsTitle = currentNews.getString("webTitle");

                //  Extract the value for the key called "sectionName"
                String newsSection = currentNews.getString("sectionName");


                // Check if newsDate exist and than extract the date for the key called "webPublicationDate"
                String newsDate = "N/A";

                if (currentNews.has("webPublicationDate")) {
                    newsDate = currentNews.getString("webPublicationDate");
                }


                // Extract the value for the key called "webUrl"
                String newsUrl = currentNews.getString("webUrl");

                // Extract the value for the key called "tags",
                JSONArray currentNewsAuthorArray = currentNews.getJSONArray("tags");

                String newsAuthor = "N/A";

                //Check if "tags" array contains data
                int tagsLenght = currentNewsAuthorArray.length();


                if (tagsLenght == 1) {
                    StringBuilder artAuthorBuilder = new StringBuilder();
                    for (int j = 0; j < currentNewsAuthorArray.length(); j++) {
                        JSONObject objectPositionOne = currentNewsAuthorArray.getJSONObject(j);
                        artAuthorBuilder.append(objectPositionOne.getString("webTitle")).append(". ");
                    }
                    newsAuthor = "by: " + artAuthorBuilder.toString();

                }

                // Create a new {@link News} object with the title, category, author, date,
                // and url from the JSON response.
                News newNews = new News(newsTitle, newsSection, newsAuthor, newsDate, newsUrl);

                // Add the new {@link News} to the list of News.
                news.add(newNews);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("NewsUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return news;

    }
}

