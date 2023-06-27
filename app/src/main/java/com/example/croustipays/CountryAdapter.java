package com.example.croustipays;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.squareup.picasso.Picasso;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private List<Country> countries = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Country country);
    }

    public List<Country> getCountries() {return countries;}
    public void setCountries(List<Country> countries) {
        // Tri par ordre alphabétique
        Collections.sort(countries, new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                return country1.getName().compareToIgnoreCase(country2.getName());
            }
        });
        this.countries = countries;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.bind(country);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(country);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView populationTextView;

        private ImageView flagImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flagImageView = itemView.findViewById(R.id.flagImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            populationTextView = itemView.findViewById(R.id.populationTextView);
        }

        public void bind(Country country) {
            Picasso.get().load(country.getFlags().getPng()).into(flagImageView );

            nameTextView.setText(country.getName());
            populationTextView.setText("CroustiPrix: " + String.valueOf(country.getPopulation()) + " $");
        }
    }
}
