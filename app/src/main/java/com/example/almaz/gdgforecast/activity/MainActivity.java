package com.example.almaz.gdgforecast.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.almaz.gdgforecast.BuildConfig;
import com.example.almaz.gdgforecast.R;
import com.example.almaz.gdgforecast.adapter.CitiesRecyclerViewAdapter;
import com.example.almaz.gdgforecast.model.Forecast;
import com.example.almaz.gdgforecast.provider.CitiesContentProvider;
import com.example.almaz.gdgforecast.rest.ForecastApiClient;
import com.example.almaz.gdgforecast.rest.ForecastService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "MainActivity TAG";

    private ForecastService mForecastService;
    private List<String> mCities;
    private RecyclerView mCitiesRcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCitiesRcv = (RecyclerView) findViewById(R.id.rcv_cities);
        updateRcv();

        for(int i=0;i<mCities.size();i++){
            loadForecast(mCities.get(i));
        }
    }

    private void updateRcv(){
        takeCities();
        setRecyclerAdapter(mCitiesRcv, mCities);
    }


    public void onClickAddCity(View v){
        showCityAddingDialog();
    }

    private void takeCities(){
        mCities = new ArrayList<>();
        Cursor cursor = getContentResolver()
                .query(CitiesContentProvider.CITIES_CONTENT_URI, null, null, null, "name ASC");
        cursor.moveToFirst();
        for(int i = 0; i<cursor.getCount();i++){
            mCities.add(cursor.getString(CitiesContentProvider.NAME_COLUMN));
            cursor.moveToNext();
        }
    }

    private void showCityAddingDialog(){
        final EditText et = new EditText(getApplicationContext());
        et.setTextColor(Color.parseColor("#555555"));
        et.setTextSize(24);
        et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Adding new city")
                .setCancelable(true)
                .setView(et)
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                addCity(et.getText().toString());
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setRecyclerAdapter(RecyclerView recyclerView, List list) {
        CitiesRecyclerViewAdapter adapter =
                new CitiesRecyclerViewAdapter(getApplicationContext(), list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
    }


    private void addCity(String cityName){
        ContentValues cv = new ContentValues();
        cv.put(CitiesContentProvider.CITY_NAME, cityName);
        getContentResolver()
                .insert(CitiesContentProvider.CITIES_CONTENT_URI, cv);
        updateRcv();
        loadForecast(cityName);
    }

    private void loadForecast(final String cityName){
        mForecastService = ForecastApiClient.getClient().create(ForecastService.class);
        Call<Forecast> call = mForecastService
                .getForecast(cityName, BuildConfig.OPEN_WEATHER_MAP_API_KEY);
        call.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast>call, Response<Forecast> response) {
                Forecast forecast = response.body();
                Log.d(TAG, "Forecast uploaded for " + forecast.name);
                updateRcv();

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                String jsonForecast = gson.toJson(forecast);
                ContentValues cv = new ContentValues();
                cv.put(CitiesContentProvider.CITY_LAST_FORECAST, jsonForecast);
                String where = CitiesContentProvider.CITY_NAME + " LIKE ?";
                String[] selectionArgs = {cityName};
                getContentResolver().update(CitiesContentProvider.CITIES_CONTENT_URI, cv, where,
                        selectionArgs);
            }

            @Override
            public void onFailure(Call<Forecast>call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}
