package com.alokbharti.parkme.Adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alokbharti.parkme.Interfaces.RecyclerViewClickListener;
import com.alokbharti.parkme.Pojo.ParkingInfo;
import com.alokbharti.parkme.R;

import java.util.List;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ViewHolder> {

    private List<ParkingInfo> parkingInfoList;
    private RecyclerViewClickListener itemListener;

    public ParkingAdapter(List<ParkingInfo> parkingInfoList, RecyclerViewClickListener itemListener) {
        this.parkingInfoList = parkingInfoList;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ParkingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingAdapter.ViewHolder holder, int position) {
        ParkingInfo parkingInfo = parkingInfoList.get(position);
        holder.parkingName.setText(parkingInfo.getParkingName());
        holder.parkingAddress.setText(parkingInfo.getParkingAddress());
        if(parkingInfo.getAvailableSlots() > 0) {
            holder.availableSlots.setText("AVAILABLE");
            holder.availableSlots.setTextColor(Color.GREEN);
        }
        else{
            holder.availableSlots.setText("FULL");
            holder.availableSlots.setTextColor(Color.RED);
        }
//        holder.totalSlots.setText(String.valueOf(parkingInfo.getTotalSlots()));
    }

    @Override
    public int getItemCount() {
        return parkingInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        LinearLayout parkingLinearLayout;
        TextView parkingName;
        TextView parkingAddress;
        TextView availableSlots;
//        TextView totalSlots;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            parkingLinearLayout = itemView.findViewById(R.id.parkingLinearLayout);
            parkingName = itemView.findViewById(R.id.parkingName);
            parkingAddress = itemView.findViewById(R.id.parkingAddress);
            availableSlots = itemView.findViewById(R.id.parkingAvailableSlots);
//            totalSlots = itemView.findViewById(R.id.parkingTotalSlots);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.e("in adapter", "onclick");
            itemListener.recyclerViewListClicked(view, this.getPosition());
        }
    }
}
