package com.example.eisat_000.weatherapp;

/**
 * Created by eisat_000 on 5/4/2017.
 */

public class Weather {

    private String averageTemp;
    private String date;
    private String minTemp;
    private String maxTemp;
    private String summary;
    private String icon;


    public Weather(String aveTemp, String _date, String _minTemp, String _maxTemp, String sum, String _icon)
    {
        averageTemp = aveTemp;
        date = _date;
        minTemp = _minTemp;
        maxTemp = _maxTemp;
        summary = sum;
        icon = _icon;
    }


    public String getAverageTemp()
    {
        return averageTemp;
    }

    public String getDate()
    {
        return date;
    }

    public String getMinTemp()
    {
        return minTemp;
    }

    public String getMaxTemp()
    {
        return maxTemp;
    }

    public String getSummary()
    {
        return summary;
    }

    public String getIcon()
    {
        return icon;
    }

}
