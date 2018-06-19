package com.example.eisat_000.weatherapp;

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

import static com.example.eisat_000.weatherapp.MainActivity.LOG_TAG;

/**
 * Created by eisat_000 on 5/4/2017.
 */

public class QueryUtils {

    // private constructor as this class only holds static methods
    private QueryUtils()
    {

    }

    public static List<Weather> extractWeather(String requestURL)
    {
        //create a URL Object
        URL url = createURL(requestURL);

        String jsonResponse = null;
        try
        {
            jsonResponse = makeHTTPRequest(url);
        }
        catch(IOException exception)
        {
            Log.e(LOG_TAG, "Error closing input stream", exception);
        }

        Log.e(LOG_TAG, requestURL);
        List<Weather> forecasts = extractFeatureFromJson(jsonResponse);
        return forecasts;
    }

    private static URL createURL(String stringUrl)
    {
        URL url = null;

        try
        {
            url = new URL(stringUrl);;
        }
        catch (MalformedURLException exception)
        {
            Log.e(LOG_TAG, "Error with creating URL", exception);
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) throws  IOException
    {
        String jsonResponse = "";

        // if the URL is null, return early
        if (url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // if the request was successful (response code 200),
            // then read the input stream and parse the response
            if (urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else
            {
                Log.e(LOG_TAG, "Error Response Code: " + urlConnection.getResponseCode());
            }
        }
        catch(IOException exception)
        {
            Log.e(LOG_TAG, "Problem retrieving the forecast JSON results.", exception);
        }
        finally{
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if (inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null)
            {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Weather> extractFeatureFromJson(String jsonResponse)
    {
        if (TextUtils.isEmpty(jsonResponse))
            return null;

        List<Weather> forecasts = new ArrayList<>();

        try
        {
            JSONObject response = new JSONObject(jsonResponse);

            Log.e(LOG_TAG, response.getString("error"));
            Log.e(LOG_TAG, response.getString("error_message"));
            JSONArray forecast = response.getJSONArray("forecast");
            for (int i = 0; i < forecast.length(); i++)
            {
                JSONObject dayForecast = forecast.getJSONObject(i);
                Weather temp = new Weather(
                        dayForecast.getString("avg_f"),
                        dayForecast.getString("date"),
                        dayForecast.getString("min_f"),
                        dayForecast.getString("max_f"),
                        dayForecast.getString("summary"),
                        dayForecast.getString("icon")
                        );

                forecasts.add(temp);
            }
        }
        catch(JSONException exception)
        {
            Log.e("QueryUtils", "Problem parsing the forecast JSON results", exception);
        }

        return forecasts;
    }
}
