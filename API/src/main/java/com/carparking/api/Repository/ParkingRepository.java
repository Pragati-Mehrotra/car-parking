package com.carparking.api.Repository;

import com.carparking.api.Entity.Parking;
import org.springframework.data.repository.Repository;

import java.util.List;
//import org.springframework.stereotype.Repository;

public interface ParkingRepository extends Repository<Parking, Integer> {

    public List<Parking> findAllBy();

    Parking findByParkingId(Integer parkingId);
}
