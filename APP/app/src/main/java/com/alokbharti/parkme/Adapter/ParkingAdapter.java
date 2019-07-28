package com.alokbharti.parkme.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alokbharti.parkme.ParkingInfo;
import com.alokbharti.parkme.R;

import java.util.List;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ViewHolder> {

    private List<ParkingInfo> parkingInfoList;
    private View.OnClickListener onClickListener;

    public ParkingAdapter(List<ParkingInfo> parkingInfoList, View.OnClickListener onClickListener) {
        this.parkingInfoList = parkingInfoList;
        onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ParkingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_list_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingAdapter.ViewHolder holder, int position) {
        ParkingInfo parkingInfo = parkingInfoList.get(position);
        holder.parkingName.setText(parkingInfo.getParkingName());
        holder.parkingAddress.setText(parkingInfo.getParkingAddress());
        holder.availableSlots.setText(String.valueOf(parkingInfo.getAvailableSlots()));
        holder.totalSlots.setText(String.valueOf(parkingInfo.getTotalSlots()));
        holder.parkingLinearLayout.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return parkingInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parkingLinearLayout;
        TextView parkingName;
        TextView parkingAddress;
        TextView availableSlots;
        TextView totalSlots;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parkingLinearLayout = itemView.findViewById(R.id.parkingLinearLayout);
            parkingName = itemView.findViewById(R.id.parkingName);
            parkingAddress = itemView.findViewById(R.id.parkingAddress);
            availableSlots = itemView.findViewById(R.id.parkingAvailableSlots);
            totalSlots = itemView.findViewById(R.id.parkingTotalSlots);
        }
    }
}
