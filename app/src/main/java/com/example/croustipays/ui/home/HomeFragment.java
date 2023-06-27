package com.example.croustipays.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.croustipays.Country;
import com.example.croustipays.CountryApiService;
import com.example.croustipays.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FragmentHomeBinding binding;
    private GoogleMap googleMap;
    private MapView mapView;
    private List<Country> countries;
    private CountryApiService apiService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restcountries.com/v3.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(CountryApiService.class);
        getCountriesFromApi();
    }

    private void getCountriesFromApi() {
        Call<List<Country>> call = apiService.getCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if (response.isSuccessful()) {
                    countries = response.body();
                    if (googleMap != null) {
                        addCountryMarkers();
                    }
                } else {
                    // Gérer les erreurs de l'API
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                // Gérer les erreurs de l'API
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (countries != null) {
            addCountryMarkers();
        }
    }

    private void addCountryMarkers() {
        if (googleMap == null || countries == null) {
            return;
        }

        for (Country country : countries) {
            List<Double> latLng = country.getLatlng();
            double latitude = latLng.get(0);
            double longitude = latLng.get(1);
            int population = country.getPopulation();

            LatLng position = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(position).title("CroustiPopulation : " + String.valueOf(population)));
        }
    }
}
