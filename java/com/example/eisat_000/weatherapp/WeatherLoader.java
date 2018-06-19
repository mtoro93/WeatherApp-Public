package com.example.eisat_000.weatherapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by eisat_000 on 5/4/2017.
 */

public class WeatherLoader extends AsyncTaskLoader<List<Weather>> {
    private String mUrl;

    public WeatherLoader(Context context, String url)
    {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading()
    {forceLoad();}

    @Override
    public List<Weather> loadInBackground()
    {
        if(mUrl == null)
            return null;

        List<Weather> forecasts = QueryUtils.extractWeather(mUrl);
        return forecasts;
    }
}
