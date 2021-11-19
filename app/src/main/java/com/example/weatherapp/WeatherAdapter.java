package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Model> weatherModelList;

    public WeatherAdapter(Context context, ArrayList<Model> list) {
        this.context = context;
        this.weatherModelList = list;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_weather, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        Model mod = weatherModelList.get(position);
        holder.temp.setText(mod.getTemperature() + "°c");
        //holder.time.setText(mod.getTime());
        holder.wind.setText(mod.getWindSpeed() + "km/h");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-DD hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("HH:mm");

        Date t = null;
        try {
            t = input.parse(mod.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.time.setText(output.format(t).concat("\nIST"));

        Picasso.get().load("https:".concat(mod.getIcon())).into(holder.condition);
    }

    @Override
    public int getItemCount() {

        return weatherModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView condition;
        TextView wind, temp, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wind = itemView.findViewById(R.id.idTvWind);
            temp = itemView.findViewById(R.id.idTvTemp);
            time = itemView.findViewById(R.id.idTvTime);
            condition = itemView.findViewById(R.id.idIvCondition);


        }
    }
}
