package com.example.croustipays;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CountryApiService {
    @GET("all")
    Call<List<Country>> getCountries();
}
