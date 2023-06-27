package com.example.croustipays.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croustipays.Country;
import com.example.croustipays.CountryAdapter;
import com.example.croustipays.CountryApiService;
import com.example.croustipays.databinding.FragmentDashboardBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DashboardFragment extends Fragment implements CountryAdapter.OnItemClickListener {

    public Gson gson = new Gson();
    private FragmentDashboardBinding binding;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://restcountries.com/v3.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private CountryApiService apiService = retrofit.create(CountryApiService.class);
    private CountryAdapter countryAdapter;
    private EditText editTextSearch;
    private Button buttonSearch;

    private List<Country> allCountries = new ArrayList<>(); // Variable pour stocker la liste complète des pays
    private List<Country> favoriteCountries  = new ArrayList<>(); // Variable pour stocker les noms des pays favoris
    public String favoritesJson;
    private SharedPreferences sharedPreferences;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);



        String favoritesJson = sharedPreferences.getString("favoriteCountries", "");
        // On convertit la chaîne JSON en liste de pays
        if (!favoritesJson.isEmpty()) {
            favoriteCountries = new Gson().fromJson(favoritesJson, new TypeToken<List<Country>>() {}.getType());
        }

        Log.e("MINCE", "onCreateView: "+favoriteCountries.toString() );
        return root;
    }

    @Override
    public void onItemClick(Country country) {
        List<Double> latLng = country.getLatlng();
        String latitude = String.valueOf(latLng.get(0));
        String longitude = String.valueOf(latLng.get(1));

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(country.getName())
                .setMessage("CroustiLatitude : " + latitude + "°" + "\n" + "CroustiLongitude : " + longitude + "°")
                .setPositiveButton(getFavoriteButtonLabel(country), (dialog, which) -> toggleFavorite(country))
                .show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        countryAdapter = new CountryAdapter();
        binding.recyclerView.setAdapter(countryAdapter);
        countryAdapter.setOnItemClickListener(this);

        binding = FragmentDashboardBinding.bind(view);
        editTextSearch = binding.editTextSearch;

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchCountries();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Appel à l'API pour récupérer les pays
        Call<List<Country>> call = apiService.getCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if (response.isSuccessful()) {
                    List<Country> countries = response.body();
                    allCountries = countries; // Stocker la liste complète des pays
                    countryAdapter.setCountries(countries);
                } else {
                    // Gérer les erreurs de l'API
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e("non", "marche pas ");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void searchCountries() {
        String searchText = editTextSearch.getText().toString().trim();

        if (searchText.isEmpty()) {
            // Champ de recherche est vide, on utilise la liste complète des pays
            countryAdapter.setCountries(allCountries);
        } else {
            // Champ de recherche non-vide, on filtre les pays en fonction du texte saisi
            List<Country> filteredCountries = new ArrayList<>();
            for (Country country : allCountries) {
                if (country.getName().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredCountries.add(country);
                }
            }
            countryAdapter.setCountries(filteredCountries);
        }
    }


    private String getFavoriteButtonLabel(Country country) {
        return isFavorite(country) ? "Retirer des favoris" : "Ajouter aux favoris";
    }

    private boolean isFavorite(Country country) {
        if(!favoriteCountries.isEmpty()){
            return favoriteCountries.contains(country);
        }else{
            return false;
        }
    }

    private void toggleFavorite(Country country) {
        if (isFavorite(country)) {
            removeFromFavorites(country);
        } else {
            addToFavorites(country);
        }
    }

    private void addToFavorites(Country country) {
        favoriteCountries.add(favoriteCountries.size(), country);
        saveFavoriteCountries();
        Log.d("caca", "addToFavorites() called with: "+favoriteCountries.toString());
    }

    private void removeFromFavorites(Country country) {
        favoriteCountries.remove(country);
        saveFavoriteCountries();
    }

    private void saveFavoriteCountries() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("favoriteCountries", gson.toJson(favoriteCountries));
        editor.apply();
    }
}
