package com.alokbharti.parkme.Adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alokbharti.parkme.Interfaces.RecyclerViewClickListener;
import com.alokbharti.parkme.Pojo.History;
import com.alokbharti.parkme.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    public List<History> historyList;
    public RecyclerViewClickListener itemListener;

    public HistoryAdapter(List<History> historyList, RecyclerViewClickListener itemListener){
        this.historyList = historyList;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.parkingName.setText(history.getParkingName());
        String status = history.getStatus();
        if(status.equals("Cancelled"))
            holder.status.setTextColor(Color.RED);
        else if(status.equals("Closed"))
            holder.status.setTextColor(Color.parseColor("#2e7d32"));
        holder.status.setText(status);
        holder.bill.setText("₹ " + String.valueOf(history.getBill()));
        Date date = new Date(history.getInTime());
        Format format = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        holder.time.setText(String.valueOf(format.format(date)));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView parkingName,status,bill,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parkingName = itemView.findViewById(R.id.parkingNameHistory);
            status = itemView.findViewById(R.id.statusHistoy);
            bill = itemView.findViewById(R.id.billHistory);
            time = itemView.findViewById(R.id.timeHistory);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e("history List","onClick");
            itemListener.recyclerViewListClicked(v, this.getPosition());
        }
    }
}
