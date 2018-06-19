package com.example.eisat_000.weatherapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by eisat_000 on 5/4/2017.
 */

public class WeatherAdapter extends ArrayAdapter<Weather> {

    public WeatherAdapter(Activity context, ArrayList<Weather> forecasts)
    {
        super(context, 0, forecasts);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);


        Weather currentForecast = getItem(position);

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(currentForecast.getDate());

        TextView summaryView = (TextView) listItemView.findViewById(R.id.summary);
        summaryView.setText(currentForecast.getSummary());

        TextView averageView = (TextView) listItemView.findViewById(R.id.average);
        String averageText = "Average: " + currentForecast.getAverageTemp() + "\u2109";
        averageView.setText(averageText);

        TextView minimumView = (TextView) listItemView.findViewById(R.id.minimum);
        String minimumText = "Minimum: " + currentForecast.getMinTemp() + "\u2109";
        minimumView.setText(minimumText);

        TextView maximumView = (TextView) listItemView.findViewById(R.id.maximum);
        String maximumText = "Maximum: " + currentForecast.getMaxTemp() + "\u2109";
        maximumView.setText(maximumText);

        ImageView iconView = (ImageView) listItemView.findViewById(R.id.weather_icon);
        iconView.setImageResource(getWeatherIcon(currentForecast.getIcon()));

        return listItemView;
    }

    private int getWeatherIcon (String icon)
    {
        int weatherIconResourceID;

        switch(icon)
        {
            case "wi_color_blizzard.png":
                weatherIconResourceID = R.drawable.wi_color_blizzard;
                break;
            case "wi_color_clear_day.png":
                weatherIconResourceID = R.drawable.wi_color_clear_day;
                break;
            case "wi_color_clear_night.png":
                weatherIconResourceID = R.drawable.wi_color_clear_night;
                break;
            case "wi_color_cloudy.png":
                weatherIconResourceID = R.drawable.wi_color_cloudy;
                break;
            case "wi_color_drizzle.png":
                weatherIconResourceID = R.drawable.wi_color_drizzle;
                break;
            case "wi_color_hail.png":
                weatherIconResourceID = R.drawable.wi_color_hail;
                break;
            case "wi_color_partly_cloudy_day.png":
                weatherIconResourceID = R.drawable.wi_color_partly_cloudy_day;
                break;
            case "wi_color_partly_cloudy_night.png":
                weatherIconResourceID = R.drawable.wi_color_partly_cloudy_night;
                break;
            case "wi_color_rain.png":
                weatherIconResourceID = R.drawable.wi_color_rain;
                break;
            case "wi_color_snow.png":
                weatherIconResourceID = R.drawable.wi_color_snow;
                break;
            case "wi_color_sunny.png":
                weatherIconResourceID = R.drawable.wi_color_sunny;
                break;
            case "wi_color_thunder.png":
                weatherIconResourceID = R.drawable.wi_color_thunder;
                break;
            case "wi_color_windy.png":
                weatherIconResourceID = R.drawable.wi_color_windy;
                break;
            default:
                weatherIconResourceID = R.drawable.wi_color_winter_mix;
        }

        return weatherIconResourceID;
    }
}
