package com.alokbharti.parkme.Interfaces;

import com.alokbharti.parkme.Pojo.History;

import java.util.List;

public interface HistoryInterface {

    void onGetHistorySuccess(List<History> historyList);
    void onGetHistoryFailed();
}
