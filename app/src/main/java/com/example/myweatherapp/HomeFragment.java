package com.example.myweatherapp;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myweatherapp.databinding.FragmentHomeBinding; // Change to the correct binding class
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding; // Fragment-specific view binding

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false); // Using view binding for fragment
        fetchWeatherData("Rajasthan"); // Example city name
        return binding.getRoot(); // Return the bound root view
    }

    private void fetchWeatherData(String cityName) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<Hydweatherdata> call = apiInterface.getWeatherData(cityName, "963ebc7b1e62163f0113d423f0e0df45", "metric");

        call.enqueue(new Callback<Hydweatherdata>() {
            @Override
            public void onResponse(Call<Hydweatherdata> call, Response<Hydweatherdata> response) {
                if (response.isSuccessful()) {
                    Hydweatherdata weatherApp = response.body();

                    if (weatherApp != null) {
                        double temp = weatherApp.getMain().getTemp();
                        binding.temp.setText(temp + " °C"); // Example TextView ID

                        double highTemp = weatherApp.getMain().getTempMax();
                        binding.textView5.setText(highTemp + " °C");

                        double lowTemp = weatherApp.getMain().getTempMin();
                        binding.textView6.setText(lowTemp + " °C");

                        String condition = weatherApp.getWeather().get(0).getDescription();
                        binding.textView4.setText(condition);

                        binding.textView7.setText(dayName(System.currentTimeMillis()));
                        binding.textView8.setText(date());
                        binding.textView.setText(cityName);

                        changeBgAccToCondt(condition);
                    }
                }
            }



            @Override
            public void onFailure(Call<Hydweatherdata> call, Throwable t) {
                Log.e("HomeFragment", "Failed to fetch weather data", t);
            }
        });
    }

    private void changeBgAccToCondt(String condition) {
    }

    public static String dayName(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static String date() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
}