package com.example.almaz.gdgforecast.rest;

import com.example.almaz.gdgforecast.model.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by almaz on 14.08.2016.
 */
public interface ForecastService {
    @GET("weather")
    Call<Forecast> getForecast(@Query("q") String name, @Query("APPID") String apiKey);
}
