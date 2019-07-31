package com.carparking.api.Service;

import com.carparking.api.Entity.History;
import com.carparking.api.Entity.Parking;
import com.carparking.api.Entity.Booking;
//import com.carparking.api.Repository.ParkingCrudRepository;
import com.carparking.api.Entity.User;
import com.carparking.api.Repository.*;
import com.carparking.api.Utils.GeoTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.concurrent.Executor;
//import org.joda.time.Instant;

@Service
public class ParkingService implements IParkingService {
//    @Autowired
//    @Qualifier("scheduler")
//    private Executor scheduleExecutor () {
//
//    }


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

    @Autowired
    UserCrudRepository userCrudRepository;

    @Autowired
    UserRepository userRepository;

//    @Scheduled(fixedDelay = 10000)
//    public void runScheduler() {
//        List<Parking> parkings = parkingRepository.findAllBy();
//        System.out.println("---------" + parkings);
//    }

    //    @Async("scheduler")
//    public void scheduleCall() {
//        try {
//            runScheduler();
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    public List<Parking> getParkings() {
        return parkingRepository.findAllBy();
    }

    @Override
    public List<Parking> getParkingsNearby(Double latitude, Double longitude, Integer radius) {
        List<Parking> allParkings = parkingRepository.findAllBy();
        List<Parking> nearbyParkings = new ArrayList<>();
        System.out.println("---------------------------------------->>>Total parkings : " + allParkings.size());
        for (Parking parking : allParkings) {
            double plat = parking.getLatitude();
            double plon = parking.getLongitude();
            double distance = GeoTools.getGeoDistance(latitude, plat, longitude, plon);
            System.out.println("---------------------------------------->>>distance : " + distance);
            if ((int) distance <= radius)
                nearbyParkings.add(parking);
        }
        return nearbyParkings;
    }

    @Override
    public Parking saveParking(Parking parking) {
        return parkingCrudRepository.save(parking);
    }

    @Override
    public String driveIn(Integer parkingId, Integer inOtp) {
        List<Booking> existingBooking = bookingRepository.findByParkingId(parkingId);
        if (existingBooking.size() == 0) {
            return "Invalid ParkingId. Please Enter valid parking Id.";
        } else {
            Booking booking = bookingRepository.findByInOtp(inOtp);
            if (booking == null) {
                return "Invalid OTP. Please Enter valid OTP.";
            } else {
                booking = bookingRepository.findByParkingIdAndInOtp(parkingId, inOtp);
                if (booking != null) {
                    booking.setStatus("Parked");
                    Booking savedBooking = bookingCrudRepository.save(booking);
                    if (savedBooking != null) {
                        return "Drive in successfull.";
                    } else {
                        return "success Something went wrong. Please try again.";
                    }
                } else {
                    return "Something went wrong. Please try again.";
                }
            }
        }
    }

    @Override
    public String driveOut(Integer parkingId, Integer outOtp) {

        List<Booking> existingBooking = bookingRepository.findByParkingId(parkingId);
        if (existingBooking.size() == 0) {
            return "Invalid ParkingId. Please Enter valid parking Id.";
        } else {
            Booking booking = bookingRepository.findByOutOtp(outOtp);
            if (booking == null) {
                return "Invalid OTP. Please Enter valid OTP.";
            } else {
                booking = bookingRepository.findByParkingIdAndOutOtp(parkingId, outOtp);
                if (booking != null) {
                    History history = new History();
                    history.setBookingId(booking.getBookingId());
                    history.setParkingId(booking.getParkingId());
                    history.setUserId(booking.getUserId());
                    history.setInTime(booking.getInTime());
                    history.setSlotDuration(booking.getSlotDuration());
                    Date date = new Date();
                    Long outTime = date.getTime();
                    booking.setOutTime(outTime);
                    history.setOutTime(booking.getOutTime());
                    Long duration = (history.getOutTime() - history.getInTime()) / 3600000;
                    Integer slotDuration = history.getSlotDuration();
                    if (duration > slotDuration) {
                        Double newbill;
                        if (duration <= 2) {
                            newbill = (double)40;
                        }
                        else if (duration > 2 && duration <= 4) {
                            newbill = (double)60;
                        }
                        else if (duration > 4 && duration <=8) {
                            newbill = (double)80;
                        }
                        else {
                            newbill = (double) (80 + (duration - 8) * 20);
                        }
                        Double extraCharge = newbill - booking.getBill();
                        User user = userRepository.findByUserId(booking.getUserId());
                        Integer balance = (int) (user.getBalance() - extraCharge);
                        user.setBalance(balance);
                        User savedUser = userCrudRepository.save(user);
                        booking.setBill(newbill);
                    }
                    history.setBill(booking.getBill());
                    history.setStatus("Closed");
                    History savedHistory = historyCrudRepository.save(history);
                    Integer bookingId = booking.getBookingId();
                    bookingCrudRepository.deleteById(bookingId);
                    if (savedHistory != null) {
                        Parking parking = parkingRepository.findByParkingId(parkingId);
                        Integer availableSlots = parking.getAvailableSlots() + 1;
                        parking.setAvailableSlots(availableSlots);
                        Parking savedParking = parkingCrudRepository.save(parking);
                        return "Drive out successfull";
                    } else {
                        return "Drive out failed";
                    }
                } else {
                    return "Something went wrong. Please try again.";
                }
            }
        }

    }
}

