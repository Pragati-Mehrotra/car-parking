package com.alokbharti.parkme.Interfaces;

import com.alokbharti.parkme.ParkingInfo;

import java.util.List;

public interface LocationInterface {
    void onGetParkingList(List<ParkingInfo> parkingList);
    void onFailedGettingparkingList();
}
