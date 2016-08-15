package com.example.almaz.gdgforecast.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.almaz.gdgforecast.R;
import com.example.almaz.gdgforecast.model.Forecast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class ForecastActivity extends AppCompatActivity {

    private final String TAG = "ForecastActivity TAG";
    public static final String EXTRA_CITY_NAME = "EXTRA_CITY_NAME";

    private Forecast mForecast;

    ImageView mWeatherImageView;
    TextView mTempView;
    TextView mCityView;
    TextView mWindView;
    TextView mPressureView;
    TextView mHumidityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mWeatherImageView = (ImageView) findViewById(R.id.weather_activity_iv);
        mTempView = (TextView) findViewById(R.id.weather_temp_tv);
        mCityView = (TextView) findViewById(R.id.weather_city_tv);
        mWindView = (TextView) findViewById(R.id.weather_wind_tv);
        mPressureView = (TextView) findViewById(R.id.weather_pressure_tv);
        mHumidityView = (TextView) findViewById(R.id.weather_humidity_tv);

        Intent intent = getIntent();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        mForecast = gson.fromJson(intent.getStringExtra(EXTRA_CITY_NAME), Forecast.class);
        updateViews();
    }

    private void updateViews(){
        mCityView.setText(mForecast.name);
        mTempView.setText((int)mForecast.main.temp - 273 + "°");
        mWindView.setText((int)mForecast.wind.speed + " m/s");
        mPressureView.setText((int)mForecast.main.pressure + " Pa");
        mHumidityView.setText(mForecast.main.humidity + " %");
        setWeatherPicture();
    }

    private void setWeatherPicture() {
        long weatherId = mForecast.weather[0].id;
        if (weatherId >= 500 && weatherId < 505) {
            mWeatherImageView.setImageResource(R.drawable.w10);
        } else if (weatherId >= 300 && weatherId < 322 || weatherId >= 520 &&weatherId < 532) {
            mWeatherImageView.setImageResource(R.drawable.w09);
        } else if (weatherId == 511 || weatherId >= 600 && weatherId < 623) {
            mWeatherImageView.setImageResource(R.drawable.w13);
        } else if (weatherId > 700 && weatherId < 782) {
            mWeatherImageView.setImageResource(R.drawable.w50);
        } else if (weatherId >= 200 && weatherId < 233) {
            mWeatherImageView.setImageResource(R.drawable.w11);
        } else if (weatherId == 800) {
            mWeatherImageView.setImageResource(R.drawable.w01);
        } else if (weatherId == 801) {
            mWeatherImageView.setImageResource(R.drawable.w02);
        } else if (weatherId == 802) {
            mWeatherImageView.setImageResource(R.drawable.w04);
        } else if (weatherId == 803 || weatherId == 804) {
            mWeatherImageView.setImageResource(R.drawable.w04);
        }
    }
}
