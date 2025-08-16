package com.example.mobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KameraAdapter extends RecyclerView.Adapter<KameraHolder> {
    private final KameraViewInterface kameraViewInterface;
    Context context;
    View.OnClickListener onClickListener;
    List<Parking> parkingi;

    public KameraAdapter(Context context, List<Parking> parkingi,
                         KameraViewInterface kameraViewInterface) {
        this.context = context;
        this.parkingi = parkingi;
        this.kameraViewInterface = kameraViewInterface;
    }

    @NonNull
    @Override
    public KameraHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KameraHolder(LayoutInflater.from(context).inflate(R.layout.kamera_na_liscie_widok,parent,false),kameraViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull KameraHolder holder, int position) {
        holder.nazwaKamery.setText(parkingi.get(position).getNazwaParkingu());
        holder.polozenieParkingu.setText(parkingi.get(position).getPolozenieNaMapie());

    }

    @Override
    public int getItemCount() {
        return parkingi.size();
    }

}
