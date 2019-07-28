package com.carparking.api.Service;

import com.carparking.api.Entity.History;
import com.carparking.api.Entity.Parking;
import com.carparking.api.Entity.Booking;
//import com.carparking.api.Repository.ParkingCrudRepository;
import com.carparking.api.Repository.*;
import com.carparking.api.Utils.GeoTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
//import org.joda.time.Instant;

@Service
public class ParkingService implements IParkingService {

    @Autowired
    ParkingRepository parkingRepository;

    @Autowired
    ParkingCrudRepository parkingCrudRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    BookingCrudRepository bookingCrudRepository;

    @Autowired
    HistoryCrudRepository historyCrudRepository;

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

    @Override
    public String driveIn(Integer parkingId, Integer inOtp) {
        Booking booking = bookingRepository.findByParkingIdAndInOtp(parkingId, inOtp);
        booking.setStatus("Parked");
        Booking savedBooking = bookingCrudRepository.save(booking);
        if(savedBooking != null) {
            return "Drive in successfull";
        }
        else {
            return "Drive in failed";
        }
    }

    @Override
    public String driveOut(Integer parkingId, Integer outOtp) {
        Booking booking = bookingRepository.findByParkingIdAndOutOtp(parkingId, outOtp);
        History history = new History();
        history.setBookingId(booking.getBookingId());
        history.setParkingId(booking.getParkingId());
        history.setUserId(booking.getUserId());
        history.setBill(booking.getBill());
        history.setInTime(booking.getInTime());
        Date date = new Date();
        Long outTime = date.getTime();
        booking.setOutTime(outTime);
        history.setOutTime(booking.getOutTime());
        history.setStatus("Closed");
        history.setSlotDuration(booking.getSlotDuration());
        System.out.println("---" + history);
        History savedHistory = historyCrudRepository.save(history);
        Integer bookingId = booking.getBookingId();
        bookingCrudRepository.deleteById(bookingId);
        if(savedHistory != null) {
            Parking parking = parkingRepository.findByParkingId(parkingId);
            Integer availableSlots = parking.getAvailableSlots() + 1;
            parking.setAvailableSlots(availableSlots);
            Parking savedParking = parkingCrudRepository.save(parking);
            return "Drive out successfull";
        }
        else {
            return "Drive out failed";
        }
    }

}
