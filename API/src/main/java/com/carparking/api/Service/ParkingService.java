package com.carparking.api.Service;

import com.carparking.api.Entity.Parking;
//import com.carparking.api.Repository.ParkingCrudRepository;
import com.carparking.api.Repository.ParkingCrudRepository;
import com.carparking.api.Repository.ParkingRepository;
import com.carparking.api.Utils.GeoTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingService implements IParkingService {

    @Autowired
    ParkingRepository parkingRepository;

    @Autowired
    ParkingCrudRepository parkingCrudRepository;

    @Override
    public List<Parking> getParkings(){
        return parkingRepository.findAllBy();
    }

    @Override
    public List<Parking> getParkingsNearby(Double latitude, Double longitude, Integer radius) {
        List<Parking> allParkings = parkingRepository.findAllBy();
        List<Parking> nearbyParkings = new ArrayList<>();
        System.out.println("---------------------------------------->>>Total parkings : " + allParkings.size());
        for(Parking parking: allParkings){
            double plat = parking.getLatitude();
            double plon = parking.getLongitude();
            double distance = GeoTools.getGeoDistance(latitude,plat,longitude, plon);
            System.out.println("---------------------------------------->>>distance : " + distance);
            if((int)distance <= radius)
                nearbyParkings.add(parking);
        }
        return nearbyParkings;
    }

    @Override
    public Parking saveParking(Parking parking){
        return parkingCrudRepository.save(parking);
    }

}
