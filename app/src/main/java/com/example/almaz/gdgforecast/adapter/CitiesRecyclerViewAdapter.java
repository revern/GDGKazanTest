package com.example.almaz.gdgforecast.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.almaz.gdgforecast.activity.ForecastActivity;
import com.example.almaz.gdgforecast.R;
import com.example.almaz.gdgforecast.model.Forecast;
import com.example.almaz.gdgforecast.provider.CitiesContentProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by almaz on 14.08.2016.
 */
public class CitiesRecyclerViewAdapter extends RecyclerView.Adapter<CitiesRecyclerViewAdapter.ViewHolder> {
    private List<String> mRecords;
    private Context mContext;

    public CitiesRecyclerViewAdapter(Context context, List<String> records){
        mRecords = records;
        mContext = context;
    }

    @Override
    public CitiesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_cities_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CitiesRecyclerViewAdapter.ViewHolder holder, final int position) {
        final TextView cityNameView = holder.mCityNameView;
        final TextView tempView = holder.mTempView;
        cityNameView.setText(mRecords.get(position));
        final Forecast forecast = getLastForecast(mRecords.get(position));
        if(forecast != null){
            tempView.setText((int)forecast.main.temp - 273 + "°");
        }
        holder.mCityNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ForecastActivity.class);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                intent.putExtra(ForecastActivity.EXTRA_CITY_NAME, gson.toJson(forecast));
                mContext.startActivity(intent);
            }
        });
        holder.mCityNameView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    @Nullable
    private Forecast getLastForecast(String cityName){
        Cursor cursor = mContext.getContentResolver()
                .query(CitiesContentProvider.CITIES_CONTENT_URI, null, null, null, null);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        cursor.moveToFirst();
        do{
            if(cursor.getString(CitiesContentProvider.NAME_COLUMN).equals(cityName)){
                try {
                    return gson.fromJson(
                            cursor.getString(CitiesContentProvider.LAST_FORECAST_COLUMN),
                            Forecast.class);
                }catch (IllegalStateException e){
                    return null;
                }
            }
        } while (cursor.moveToNext());

        return null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mCityNameView;
        private TextView mTempView;
        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mCityNameView = (TextView) itemView.findViewById(R.id.rcv_city_item);
            mTempView = (TextView) itemView.findViewById(R.id.rcv_temp_item);
        }
    }
}

