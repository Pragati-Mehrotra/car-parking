package com.carparking.api.Service;

import com.carparking.api.Entity.Booking;
import com.carparking.api.Entity.History;
import com.carparking.api.Entity.Parking;
import com.carparking.api.Entity.User;
import com.carparking.api.Repository.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCrudRepository userCrudRepository;

    @Autowired
    BookingCrudRepository bookingCrudRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    HistoryCrudRepository historyCrudRepository;

    @Autowired
    ParkingRepository parkingRepository;

    @Autowired
    ParkingCrudRepository parkingCrudRepository;

    @Override
    public Booking saveBooking(Booking booking) {

        do {
            int randomPin = (int)(Math.random()* 9000)+ 1000;
            Booking existingBooking = bookingRepository.findByParkingIdAndInOtp(booking.getParkingId(), randomPin);
            if(existingBooking == null) {
                booking.setInOtp(randomPin);
                break;
            }
        }
        while(booking.getInOtp() == null);
        Date date = new Date();
        Long inTime = date.getTime();
        booking.setInTime(inTime);
        booking.setStatus("Booked");
        User user = userRepository.findByUserId(booking.getUserId());
        Integer previousBalance = user.getBalance();
        Double bill;
        if (booking.getSlotDuration() <= 2) {
            bill = (double)40;
        }
        else if (booking.getSlotDuration() > 2 && booking.getSlotDuration() <= 4) {
            bill = (double)60;
        }
        else if (booking.getSlotDuration() > 4 && booking.getSlotDuration() <=8) {
            bill = (double)80;
        }
        else {
            bill = (double)(80 + (booking.getSlotDuration()-8)*20);
        }
        if (previousBalance < 0) {
            bill = bill - previousBalance;
            user.setBalance(0);
            User savedUser = userCrudRepository.save(user);
        }
        booking.setBill(bill);
        Integer parkingId = booking.getParkingId();
        Booking savedBooking = bookingCrudRepository.save(booking);
        if(savedBooking != null) {
            Parking parking = parkingRepository.findByParkingId(parkingId);
            Integer availableSlots = parking.getAvailableSlots() - 1;
            parking.setAvailableSlots(availableSlots);
            Parking savedParking = parkingCrudRepository.save(parking);
        }
        return savedBooking;
    }

    @Override
    public Object getBookingById(Integer bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if(booking != null) {
            return booking;
        }
        else {
            JSONObject message = new JSONObject();
            message.put("error","Booking Id does not exist.Please enter a valid booking Id.");
            return message;
        }
    }

    @Override
    public Booking checkout(Integer bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        booking.setStatus("CheckedOut");

        do {
            int randomPin = (int)(Math.random()* 9000)+ 1000;
            Booking existingBooking = bookingRepository.findByParkingIdAndOutOtp(booking.getParkingId(), randomPin);
            if(existingBooking == null) {
                booking.setOutOtp(randomPin);
                break;
            }
        }
        while(booking.getOutOtp() == null);
        Booking updatedBooking = bookingCrudRepository.save(booking);
        return updatedBooking;
    }

    @Override
    public History cancelBooking(Integer bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        Parking parking = parkingRepository.findByParkingId(booking.getParkingId());
        History history = new History();
        history.setBookingId(booking.getBookingId());
        history.setParkingId(booking.getParkingId());
        history.setUserId(booking.getUserId());
        history.setBill(booking.getBill());
        history.setStatus("Cancelled");
        history.setInTime(booking.getInTime());
        history.setOutTime(booking.getOutTime());
        history.setSlotDuration(booking.getSlotDuration());
        History savedHistory = historyCrudRepository.save(history);
        bookingCrudRepository.deleteById(bookingId);
        Integer availableSlots = parking.getAvailableSlots() + 1;
        parking.setAvailableSlots(availableSlots);
        Parking savedParking = parkingCrudRepository.save(parking);
        String parkingName = parking.getParkingName();
        savedHistory.setParkingName(parkingName);
        return savedHistory;
    }

    @Override
    public Object getActiveBookings(Integer userId) {
        List <Booking> bookingList = bookingRepository.findByUserId(userId);
        return bookingList;
    }
}
