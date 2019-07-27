package com.carparking.api.Service;

import com.carparking.api.Entity.Parking;

import java.util.List;

public interface IParkingService {
    public List<Parking> getParkings();
    public List<Parking> getParkingsNearby(Double latitude, Double longitude, Integer radius);
    public Parking saveParking(Parking parking);
}
