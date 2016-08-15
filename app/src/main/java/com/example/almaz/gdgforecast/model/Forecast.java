package com.example.almaz.gdgforecast.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by almaz on 14.08.2016.
 */
public class Forecast {

    @SerializedName("coord")
    public Coord coord;
    @SerializedName("weather")
    public Weather[] weather;
    @SerializedName("base")
    public String base;
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("clouds")
    public Clouds clouds;
    @SerializedName("dt")
    public String dt;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("id")
    public long id;
    @SerializedName("name")
    public String name;
    @SerializedName("cod")
    public long cod;

    public static class Coord{
        @SerializedName("lon")
        public double lon;
        @SerializedName("lat")
        public double lat;
    }

    public static class Sys{
        @SerializedName("message")
        public String message;
        @SerializedName("country")
        public String country;
        @SerializedName("sunrise")
        public String sunrise;
        @SerializedName("sunset")
        public String sunset;
    }

    public static class Weather{
        @SerializedName("id")
        public long id;
        @SerializedName("main")
        public String main;
        @SerializedName("description")
        public String description;
        @SerializedName("icon")
        public String icon;
    }

    public static class Main{
        @SerializedName("temp")
        public double temp;
        @SerializedName("pressure")
        public double pressure;
        @SerializedName("humidity")
        public int humidity;
        @SerializedName("temp_min")
        public double temp_min;
        @SerializedName("temp_max")
        public double temp_max;
        @SerializedName("sea_level")
        public double sea_level;
        @SerializedName("grnd_level")
        public double grnd_level;
    }

    public static class Wind{
        @SerializedName("speed")
        public double speed;
        @SerializedName("deg")
        public double deg;
    }

    public static class Clouds{
        @SerializedName("all")
        public int all;
    }
}
