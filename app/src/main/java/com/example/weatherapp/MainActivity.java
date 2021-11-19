  package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Permission;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRl;
    private ProgressBar loadingPb;
    private TextView cityNameTV, temperatureTv, conditionTV;
    private RecyclerView weatherRv;
    private TextInputEditText cityEdt;
    private ImageView backIv, iconIv, searchIv;
    private ArrayList<Model> weatherModelList;
    private WeatherAdapter weatherAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String nameOfCity;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_main);
        homeRl = findViewById(R.id.idRlHome);
        loadingPb = findViewById(R.id.idLoading);
        cityNameTV = findViewById(R.id.idTVCityName);
        temperatureTv = findViewById(R.id.idTvTemp);
        conditionTV = findViewById(R.id.idTvCondition);
        weatherRv = findViewById(R.id.idRvWeather);
        cityEdt = findViewById(R.id.idEdtCity);
        //  backIv=findViewById(R.id.idIVBack);
        iconIv = findViewById(R.id.idIvIcon);
        searchIv = findViewById(R.id.idIvSearch);


        weatherModelList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this, weatherModelList);
        weatherRv.setAdapter(weatherAdapter);

        //getWeatherInfo("Delhi");

//       if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
//         ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
//       }
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
//           String city = getLocation();
////            cityNameTV.setTextColor(Color.parseColor("#FFFFFFFF"));
////
////            Toast.makeText(MainActivity.this, city, Toast.LENGTH_SHORT).show();
////            Log.i("CITY", "m");
//
//            getWeatherInfo(city);
//            cityNameTV.setText("Your Location");
//        }
//        else{
//            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
//        }

//               if(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!=null) {
//    nameOfCity = getCityName(location.getLongitude(), location.getLatitude());
//      getWeatherInfo(nameOfCity);
//
//       }
//    else
//           getWeatherInfo("Delhi");

        cityEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getWeatherInfo(cityEdt.getText().toString());
                    return true;
                }
                return false;
            }
        });

        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityEdt.getText().toString();
                if (city.isEmpty())
                    Toast.makeText(MainActivity.this, "First Enter the city Name", Toast.LENGTH_SHORT).show();
                else {
                    // String s = city.toUpperCase();

                    cityNameTV.setText(city);

                    getWeatherInfo(city);

                }
            }
        });

    }

//    @SuppressLint("MissingPermission")
//    private String getLocation() {
//        final String[] city = new String[1];
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//    Location location = task.getResult();
//    if(location!=null){
//        Geocoder geocoder = new Geocoder(MainActivity.this,Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//          double latitude=  addresses.get(0).getLatitude();
//          double longitude = addresses.get(0).getLongitude();
//
//         //   Log.i( "onComplete: ",latitude+" "+longitude);
//
//          city[0] =getCityName(longitude,latitude);
//            Toast.makeText(MainActivity.this, Double.toString(longitude), Toast.LENGTH_LONG).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//            }
//        });
//        return city[0];
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this, "Please Provide Permissions", Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }
//    private String getCityName(double longitude, double latitude){
//        String cityName ="Not Found";
//        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
//        List<Address> addresses = null;
//        try {
//            addresses = geocoder.getFromLocation(latitude, longitude, 10);
//            for (Address adr:addresses){
//                if(adr!=null){
//                    String city =adr.getLocality();
//                    if(city!=null&& !city.equals("")){
//                        cityName=city;
//                    }
//                    else{
//                        //Log.d("TAG","City Not Found");
//                        Toast.makeText(this, "City Not Found", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return cityName;
////        String cityName = addresses.get(0).getAddressLine(0);
////        String stateName = addresses.get(0).getAddressLine(1);
////        String countryName = addresses.get(0).getAddressLine(2);
//    }

    private void getWeatherInfo(String cityName) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=92d4526f3d514b38bb271401211509&q=" + cityName + "&days=1&aqi=no&alerts=no";
        cityNameTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPb.setVisibility(View.GONE);
                homeRl.setVisibility(View.VISIBLE);
                weatherModelList.clear();

                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    temperatureTv.setText(temperature + "°c");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String icon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("https:".concat(icon)).into(iconIv);
                    conditionTV.setText(condition);
//                    if(isDay==1)
//                        Picasso.get().load("https://images.unsplash.com/photo-1514241516423-6c0a5e031aa2?ixid=MnwxMjA3fDB8MHxzZWFyY2h8NjV8fG1vcm5pbmd8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60").into(backIv);
//                    else
//                        Picasso.get().load("https://images.unsplash.com/photo-1532767153582-b1a0e5145009?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80").into(backIv);

                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecastDay = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecastDay.getJSONArray("hour");

                    for (int i = 0; i < hourArray.length(); i++) {
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temp = hourObj.getString("temp_c");
                        String image = hourObj.getJSONObject("condition").getString("icon");
                        String wind = hourObj.getString("wind_kph");
                        weatherModelList.add(new Model(time, temp, image, wind));
                    }
                    weatherAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "City Not Found", Toast.LENGTH_LONG).show();

            }
        });
        requestQueue.add(jsonObjectRequest);
    }


}