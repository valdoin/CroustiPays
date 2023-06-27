package com.example.croustipays.ui.favorites;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croustipays.Country;
import com.example.croustipays.CountryAdapter;
import com.example.croustipays.databinding.FragmentDashboardBinding;
import com.example.croustipays.databinding.FragmentNotificationsBinding;
import com.example.croustipays.ui.dashboard.DashboardFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.example.croustipays.databinding.FragmentFavoritesBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesFragment extends Fragment implements CountryAdapter.OnItemClickListener {

    private @NonNull FragmentFavoritesBinding binding;
    private CountryAdapter countryAdapter;
    private List<Country> favoriteCountries = new ArrayList<>(); // Liste pour stocker les pays favoris

    private SharedPreferences sharedPreferences;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // On récupère la liste des pays favoris sous forme de chaîne JSON
        String favoritesJson = sharedPreferences.getString("favoriteCountries", "");
        // On convertit la chaîne JSON en liste de pays
        if (!favoritesJson.isEmpty()) {
            favoriteCountries = new Gson().fromJson(favoritesJson, new TypeToken<List<Country>>() {}.getType());
        }
        Log.e("MINCE", "onCreateView: "+favoriteCountries.toString() );
        return root;
    }
/*
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        countryAdapter = new CountryAdapter();
        binding.recyclerView.setAdapter(countryAdapter);
        countryAdapter.setOnItemClickListener(this);

        // On récupère la liste des pays favoris depuis les sharedpreferences
        Set<String> favoriteCountryNames = sharedPreferences.getStringSet("favoriteCountries", null);
        if (favoriteCountryNames != null) {
            // On récupère les pays correspondants à partir du dashboardfragment
            DashboardFragment dashboardFragment = (DashboardFragment) getParentFragmentManager().findFragmentByTag("dashboard_fragment");
            if (dashboardFragment != null) {
                List<Country> favoriteCountries = dashboardFragment.getFavoriteCountries();
                countryAdapter.setCountries(favoriteCountries);
            }
        }

        countryAdapter.setCountries(favoriteCountries);
    }
    */
@Override
public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);


    binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    countryAdapter = new CountryAdapter();
    binding.recyclerView.setAdapter(countryAdapter);
    countryAdapter.setOnItemClickListener(this);

    binding = FragmentFavoritesBinding.bind(view);



    countryAdapter.setCountries(favoriteCountries);

    };



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Country country) {
        // Gérer le clic sur un pays favori si nécessaire
    }
}
