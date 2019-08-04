package com.alokbharti.parkme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alokbharti.parkme.Adapter.HistoryAdapter;
import com.alokbharti.parkme.Interfaces.HistoryInterface;
import com.alokbharti.parkme.Interfaces.RecyclerViewClickListener;
import com.alokbharti.parkme.Pojo.History;
import com.alokbharti.parkme.Utilities.APIHelper;
import com.alokbharti.parkme.Utilities.GlobalConstants;

import java.util.List;

public class HistoryActivity extends AppCompatActivity implements HistoryInterface, RecyclerViewClickListener {

    RecyclerView historyRecyclerView;
    APIHelper apiHelper = new APIHelper(this);
    HistoryAdapter historyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("Previous Bookings");

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiHelper.getUserHistory(GlobalConstants.currentUserId);

    }

    @Override
    public void onGetHistorySuccess(List<History> historyList) {
        System.out.println("----------------------------------------------------------------->>>>Data Recieved" + historyList);
        if(historyList.size()==0){
            Toast.makeText(this,"You dont have any previous bookings",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,historyList.size() + " previous bookings",Toast.LENGTH_SHORT).show();
            historyAdapter = new HistoryAdapter(historyList, this);
            historyRecyclerView.setAdapter(historyAdapter);
        }
    }

    @Override
    public void onGetHistoryFailed() {
        System.out.println("------------------------------------------------------------------>>>>Data Not Received");
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
