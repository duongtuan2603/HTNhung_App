package com.example.htnhung_app.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.htnhung_app.databinding.ItemCarParkBinding;

import java.util.ArrayList;
import java.util.List;

public class CarParkAdapter extends RecyclerView.Adapter<CarParkAdapter.CarParkViewHolder> {
    private List<CarPark> carParks = new ArrayList<>();
    private ICarPark iCarPark;
    Context context;

    public CarParkAdapter(ICarPark iCarPark) {
        this.iCarPark = iCarPark;
    }

    public void setCarParks(List<CarPark> carParks) {
        this.carParks = carParks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarParkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCarParkBinding binding = ItemCarParkBinding.inflate(layoutInflater, parent, false);
        return new CarParkViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CarParkViewHolder holder, int position) {
        CarPark carPark = carParks.get(position);
        holder.binding.setCarPark(carPark);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "RATE: "+carPark.getTotalClassified(), Toast.LENGTH_SHORT).show();
                iCarPark.onClickItem(carPark.getLat(), carPark.getLon());
            }
        });

    }

    @Override
    public int getItemCount() {
        return carParks.size();
    }

    public class CarParkViewHolder extends RecyclerView.ViewHolder {
        ItemCarParkBinding binding;

        public CarParkViewHolder(@NonNull ItemCarParkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ICarPark {
        void onClickItem(double lat, double lon);
    }
}
