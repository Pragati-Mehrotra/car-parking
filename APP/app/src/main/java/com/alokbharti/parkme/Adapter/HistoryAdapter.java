package com.alokbharti.parkme.Adapter;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.parkingName.setText(history.getParkingName());
        holder.status.setText(history.getStatus());
        holder.bill.setText(String.valueOf(history.getBill()));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView parkingName,status,bill;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parkingName = itemView.findViewById(R.id.parkingNameHistory);
            status = itemView.findViewById(R.id.statusHistoy);
            bill = itemView.findViewById(R.id.billHistory);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e("history List","onClick");
            itemListener.recyclerViewListClicked(v, this.getPosition());
        }
    }
}
