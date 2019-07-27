package com.carparking.api.Service;

import com.carparking.api.Entity.Parking;
//import com.carparking.api.Repository.ParkingCrudRepository;
import com.carparking.api.Repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingService {

    @Autowired
    ParkingRepository parkingRepository;

    public List<Parking> getParkings(){
        return parkingRepository.findAllBy();
    }
}
